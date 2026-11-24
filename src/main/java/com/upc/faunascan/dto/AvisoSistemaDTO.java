package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-12/HU-40: aviso de sistema o mantenimiento para todos los usuarios
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvisoSistemaDTO {
    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(example = "Mantenimiento programado el domingo a las 22:00")
    private String mensaje;
}
