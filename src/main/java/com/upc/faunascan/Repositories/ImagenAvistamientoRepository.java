package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.ImagenAvistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenAvistamientoRepository extends JpaRepository<ImagenAvistamiento, Long> {

    // Galeria de fotos de un avistamiento (US10)
    List<ImagenAvistamiento> findByAvistamiento_IdAvistamiento(Long idAvistamiento);
}
