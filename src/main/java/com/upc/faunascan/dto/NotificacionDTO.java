package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
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
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50)
    private String tipo;
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;
}
