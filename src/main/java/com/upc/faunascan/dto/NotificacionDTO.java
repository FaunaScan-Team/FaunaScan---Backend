package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "15")
    private Long idNotificacion;
    @NotNull(message = "El usuario es obligatorio")
    @Schema(example = "4")
    private Long idUsuario;
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50)
    @Schema(example = "Alerta")
    private String tipo;
    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(example = "Tu avistamiento ha sido validado")
    private String mensaje;
    @Schema(example = "false")
    private Boolean leido;
    @Schema(example = "2026-11-17T10:30:00")
    private LocalDateTime fechaCreacion;
}
