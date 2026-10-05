package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
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
    private Long idAvistamiento;
}
