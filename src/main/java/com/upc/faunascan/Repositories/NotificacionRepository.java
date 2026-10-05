package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Bandeja de notificaciones del usuario (HU-11, HU-12, HU-13)
    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

    List<Notificacion> findByUsuario_IdUsuarioAndLeidoFalse(Long idUsuario);

    List<Notificacion> findByUsuario_IdUsuarioAndTipoIgnoreCaseOrderByFechaCreacionDesc(Long idUsuario, String tipo);

    boolean existsByIdNotificacionAndUsuario_Correo(Long idNotificacion, String correo);
}
