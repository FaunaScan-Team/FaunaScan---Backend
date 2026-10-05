package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.ImagenAvistamiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenAvistamientoRepository extends JpaRepository<ImagenAvistamiento, Long> {

    // Galeria de fotos de un avistamiento (HU-10)
    List<ImagenAvistamiento> findByAvistamiento_IdAvistamiento(Long idAvistamiento);

    boolean existsByIdImagenAndAvistamiento_Usuario_Correo(Long idImagen, String correo);

    // HU-37: imagenes con resultado de IA para medir la precision del modelo
    @Query("select i from ImagenAvistamiento i join fetch i.avistamiento a join fetch a.especie where i.resultadoIa is not null")
    List<ImagenAvistamiento> listarConResultadoIa();
}
