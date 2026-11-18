package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    @Schema(example = "2")
    private Long idUsuario;
    @Schema(example = "3")
    private Long idRol;
    @Schema(example = "ROLE_VOLUNTARIO")
    private String nombreRol;
    @Schema(example = "2")
    private Long idPerfil;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Schema(example = "Ana")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    @Schema(example = "Torres")
    private String apellido;
    @Schema(example = "ana.torres@faunascan.pe")
    private String correo;
    @Schema(example = "2026-09-15T10:00:00")
    private LocalDateTime fechaRegistro;
    @Schema(example = "true")
    private Boolean estado;
    @Schema(example = "https://res.cloudinary.com/demo/credencial-upc.pdf")
    private String urlCredencial;
    @Schema(example = "{\"validaciones\":true,\"recordatorios\":false}")
    private String preferenciasNotificaciones;
}
