package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-34: avistamiento que dispara la alerta critica
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionCriticaDTO {
    @NotNull(message = "El avistamiento es obligatorio")
    @Schema(example = "24")
    private Long idAvistamiento;
}
