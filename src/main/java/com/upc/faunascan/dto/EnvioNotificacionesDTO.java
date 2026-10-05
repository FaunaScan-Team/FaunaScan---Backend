package com.upc.faunascan.dto;

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
    private String tipo;
    private Integer notificacionesEnviadas;
}
