package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-09: notas de observacion y condiciones del entorno
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionesDTO {
    @Size(max = 2000)
    @Schema(example = "Juvenil alimentándose cerca del mirador")
    private String observaciones;
    @Size(max = 2000)
    @Schema(example = "Neblina ligera, 8 °C")
    private String condicionesEntorno;
}
