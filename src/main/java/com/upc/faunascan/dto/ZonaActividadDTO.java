package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-57: zona con su cantidad de avistamientos
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ZonaActividadDTO {
    private String direccion;
    private Long totalAvistamientos;
}
