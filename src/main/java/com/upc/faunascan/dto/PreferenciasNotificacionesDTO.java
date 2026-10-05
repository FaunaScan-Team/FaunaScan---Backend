package com.upc.faunascan.dto;

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
    private Boolean criticas;
    private Boolean sistema;
    private Boolean alertas;
}
