package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Familia;
import com.upc.faunascan.dto.FamiliaDTO;
import java.util.List;

public interface FamiliaService {
    List<FamiliaDTO> listar();
    FamiliaDTO obtenerPorId(Long id);
    FamiliaDTO crear(FamiliaDTO dto);
    FamiliaDTO actualizar(Long id, FamiliaDTO dto);
    void eliminar(Long id);
    Familia buscarEntidad(Long id);
}
