package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

// HU-49: reporte guardado
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {
    @Schema(example = "5")
    private Long idReporte;
    @Schema(example = "Biodiversidad")
    private String tipo;
    @Schema(example = "Lima Metropolitana")
    private String area;
    @Schema(example = "2026-11-01")
    private LocalDate fechaInicio;
    @Schema(example = "2026-11-17")
    private LocalDate fechaFin;
    @Schema(example = "2026-11-17T15:00:00")
    private LocalDateTime fechaGeneracion;
    @Schema(example = "https://faunascan.example/reportes/abc123")
    private String enlaceCompartido;
}
