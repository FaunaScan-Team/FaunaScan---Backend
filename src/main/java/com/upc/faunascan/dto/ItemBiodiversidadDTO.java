package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-18: fila del reporte de biodiversidad
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemBiodiversidadDTO {
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "Vulnerable")
    private String estadoConservacion;
    @Schema(example = "8")
    private Long totalRegistros;
}
