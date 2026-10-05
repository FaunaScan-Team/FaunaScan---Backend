package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Entities.CategoriaConservacion;
import com.upc.faunascan.Entities.Notificacion;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.NotificacionRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.Services.NotificacionService;
import com.upc.faunascan.dto.NotificacionDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements NotificacionService {
    public static final String TIPO_CRITICA = "Crítica";
    public static final String TIPO_SISTEMA = "Sistema";
    public static final String TIPO_ALERTA = "Alerta";
    public static final String MENSAJE_RECORDATORIO =
            "Hace varios días que no registras un avistamiento. ¡Continúa participando!";
    private static final int DIAS_SIN_ACTIVIDAD = 7;

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    public NotificacionDTO crear(NotificacionDTO dto) {
        Notificacion notificacion = modelMapper.map(dto, Notificacion.class);
        notificacion.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);
        return aDTO(notificacionRepository.save(notificacion));
    }

    @Override
    public List<NotificacionDTO> listarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioOrderByFechaCreacionDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    // HU-11/HU-12/HU-13: la categoria del query string se guarda en la columna tipo
    @Override
    public List<NotificacionDTO> listarPorUsuarioYCategoria(Long idUsuario, String categoria) {
        String tipo = switch (categoria.toLowerCase()) {
            case "critica" -> TIPO_CRITICA;
            case "sistema" -> TIPO_SISTEMA;
            case "alerta" -> TIPO_ALERTA;
            default -> categoria;
        };
        return notificacionRepository.findByUsuario_IdUsuarioAndTipoIgnoreCaseOrderByFechaCreacionDesc(idUsuario, tipo)
                .stream().map(this::aDTO).toList();
    }

    @Override
    public List<NotificacionDTO> listarNoLeidas(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioAndLeidoFalse(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    @Override
    public NotificacionDTO marcarLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion no encontrada con id: " + idNotificacion));
        notificacion.setLeido(true);
        return aDTO(notificacionRepository.save(notificacion));
    }

    @Override
    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }

    // HU-34: alerta critica a todos los usuarios activos cuando se valida una especie vulnerable
    @Override
    @Transactional
    public int notificarEspecieVulnerable(Long idAvistamiento) {
        Avistamiento avistamiento = buscarAvistamiento(idAvistamiento);
        if (!"validado".equals(avistamiento.getEstadoValidacion())) {
            throw new RuntimeException("Solo se emiten alertas criticas de avistamientos validados");
        }
        if (!esEspecieVulnerable(avistamiento)) {
            throw new RuntimeException("La especie no esta en una categoria vulnerable o en peligro");
        }
        return enviarAlertaCritica(avistamiento);
    }

    // HU-13: aviso al autor cuando un investigador valida o rechaza su registro,
    // y HU-11/HU-34 si la especie validada es vulnerable
    @Override
    @Transactional
    public void notificarValidacion(Long idAvistamiento) {
        Avistamiento avistamiento = buscarAvistamiento(idAvistamiento);
        Usuario autor = avistamiento.getUsuario();
        if (permite(autor, "alertas")) {
            guardar(autor, TIPO_ALERTA, "Tu avistamiento de " + avistamiento.getEspecie().getNombreComun()
                    + " fue " + avistamiento.getEstadoValidacion() + " por un investigador.");
        }
        if ("validado".equals(avistamiento.getEstadoValidacion()) && esEspecieVulnerable(avistamiento)) {
            enviarAlertaCritica(avistamiento);
        }
    }

    // HU-12/HU-40: aviso de sistema o mantenimiento para todos los usuarios activos
    @Override
    @Transactional
    public int enviarAvisoSistema(String mensaje) {
        return enviarATodos(TIPO_SISTEMA, "sistema", mensaje);
    }

    // HU-59: recordatorio para voluntarios sin avistamientos en los ultimos 7 dias
    // (no se repite si ya recibieron el mismo recordatorio en ese periodo)
    @Override
    @Transactional
    public int enviarRecordatorios() {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_SIN_ACTIVIDAD);
        int enviados = 0;
        for (Usuario usuario : usuarioRepository.buscarVoluntariosInactivos(limite, MENSAJE_RECORDATORIO)) {
            if (permite(usuario, "alertas")) {
                guardar(usuario, TIPO_ALERTA, MENSAJE_RECORDATORIO);
                enviados++;
            }
        }
        log.info("Recordatorios de participacion enviados: {}", enviados);
        return enviados;
    }

    private int enviarAlertaCritica(Avistamiento avistamiento) {
        String direccion = avistamiento.getUbicacion() != null ? avistamiento.getUbicacion().getDireccion() : null;
        String mensaje = "Se registró un avistamiento de una especie vulnerable: "
                + avistamiento.getEspecie().getNombreComun()
                + " (" + avistamiento.getEspecie().getCategoriaConservacion().getNombre() + ")"
                + (direccion != null ? " en " + direccion : "") + ".";
        return enviarATodos(TIPO_CRITICA, "criticas", mensaje);
    }

    private int enviarATodos(String tipo, String preferencia, String mensaje) {
        int enviados = 0;
        for (Usuario usuario : usuarioRepository.findByEstadoTrue()) {
            if (permite(usuario, preferencia)) {
                guardar(usuario, tipo, mensaje);
                enviados++;
            }
        }
        return enviados;
    }

    private void guardar(Usuario usuario, String tipo, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    // HU-32: respeta los switches de preferencias_notificaciones (si no hay preferencia, se envia)
    private boolean permite(Usuario usuario, String clave) {
        String preferencias = usuario.getPreferenciasNotificaciones();
        if (preferencias == null || preferencias.isBlank()) {
            return true;
        }
        try {
            JsonNode valor = objectMapper.readTree(preferencias).get(clave);
            return valor == null || valor.isNull() || valor.asBoolean(true);
        } catch (RuntimeException e) {
            log.warn("Preferencias de notificacion invalidas para el usuario {}", usuario.getIdUsuario());
            return true;
        }
    }

    private boolean esEspecieVulnerable(Avistamiento avistamiento) {
        CategoriaConservacion categoria = avistamiento.getEspecie().getCategoriaConservacion();
        if (categoria == null || categoria.getNombre() == null) {
            return false;
        }
        String nombre = categoria.getNombre().toLowerCase();
        return nombre.contains("vulnerable") || nombre.contains("peligro");
    }

    private Avistamiento buscarAvistamiento(Long idAvistamiento) {
        return avistamientoRepository.findById(idAvistamiento)
                .orElseThrow(() -> new RecursoNoEncontradoException("Avistamiento no encontrado con id: " + idAvistamiento));
    }

    private NotificacionDTO aDTO(Notificacion notificacion) {
        NotificacionDTO dto = modelMapper.map(notificacion, NotificacionDTO.class);
        if (notificacion.getUsuario() != null) {
            dto.setIdUsuario(notificacion.getUsuario().getIdUsuario());
        }
        return dto;
    }
}
