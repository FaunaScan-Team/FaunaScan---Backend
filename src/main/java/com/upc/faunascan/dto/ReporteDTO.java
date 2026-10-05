package com.upc.faunascan.dto;

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
    private Long idReporte;
    private String tipo;
    private String area;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime fechaGeneracion;
    private String enlaceCompartido;
}
