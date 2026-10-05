package com.upc.faunascan.dto;

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
    private Long idAvistamiento;
    private String nombreComun;
    private String estadoConservacion;
    private BigDecimal latitud;
    private BigDecimal longitud;
}
