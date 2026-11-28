package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Repositories.EspecieRepository;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import com.upc.faunascan.Services.IAService;
import com.upc.faunascan.dto.EvaluacionIADTO;
import com.upc.faunascan.dto.IdentificarEspecieDTO;
import com.upc.faunascan.dto.ResultadoIdentificacionDTO;
import com.upc.faunascan.dto.SugerenciaEspecieDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

// Identificacion de especies con Gemini (HU-27, HU-28, HU-30, HU-37, HU-38, HU-51).
// Se envia la foto y el catalogo; Gemini responde las 3 especies mas probables del catalogo con su confianza.
@Service
@RequiredArgsConstructor
@Slf4j
public class IAServiceImpl implements IAService {
    private static final String URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/models/";
    public static final String ESTADO_IDENTIFICADA = "identificada";
    public static final String ESTADO_NO_IDENTIFICADA = "no_identificada";

    private final EspecieRepository especieRepository;
    private final ImagenAvistamientoRepository imagenAvistamientoRepository;
    private final ObjectMapper objectMapper;

    // HU-51: confianza minima para dar por buena la sugerencia
    @Value("${ia.umbral-confianza:0.70}")
    private double umbralConfianza;

    // HU-38: tiempo maximo de procesamiento
    @Value("${ia.tiempo-maximo-ms:5000}")
    private long tiempoMaximoMs;

    // HU-37: precision minima esperada (Top-3)
    @Value("${ia.meta-precision:0.85}")
    private double metaPrecision;

    @Value("${gemini.api-key:}")
    private String geminiApiKey;

    @Value("${gemini.modelo:gemini-3.8-flash}")
    private String geminiModelo;

    // Tiempo de espera de la llamada; si se supera, la foto queda como no identificada
    @Value("${gemini.timeout-ms:10000}")
    private long geminiTimeoutMs;

    @Override
    @Transactional
    public ResultadoIdentificacionDTO identificarEspecie(IdentificarEspecieDTO dto) {
        long inicio = System.nanoTime();
        List<SugerenciaEspecieDTO> sugerencias;
        String falla = null;
        try {
            sugerencias = generarSugerencias(dto.getImagen());
        } catch (RuntimeException e) {
            log.warn("No se pudo identificar la especie con Gemini: {}", e.getMessage());
            sugerencias = new ArrayList<>();
            falla = e instanceof IllegalStateException
                    ? e.getMessage()
                    : "El servicio de IA no está disponible en este momento. Selecciona la especie manualmente.";
        }
        long tiempoMs = (System.nanoTime() - inicio) / 1_000_000;

        double confianzaMaxima = sugerencias.isEmpty() ? 0.0 : sugerencias.get(0).getConfianza();
        boolean identificada = confianzaMaxima >= umbralConfianza;

        ResultadoIdentificacionDTO resultado = new ResultadoIdentificacionDTO();
        resultado.setEstado(identificada ? ESTADO_IDENTIFICADA : ESTADO_NO_IDENTIFICADA);
        resultado.setConfianzaMaxima(confianzaMaxima);
        resultado.setUmbralConfianza(umbralConfianza);
        resultado.setEspecieSugerida(identificada ? sugerencias.get(0) : null);
        resultado.setSugerencias(sugerencias);
        if (identificada) {
            resultado.setMensaje("Especie identificada con " + Math.round(confianzaMaxima * 100) + "% de confianza.");
        } else if (falla != null) {
            resultado.setMensaje(falla);
        } else {
            resultado.setMensaje("No se pudo identificar la especie con suficiente confianza. Selecciona la especie manualmente.");
        }
        resultado.setModelo(geminiModelo);
        resultado.setTiempoProcesamientoMs(tiempoMs);
        resultado.setDentroDelTiempoMaximo(tiempoMs <= tiempoMaximoMs);
        if (tiempoMs > tiempoMaximoMs) {
            log.warn("La identificacion por IA tardo {} ms (maximo {} ms)", tiempoMs, tiempoMaximoMs);
        }

        // HU-28/HU-38/HU-51: guardar el resultado en imagenes_avistamiento.resultado_ia
        if (dto.getIdImagen() != null) {
            ImagenAvistamiento imagen = imagenAvistamientoRepository.findById(dto.getIdImagen())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Imagen no encontrada con id: " + dto.getIdImagen()));
            imagen.setResultadoIa(aJson(resultado));
            imagenAvistamientoRepository.save(imagen);
            resultado.setIdImagen(imagen.getIdImagen());
        }
        return resultado;
    }

