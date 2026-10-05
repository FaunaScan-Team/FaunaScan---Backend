package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImagenAvistamientoDTO {
    private Long idImagen;
    private Long idAvistamiento;
    @NotBlank(message = "La ruta de la imagen es obligatoria")
    @Size(max = 255)
    private String rutaImagen;
    private Boolean esPrincipal;
    private String resultadoIa;
}
