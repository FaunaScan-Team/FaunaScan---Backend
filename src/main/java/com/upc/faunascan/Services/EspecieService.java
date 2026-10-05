package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.dto.EspecieDTO;
import com.upc.faunascan.dto.GuiaIdentificacionDTO;
import java.util.List;

public interface EspecieService {
    List<EspecieDTO> listar();
    EspecieDTO obtenerPorId(Long id);
    List<EspecieDTO> buscar(String texto);
    List<EspecieDTO> listarPorFamilia(Long idFamilia);
    List<EspecieDTO> listarPorCategoria(Long idCategoria);
    EspecieDTO crear(EspecieDTO dto);
    EspecieDTO actualizar(Long id, EspecieDTO dto);
    void eliminar(Long id);
    Especie buscarEntidad(Long id);
    List<EspecieDTO> obtenerDetallesMultiples(List<Long> ids);
    GuiaIdentificacionDTO obtenerGuia(Long id);
    List<GuiaIdentificacionDTO> listarGuiasDestacadas();
}
