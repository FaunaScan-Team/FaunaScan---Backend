package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenAvistamientoService {

    private final ImagenAvistamientoRepository imagenAvistamientoRepository;

    // US10: adjuntar foto(s) a un avistamiento
    public ImagenAvistamiento agregar(ImagenAvistamiento imagen) {
        return imagenAvistamientoRepository.save(imagen);
    }

    public List<ImagenAvistamiento> listarPorAvistamiento(Long idAvistamiento) {
        return imagenAvistamientoRepository.findByAvistamiento_IdAvistamiento(idAvistamiento);
    }

    public void eliminar(Long id) {
        imagenAvistamientoRepository.deleteById(id);
    }
}
