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
public class CategoriaConservacionDTO {
    @Schema(example = "2")
    private Long idCategoria;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Schema(example = "Vulnerable")
    private String nombre;
    @Schema(example = "Riesgo alto de extinción en estado silvestre")
    private String descripcion;
}
