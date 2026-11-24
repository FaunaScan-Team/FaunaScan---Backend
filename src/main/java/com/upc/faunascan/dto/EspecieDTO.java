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
public class EspecieDTO {
    @Schema(example = "1")
    private Long idEspecie;
    @NotNull(message = "La familia es obligatoria")
    @Schema(example = "1")
    private Long idFamilia;
    @Schema(example = "Cathartidae")
    private String nombreFamilia;
    @NotNull(message = "La categoria de conservacion es obligatoria")
    @Schema(example = "2")
    private Long idCategoria;
    @Schema(example = "Vulnerable")
    private String nombreCategoria;
    @NotBlank(message = "El nombre comun es obligatorio")
    @Size(max = 100)
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @NotBlank(message = "El nombre cientifico es obligatorio")
    @Size(max = 150)
    @Schema(example = "Vultur gryphus")
    private String nombreCientifico;
    @Schema(example = "https://res.cloudinary.com/demo/condor.jpg")
    private String imagenReferencia;
    @Schema(example = "Plumaje negro con collar blanco y alas muy anchas")
    private String pistasIdentificacion;
}
