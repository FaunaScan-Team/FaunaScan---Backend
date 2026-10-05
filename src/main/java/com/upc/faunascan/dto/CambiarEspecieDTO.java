package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-29: especie elegida manualmente en lugar de la sugerida por la IA
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CambiarEspecieDTO {
    @NotNull(message = "La especie es obligatoria")
    private Long idEspecie;
}
