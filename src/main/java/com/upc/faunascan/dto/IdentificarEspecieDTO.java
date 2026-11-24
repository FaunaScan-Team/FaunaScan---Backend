package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-27: imagen a analizar (base64 o URL) y, opcional, la imagen guardada donde se registra el resultado
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdentificarEspecieDTO {
    @NotBlank(message = "La imagen es obligatoria")
    @Schema(example = "https://res.cloudinary.com/demo/foto-avistamiento.jpg")
    private String imagen;
    @Schema(example = "5")
    private Long idImagen;
}
