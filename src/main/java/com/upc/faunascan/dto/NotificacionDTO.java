package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long idNotificacion;
    private Long idUsuario;
    private String tipo;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;
}
