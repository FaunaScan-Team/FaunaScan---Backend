package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-46: enlace generado para compartir un reporte
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnlaceReporteDTO {
    @Schema(example = "5")
    private Long idReporte;
    @Schema(example = "abc123")
    private String token;
    @Schema(example = "https://faunascan.example/reportes/abc123")
    private String enlace;
}
