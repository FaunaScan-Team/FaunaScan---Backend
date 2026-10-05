package com.upc.faunascan.Services;

import com.upc.faunascan.dto.AvistamientoFavoritoDTO;
import java.util.List;

public interface AvistamientoFavoritoService {
    AvistamientoFavoritoDTO marcar(AvistamientoFavoritoDTO dto);
    List<AvistamientoFavoritoDTO> listarPorUsuario(Long idUsuario);
    void desmarcar(Long idUsuario, Long idAvistamiento);
}
