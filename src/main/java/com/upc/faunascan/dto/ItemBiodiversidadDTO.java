package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-18: fila del reporte de biodiversidad
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemBiodiversidadDTO {
    private String nombreComun;
    private String estadoConservacion;
    private Long totalRegistros;
}
