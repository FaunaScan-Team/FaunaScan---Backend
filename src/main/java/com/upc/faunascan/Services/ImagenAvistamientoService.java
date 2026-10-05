package com.upc.faunascan.Services;

import com.upc.faunascan.dto.ImagenAvistamientoDTO;
import java.util.List;

public interface ImagenAvistamientoService {
    ImagenAvistamientoDTO agregar(ImagenAvistamientoDTO dto);
    List<ImagenAvistamientoDTO> listarPorAvistamiento(Long idAvistamiento);
    void eliminar(Long id);
}
