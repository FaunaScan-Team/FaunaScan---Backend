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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Identificacion de especies SIMULADA (HU-27, HU-28, HU-37, HU-38, HU-51).
 * No hay un modelo real: se eligen 3 especies del catalogo con confianzas
 * pseudoaleatorias calculadas a partir de la imagen, de modo que la misma
 * imagen siempre devuelve el mismo resultado. Para conectar un modelo real
 * solo hay que reemplazar el metodo generarSugerencias.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IAServiceImpl implements IAService {
    public static final String MODELO = "FaunaScan-Simulado-v1";
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

    @Override
    @Transactional
    public ResultadoIdentificacionDTO identificarEspecie(IdentificarEspecieDTO dto) {
        long inicio = System.nanoTime();
        List<SugerenciaEspecieDTO> sugerencias = generarSugerencias(dto.getImagen());
        long tiempoMs = (System.nanoTime() - inicio) / 1_000_000;

        double confianzaMaxima = sugerencias.isEmpty() ? 0.0 : sugerencias.get(0).getConfianza();
        boolean identificada = confianzaMaxima >= umbralConfianza;

        ResultadoIdentificacionDTO resultado = new ResultadoIdentificacionDTO();
        resultado.setEstado(identificada ? ESTADO_IDENTIFICADA : ESTADO_NO_IDENTIFICADA);
        resultado.setConfianzaMaxima(confianzaMaxima);
        resultado.setUmbralConfianza(umbralConfianza);
        resultado.setEspecieSugerida(identificada ? sugerencias.get(0) : null);
        resultado.setSugerencias(sugerencias);
        resultado.setMensaje(identificada
                ? "Especie identificada con " + Math.round(confianzaMaxima * 100) + "% de confianza."
                : "No se pudo identificar la especie con suficiente confianza. Selecciona la especie manualmente.");
        resultado.setModelo(MODELO);
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

    // Top 3 del catalogo con confianzas decrecientes; la semilla depende de la imagen
    private List<SugerenciaEspecieDTO> generarSugerencias(String imagen) {
        List<Especie> candidatas = new ArrayList<>(especieRepository.findAll(Sort.by("idEspecie")));
        Random random = new Random(imagen.hashCode());
        Collections.shuffle(candidatas, random);

        List<SugerenciaEspecieDTO> sugerencias = new ArrayList<>();
        double confianza = 0.45 + random.nextDouble() * 0.53;
        for (Especie especie : candidatas.subList(0, Math.min(3, candidatas.size()))) {
            sugerencias.add(new SugerenciaEspecieDTO(especie.getIdEspecie(), especie.getNombreComun(),
                    especie.getNombreCientifico(), redondear(confianza)));
            confianza = confianza * (0.25 + random.nextDouble() * 0.35);
        }
        return sugerencias;
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
        json.put("metadatos_modelo", MODELO);
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
