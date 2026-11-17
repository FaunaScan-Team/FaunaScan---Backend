package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
// HU-27/HU-28/HU-30: especie sugerida por la IA con su confianza (0 a 1)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SugerenciaEspecieDTO {
    @Schema(example = "1")
    private Long idEspecie;
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "Vultur gryphus")
    private String nombreCientifico;
    @Schema(example = "0.91")
    private Double confianza;
}
