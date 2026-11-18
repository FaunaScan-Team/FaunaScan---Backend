package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecuperarContrasenaDTO {
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es valido")
    @Schema(example = "ana.torres@faunascan.pe")
    private String correo;
}
