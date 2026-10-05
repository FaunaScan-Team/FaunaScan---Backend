package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-09: notas de observacion y condiciones del entorno
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionesDTO {
    @Size(max = 2000)
    private String observaciones;
    @Size(max = 2000)
    private String condicionesEntorno;
}
