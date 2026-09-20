package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Notificacion;
import com.upc.faunascan.Repositories.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    // US11/US12/US13: generar una notificacion (avistamiento cercano, alerta, etc.)
    public Notificacion crear(Notificacion notificacion) {
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioOrderByFechaCreacionDesc(idUsuario);
    }

    public List<Notificacion> listarNoLeidas(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioAndLeidoFalse(idUsuario);
    }

    public Notificacion marcarLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con id: " + idNotificacion));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }
}
