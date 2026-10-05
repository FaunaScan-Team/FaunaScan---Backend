package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

// HU-04/HU-05: datos de usuarios + perfiles del usuario autenticado
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilUsuarioDTO {
    private Long idUsuario;
    @Size(max = 100)
    private String nombre;
    @Size(max = 100)
    private String apellido;
    private String correo;
    private String nombreRol;
    private String biografia;
    private String ubicacion;
    private LocalDateTime fechaActualizacion;
}
