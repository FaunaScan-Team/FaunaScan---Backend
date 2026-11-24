package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "1")
    private Long idAvistamiento;
    @Schema(example = "Ana")
    private String nombreUsuario;
    @Schema(example = "Torres")
    private String apellidoUsuario;
    @Schema(example = "Cóndor andino")
    private String nombreComun;
    @Schema(example = "Vulnerable")
    private String estadoConservacion;
    @Schema(example = "Cruz del Cóndor, Cañón del Colca, Arequipa")
    private String direccion;
    @Schema(example = "-15.6122")
    private BigDecimal latitud;
    @Schema(example = "-71.9060")
    private BigDecimal longitud;
    @Schema(example = "2026-11-10T08:30:00")
    private LocalDateTime fechaAvistamiento;
}
