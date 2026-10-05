package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-27/HU-28/HU-30: especie sugerida por la IA con su confianza (0 a 1)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SugerenciaEspecieDTO {
    private Long idEspecie;
    private String nombreComun;
    private String nombreCientifico;
    private Double confianza;
}
