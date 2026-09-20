package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Bandeja de notificaciones del usuario (US11, US12, US13)
    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

    List<Notificacion> findByUsuario_IdUsuarioAndLeidoFalse(Long idUsuario);
}
