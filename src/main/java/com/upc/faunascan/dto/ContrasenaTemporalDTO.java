package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta de la recuperacion de contrasena (US03).
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContrasenaTemporalDTO {
    private String contrasenaTemporal;
}
