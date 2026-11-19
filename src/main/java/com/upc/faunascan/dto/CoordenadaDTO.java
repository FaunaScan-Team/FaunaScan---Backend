package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "-15.6122")
    private BigDecimal latitud;
    @Schema(example = "-71.9060")
    private BigDecimal longitud;
}
