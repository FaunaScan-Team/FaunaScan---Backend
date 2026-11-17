package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
// HU-37: precision del modelo sobre los avistamientos ya registrados
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionIADTO {
    @Schema(example = "100")
    private Integer imagenesEvaluadas;
    @Schema(example = "86")
    private Integer aciertosTop1;
    @Schema(example = "95")
    private Integer aciertosTop3;
    @Schema(example = "0.86")
    private Double precisionTop1;
    @Schema(example = "0.95")
    private Double precisionTop3;
    @Schema(example = "0.85")
    private Double metaPrecision;
    @Schema(example = "true")
    private Boolean cumpleMeta;
}
