package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-23: resumen de progreso del usuario
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoDTO {
    private Long totalAvistamientos;
    private Long especiesRegistradas;
    private Long zonasVisitadas;
}
