package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-19: resumen de un mes
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumenMensualDTO {
    @Schema(example = "2026")
    private Integer anio;
    @Schema(example = "11")
    private Integer mes;
    @Schema(example = "42")
    private Long totalAvistamientos;
    @Schema(example = "11")
    private Long totalEspecies;
}
