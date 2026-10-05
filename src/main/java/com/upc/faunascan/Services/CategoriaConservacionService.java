package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.CategoriaConservacion;
import com.upc.faunascan.dto.CategoriaConservacionDTO;
import java.util.List;

public interface CategoriaConservacionService {
    List<CategoriaConservacionDTO> listar();
    CategoriaConservacionDTO obtenerPorId(Long id);
    CategoriaConservacionDTO crear(CategoriaConservacionDTO dto);
    CategoriaConservacionDTO actualizar(Long id, CategoriaConservacionDTO dto);
    void eliminar(Long id);
    CategoriaConservacion buscarEntidad(Long id);
}
