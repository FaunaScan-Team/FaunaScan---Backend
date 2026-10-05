package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

// HU-15: punto para el mapa de calor
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoordenadaDTO {
    private BigDecimal latitud;
    private BigDecimal longitud;
}
