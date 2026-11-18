package com.upc.faunascan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-34/HU-40/HU-59: resultado de un envio masivo de notificaciones
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnvioNotificacionesDTO {
    @Schema(example = "Crítica")
    private String tipo;
    @Schema(example = "7")
    private Integer notificacionesEnviadas;
}
