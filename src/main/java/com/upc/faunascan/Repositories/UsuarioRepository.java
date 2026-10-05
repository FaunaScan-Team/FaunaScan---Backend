package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Para login y validar que el correo no este duplicado (HU-01, HU-02)
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByIdUsuarioAndCorreo(Long idUsuario, String correo);

    // HU-34/HU-40: destinatarios de avisos masivos
    List<Usuario> findByEstadoTrue();

    // HU-59: voluntarios activos que no registran avistamientos desde "limite" y aun no recibieron el recordatorio
    // (se usa la fecha de registro y no la del avistamiento, para no molestar a quien acaba de sincronizar registros antiguos)
    @Query("select u from Usuario u where u.rol.nombre = 'ROLE_VOLUNTARIO' and u.estado = true " +
           "and coalesce((select max(a.fechaRegistro) from Avistamiento a where a.usuario = u), u.fechaRegistro) < :limite " +
           "and not exists (select n from Notificacion n where n.usuario = u and n.tipo = 'Alerta' " +
           "and n.mensaje = :mensaje and n.fechaCreacion > :limite)")
    List<Usuario> buscarVoluntariosInactivos(@Param("limite") LocalDateTime limite, @Param("mensaje") String mensaje);
}
