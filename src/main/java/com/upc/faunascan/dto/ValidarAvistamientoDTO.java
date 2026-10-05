package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidarAvistamientoDTO {
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "validado|rechazado", message = "El estado debe ser validado o rechazado")
    private String estado;
}
