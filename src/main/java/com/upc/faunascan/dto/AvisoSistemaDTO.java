package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
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
    private String mensaje;
}
