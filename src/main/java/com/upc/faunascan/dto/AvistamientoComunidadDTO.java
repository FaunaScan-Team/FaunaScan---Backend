package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// HU-21: avistamiento reciente de otro usuario
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvistamientoComunidadDTO {
    private Long idAvistamiento;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombreComun;
    private String estadoConservacion;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private LocalDateTime fechaAvistamiento;
}
