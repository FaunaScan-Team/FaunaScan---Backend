package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-58: guia visual de identificacion de una especie
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuiaIdentificacionDTO {
    @Schema(example = "1")
    private Long idEspecie;
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "Vultur gryphus")
    private String nombreCientifico;
    @Schema(example = "https://res.cloudinary.com/demo/condor.jpg")
    private String imagenReferencia;
    @Schema(example = "Plumaje negro con collar blanco y alas muy anchas")
    private String pistasIdentificacion;
    @Schema(example = "Cathartidae")
    private String familia;
    @Schema(example = "Vulnerable")
    private String estadoConservacion;
}
