package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cuerpo de la validacion de un avistamiento (US45). estado: \"validado\" o \"rechazado\".
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidarAvistamientoDTO {
    private Long idInvestigador;
    private String estado;
}
