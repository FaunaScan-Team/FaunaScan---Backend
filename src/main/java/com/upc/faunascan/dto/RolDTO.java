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
public class RolDTO {
    @Schema(example = "2")
    private Long idRol;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    @Schema(example = "ROLE_INVESTIGADOR")
    private String nombre;
    @Schema(example = "Investigador de fauna: registra y valida avistamientos")
    private String descripcion;
}
