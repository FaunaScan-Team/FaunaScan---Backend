package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-57: zona con su cantidad de avistamientos
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ZonaActividadDTO {
    @Schema(example = "Ayacucho")
    private String direccion;
    @Schema(example = "18")
    private Long totalAvistamientos;
}
