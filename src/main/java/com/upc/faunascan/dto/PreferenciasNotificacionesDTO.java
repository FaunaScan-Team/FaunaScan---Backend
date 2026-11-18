package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-32: switches de notificaciones (null = activado)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreferenciasNotificacionesDTO {
    @Schema(example = "true")
    private Boolean criticas;
    @Schema(example = "true")
    private Boolean sistema;
    @Schema(example = "false")
    private Boolean alertas;
}
