package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// HU-57: panel de tendencias y patrones
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TendenciasDTO {
    private List<TendenciaEspecieDTO> porEspecie;
    private List<ZonaActividadDTO> zonasMasActivas;
}
