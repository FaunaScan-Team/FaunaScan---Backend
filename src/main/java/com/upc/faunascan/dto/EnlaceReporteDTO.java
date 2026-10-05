package com.upc.faunascan.dto;

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
    private Long idReporte;
    private String token;
    private String enlace;
}
