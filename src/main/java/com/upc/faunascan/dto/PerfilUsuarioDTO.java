package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-04/HU-05: datos de usuarios + perfiles del usuario autenticado
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilUsuarioDTO {
    @Schema(example = "2")
    private Long idUsuario;
    @Size(max = 100)
    @Schema(example = "Ana")
    private String nombre;
    @Size(max = 100)
    @Schema(example = "Torres")
    private String apellido;
    @Schema(example = "ana.torres@faunascan.pe")
    private String correo;
    @Schema(example = "ROLE_VOLUNTARIO")
    private String nombreRol;
    @Schema(example = "Voluntaria del Colca desde 2024")
    private String biografia;
    @Schema(example = "Arequipa")
    private String ubicacion;
    @Schema(example = "2026-11-01T18:00:00")
    private LocalDateTime fechaActualizacion;
}
