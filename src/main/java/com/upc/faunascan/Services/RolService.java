package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.dto.RolDTO;
import java.util.List;

public interface RolService {
    List<RolDTO> listar();
    RolDTO obtenerPorId(Long id);
    RolDTO crear(RolDTO dto);
    RolDTO actualizar(Long id, RolDTO dto);
    void eliminar(Long id);
    Rol buscarEntidad(Long id);
}
