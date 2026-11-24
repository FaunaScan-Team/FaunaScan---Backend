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
public class FamiliaDTO {
    @Schema(example = "1")
    private Long idFamilia;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Schema(example = "Cathartidae")
    private String nombre;
    @Schema(example = "Buitres del Nuevo Mundo")
    private String descripcion;
}
