package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

// HU-14/HU-17/HU-42: marcador del mapa
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapaAvistamientoDTO {
    @Schema(example = "1")
    private Long idAvistamiento;
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "Vulnerable")
    private String estadoConservacion;
    @Schema(example = "-15.6122")
    private BigDecimal latitud;
    @Schema(example = "-71.9060")
    private BigDecimal longitud;
}
