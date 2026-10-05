package com.upc.faunascan.Services;

import com.upc.faunascan.dto.AvistamientoCompartidoDTO;
import java.util.List;

public interface AvistamientoCompartidoService {
    AvistamientoCompartidoDTO compartir(AvistamientoCompartidoDTO dto);
    List<AvistamientoCompartidoDTO> listarRecibidos(Long idUsuario);
    List<AvistamientoCompartidoDTO> listarEnviados(Long idUsuario);
    void eliminar(Long id);
}
