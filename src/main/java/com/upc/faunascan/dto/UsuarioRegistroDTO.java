package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Datos de entrada del registro (US01). La contrasena solo viaja en esta direccion.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    private Long idRol;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String urlCredencial;
}
