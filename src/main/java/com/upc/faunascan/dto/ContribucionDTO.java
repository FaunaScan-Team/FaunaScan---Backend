package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-54: contribucion del usuario
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContribucionDTO {
    private Long totalAvistamientos;
    private Long avistamientosValidados;
    private Long avistamientosPendientes;
    private Long avistamientosRechazados;
}
