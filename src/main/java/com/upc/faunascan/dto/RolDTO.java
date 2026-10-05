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
public class RolDTO {
    private Long idRol;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;
    private String descripcion;
}
