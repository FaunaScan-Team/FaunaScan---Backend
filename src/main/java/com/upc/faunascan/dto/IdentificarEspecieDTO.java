package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-27: imagen a analizar (base64 o URL) y, opcional, la imagen guardada donde se registra el resultado
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdentificarEspecieDTO {
    @NotBlank(message = "La imagen es obligatoria")
    private String imagen;
    private Long idImagen;
}
