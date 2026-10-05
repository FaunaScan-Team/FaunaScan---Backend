package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private Long idRol;
    private String nombreRol;
    private Long idPerfil;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;
    private String correo;
    private LocalDateTime fechaRegistro;
    private Boolean estado;
    private String urlCredencial;
    private String preferenciasNotificaciones;
}
