package com.upc.faunascan.security.services;

import com.upc.faunascan.Repositories.AvistamientoCompartidoRepository;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import com.upc.faunascan.Repositories.NotificacionRepository;
import com.upc.faunascan.Repositories.ReporteRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Reglas de "dueno del recurso" para usar en @PreAuthorize, por ejemplo:
 * {@code @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")}
 * Si el recurso no existe se deja pasar para que el service responda 404.
 */
@Component("autorizacion")
@RequiredArgsConstructor
public class Autorizacion {
    private final UsuarioRepository usuarioRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ImagenAvistamientoRepository imagenAvistamientoRepository;
    private final NotificacionRepository notificacionRepository;
    private final AvistamientoCompartidoRepository avistamientoCompartidoRepository;
    private final ReporteRepository reporteRepository;

    // el {idUsuario} de la ruta es el usuario autenticado
    public boolean esUsuario(Long idUsuario) {
        return !usuarioRepository.existsById(idUsuario)
                || usuarioRepository.existsByIdUsuarioAndCorreo(idUsuario, correoActual());
    }

    public boolean esDuenoAvistamiento(Long idAvistamiento) {
        return !avistamientoRepository.existsById(idAvistamiento)
                || avistamientoRepository.existsByIdAvistamientoAndUsuario_Correo(idAvistamiento, correoActual());
    }

    // idImagen null = no se asocia a ninguna imagen (HU-27 sin guardar resultado)
    public boolean esDuenoImagen(Long idImagen) {
        return idImagen == null
                || !imagenAvistamientoRepository.existsById(idImagen)
                || imagenAvistamientoRepository.existsByIdImagenAndAvistamiento_Usuario_Correo(idImagen, correoActual());
    }

    public boolean esDuenoNotificacion(Long idNotificacion) {
        return !notificacionRepository.existsById(idNotificacion)
                || notificacionRepository.existsByIdNotificacionAndUsuario_Correo(idNotificacion, correoActual());
    }

    public boolean esOrigenCompartido(Long idCompartido) {
        return !avistamientoCompartidoRepository.existsById(idCompartido)
                || avistamientoCompartidoRepository.existsByIdCompartidoAndInvestigadorOrigen_Correo(idCompartido, correoActual());
    }

    public boolean esDuenoReporte(Long idReporte) {
        return !reporteRepository.existsById(idReporte)
                || reporteRepository.existsByIdReporteAndUsuario_Correo(idReporte, correoActual());
    }

    private String correoActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