    // HU-37: compara el Top-1/Top-3 guardado en resultado_ia con la especie final de los avistamientos validados
    @Override
    @Transactional(readOnly = true)
    public EvaluacionIADTO evaluarPrecision() {
        int evaluadas = 0;
        int aciertosTop1 = 0;
        int aciertosTop3 = 0;
        for (ImagenAvistamiento imagen : imagenAvistamientoRepository.listarConResultadoIa()) {
            if (!"validado".equals(imagen.getAvistamiento().getEstadoValidacion())) {
                continue;
            }
            List<Long> idsSugeridos = leerIdsSugeridos(imagen.getResultadoIa());
            if (idsSugeridos.isEmpty()) {
                continue;
            }
            Long especieReal = imagen.getAvistamiento().getEspecie().getIdEspecie();
            evaluadas++;
            if (idsSugeridos.get(0).equals(especieReal)) {
                aciertosTop1++;
            }
            if (idsSugeridos.contains(especieReal)) {
                aciertosTop3++;
            }
        }
        Double precisionTop1 = evaluadas == 0 ? null : redondear((double) aciertosTop1 / evaluadas);
        Double precisionTop3 = evaluadas == 0 ? null : redondear((double) aciertosTop3 / evaluadas);
        return new EvaluacionIADTO(evaluadas, aciertosTop1, aciertosTop3, precisionTop1, precisionTop3,
                metaPrecision, precisionTop3 != null && precisionTop3 >= metaPrecision);
    }

