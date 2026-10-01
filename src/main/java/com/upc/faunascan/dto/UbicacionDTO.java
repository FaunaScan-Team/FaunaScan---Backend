package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionDTO {
    private Long idUbicacion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String direccion;
}
