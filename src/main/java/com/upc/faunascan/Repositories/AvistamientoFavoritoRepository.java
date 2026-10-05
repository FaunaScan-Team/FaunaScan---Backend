package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.AvistamientoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvistamientoFavoritoRepository extends JpaRepository<AvistamientoFavorito, Long> {

    // Marcar / desmarcar favoritos (HU-26)
    List<AvistamientoFavorito> findByUsuario_IdUsuario(Long idUsuario);

    Optional<AvistamientoFavorito> findByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(
            Long idUsuario, Long idAvistamiento);

    boolean existsByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(Long idUsuario, Long idAvistamiento);
}
