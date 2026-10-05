package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-37: precision del modelo sobre los avistamientos ya registrados
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionIADTO {
    private Integer imagenesEvaluadas;
    private Integer aciertosTop1;
    private Integer aciertosTop3;
    private Double precisionTop1;
    private Double precisionTop3;
    private Double metaPrecision;
    private Boolean cumpleMeta;
}
