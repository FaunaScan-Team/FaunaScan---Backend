package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-57: avistamientos de una especie en un mes
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TendenciaEspecieDTO {
    private String nombreComun;
    private Integer anio;
    private Integer mes;
    private Long totalAvistamientos;
}
