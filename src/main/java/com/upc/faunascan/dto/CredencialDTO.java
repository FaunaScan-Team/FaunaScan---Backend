package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-31: credencial institucional del investigador
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CredencialDTO {
    @NotBlank(message = "La URL de la credencial es obligatoria")
    @Size(max = 255)
    @Schema(example = "https://res.cloudinary.com/demo/credencial-upc.pdf")
    private String urlCredencial;
}
