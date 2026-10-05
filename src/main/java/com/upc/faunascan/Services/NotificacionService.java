package com.upc.faunascan.Services;

import com.upc.faunascan.dto.NotificacionDTO;
import java.util.List;

public interface NotificacionService {
    NotificacionDTO crear(NotificacionDTO dto);
    List<NotificacionDTO> listarPorUsuario(Long idUsuario);
    List<NotificacionDTO> listarPorUsuarioYCategoria(Long idUsuario, String categoria);
    List<NotificacionDTO> listarNoLeidas(Long idUsuario);
    NotificacionDTO marcarLeida(Long idNotificacion);
    void eliminar(Long id);
    int notificarEspecieVulnerable(Long idAvistamiento);
    void notificarValidacion(Long idAvistamiento);
    int enviarAvisoSistema(String mensaje);
    int enviarRecordatorios();
}
