package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    @NotNull(message = "Selecciona el tipo de usuario")
    private Long idRol;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es valido")
    @Size(max = 150)
    private String correo;
    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String contrasena;
    private String urlCredencial;
}
