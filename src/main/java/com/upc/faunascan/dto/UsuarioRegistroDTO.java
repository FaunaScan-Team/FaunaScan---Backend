package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    @NotNull(message = "Selecciona el tipo de usuario")
    @Schema(example = "3")
    private Long idRol;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Schema(example = "Ana")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    @Schema(example = "Torres")
    private String apellido;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es valido")
    @Size(max = 150)
    @Schema(example = "ana.torres@faunascan.pe")
    private String correo;
    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    @Schema(example = "Fauna2026!")
    private String contrasena;
    @Schema(example = "https://res.cloudinary.com/demo/credencial-upc.pdf")
    private String urlCredencial;
}
