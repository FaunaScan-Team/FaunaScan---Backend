package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "2026")
    private Integer anio;
    @Schema(example = "11")
    private Integer mes;
    @Schema(example = "8")
    private Long totalAvistamientos;
}
