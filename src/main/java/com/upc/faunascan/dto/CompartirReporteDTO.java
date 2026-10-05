package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-46: reporte a compartir
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompartirReporteDTO {
    @NotNull(message = "El reporte es obligatorio")
    private Long idReporte;
}
