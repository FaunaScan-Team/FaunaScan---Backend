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
public class ImagenAvistamientoDTO {
    @Schema(example = "1")
    private Long idImagen;
    @Schema(example = "1")
    private Long idAvistamiento;
    @NotBlank(message = "La ruta de la imagen es obligatoria")
    @Size(max = 255)
    @Schema(example = "https://res.cloudinary.com/demo/condor-colca.jpg")
    private String rutaImagen;
    @Schema(example = "true")
    private Boolean esPrincipal;
    @Schema(example = "Vultur gryphus (0.91)")
    private String resultadoIa;
}
