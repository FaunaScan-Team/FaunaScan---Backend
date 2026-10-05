package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.dto.UbicacionDTO;
import java.util.List;

public interface UbicacionService {
    List<UbicacionDTO> listar();
    UbicacionDTO obtenerPorId(Long id);
    UbicacionDTO crear(UbicacionDTO dto);
    UbicacionDTO actualizar(Long id, UbicacionDTO dto);
    void eliminar(Long id);
    Ubicacion buscarEntidad(Long id);
}
