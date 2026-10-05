package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-19: resumen de un mes
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumenMensualDTO {
    private Integer anio;
    private Integer mes;
    private Long totalAvistamientos;
    private Long totalEspecies;
}
