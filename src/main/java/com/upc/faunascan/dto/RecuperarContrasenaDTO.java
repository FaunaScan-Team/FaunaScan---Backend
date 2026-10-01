package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Solicitud de recuperacion de contrasena (US03).
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecuperarContrasenaDTO {
    private String correo;
}
