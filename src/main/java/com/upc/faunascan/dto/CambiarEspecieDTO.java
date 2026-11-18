package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-29: especie elegida manualmente en lugar de la sugerida por la IA
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CambiarEspecieDTO {
    @NotNull(message = "La especie es obligatoria")
    @Schema(example = "2")
    private Long idEspecie;
}