    // Top 3 del catalogo segun Gemini, ordenado de mayor a menor confianza
    private List<SugerenciaEspecieDTO> generarSugerencias(String imagen) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException("La identificación por IA no está configurada (falta GEMINI_API_KEY). Selecciona la especie manualmente.");
        }
        List<Especie> catalogo = especieRepository.findAll(Sort.by("idEspecie"));
        if (catalogo.isEmpty()) {
            return new ArrayList<>();
        }
        RestClient restClient = crearRestClient();
        String[] foto = leerImagen(restClient, imagen);

        Map<String, Object> cuerpo = Map.of(
                "contents", List.of(Map.of("parts", List.of(
                        Map.of("inline_data", Map.of("mime_type", foto[0], "data", foto[1])),
                        Map.of("text", armarInstrucciones(catalogo))))),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "temperature", 0));

        String respuesta = restClient.post()
                .uri(URL_GEMINI + geminiModelo + ":generateContent")
                .header("x-goog-api-key", geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(cuerpo)
                .retrieve()
                .body(String.class);

        JsonNode partes = objectMapper.readTree(respuesta).path("candidates").path(0).path("content").path("parts");
        for (JsonNode parte : partes) {
            if (parte.hasNonNull("text") && !parte.path("thought").asBoolean(false)) {
                return leerSugerencias(parte.get("text").asString(), catalogo);
            }
        }
        return new ArrayList<>();
    }

    private String armarInstrucciones(List<Especie> catalogo) {
        StringBuilder especies = new StringBuilder();
        for (Especie especie : catalogo) {
            especies.append(especie.getIdEspecie()).append(" | ")
                    .append(especie.getNombreComun()).append(" | ")
                    .append(especie.getNombreCientifico()).append('\n');
        }
        return "Eres un biólogo experto en fauna silvestre del Perú. Observa la fotografía e indica qué especies "
                + "del siguiente catálogo podrían ser el animal que aparece. Usa solo los ids del catálogo.\n\n"
                + "Catálogo (id | nombre común | nombre científico):\n" + especies
                + "\nResponde solo con este JSON: {\"sugerencias\": [{\"idEspecie\": 1, \"confianza\": 0.9}]}. "
                + "Incluye como máximo 3 especies, ordenadas de mayor a menor confianza, con confianza entre 0 y 1. "
                + "Si el animal no está en el catálogo o la foto no muestra un animal, usa confianzas por debajo de 0.3 "
                + "o devuelve la lista vacía.";
    }

    private List<SugerenciaEspecieDTO> leerSugerencias(String json, List<Especie> catalogo) {
        Map<Long, Especie> porId = new HashMap<>();
        for (Especie especie : catalogo) {
            porId.put(especie.getIdEspecie(), especie);
        }
        List<SugerenciaEspecieDTO> sugerencias = new ArrayList<>();
        Set<Long> usados = new HashSet<>();
        for (JsonNode item : objectMapper.readTree(json).path("sugerencias")) {
            long id = item.path("idEspecie").asLong(-1);
            Especie especie = porId.get(id);
            if (especie == null || !usados.add(id)) {
                continue;
            }
            double confianza = Math.max(0.0, Math.min(1.0, item.path("confianza").asDouble(0.0)));
            sugerencias.add(new SugerenciaEspecieDTO(especie.getIdEspecie(), especie.getNombreComun(),
                    especie.getNombreCientifico(), redondear(confianza)));
        }
        sugerencias.sort(Comparator.comparing(SugerenciaEspecieDTO::getConfianza).reversed());
        return sugerencias.size() > 3 ? new ArrayList<>(sugerencias.subList(0, 3)) : sugerencias;
    }

    // Devuelve {tipo MIME, datos en base64}. La imagen llega como data URL, como URL de Cloudinary o como base64 solo
    private String[] leerImagen(RestClient restClient, String imagen) {
        String valor = imagen.trim();
        if (valor.startsWith("data:")) {
            int coma = valor.indexOf(',');
            String tipo = valor.substring(5, valor.indexOf(';'));
            return new String[]{tipo, valor.substring(coma + 1)};
        }
        if (valor.startsWith("http://") || valor.startsWith("https://")) {
            ResponseEntity<byte[]> descarga = restClient.get().uri(valor).retrieve().toEntity(byte[].class);
            if (descarga.getBody() == null) {
                throw new IllegalStateException("No se pudo descargar la foto para analizarla.");
            }
            MediaType tipo = descarga.getHeaders().getContentType();
            String mime = tipo != null && "image".equals(tipo.getType()) ? tipo.getType() + "/" + tipo.getSubtype() : "image/jpeg";
            return new String[]{mime, Base64.getEncoder().encodeToString(descarga.getBody())};
        }
        return new String[]{"image/jpeg", valor};
    }

    private RestClient crearRestClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(httpClient);
        fabrica.setReadTimeout(Duration.ofMillis(geminiTimeoutMs));
        return RestClient.builder().requestFactory(fabrica).build();
    }

    private String aJson(ResultadoIdentificacionDTO resultado) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("estado", resultado.getEstado());
        SugerenciaEspecieDTO top = resultado.getEspecieSugerida();
        json.put("especie_sugerida", top != null ? top.getNombreComun() : null);
        json.put("id_especie_sugerida", top != null ? top.getIdEspecie() : null);
        json.put("porcentaje_confianza", Math.round(resultado.getConfianzaMaxima() * 1000) / 10.0);
        json.put("confianza_maxima", resultado.getConfianzaMaxima());
        json.put("umbral", resultado.getUmbralConfianza());
        List<Map<String, Object>> sugerencias = new ArrayList<>();
        for (SugerenciaEspecieDTO s : resultado.getSugerencias()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id_especie", s.getIdEspecie());
            item.put("nombre_comun", s.getNombreComun());
            item.put("confianza", s.getConfianza());
            sugerencias.add(item);
        }
        json.put("sugerencias", sugerencias);
        json.put("metadatos_modelo", geminiModelo);
        json.put("tiempo_ms", resultado.getTiempoProcesamientoMs());
        json.put("fecha_analisis", LocalDateTime.now().toString());
        return objectMapper.writeValueAsString(json);
    }

    private List<Long> leerIdsSugeridos(String resultadoIa) {
        List<Long> ids = new ArrayList<>();
        try {
            JsonNode sugerencias = objectMapper.readTree(resultadoIa).get("sugerencias");
            if (sugerencias != null) {
                for (JsonNode s : sugerencias) {
                    if (s.hasNonNull("id_especie")) {
                        ids.add(s.get("id_especie").asLong());
                    }
                }
            }
        } catch (RuntimeException e) {
            log.warn("resultado_ia con formato no reconocido: {}", e.getMessage());
        }
        return ids;
    }

    private double redondear(double valor) {
        return Math.round(valor * 10000) / 10000.0;
    }
}
