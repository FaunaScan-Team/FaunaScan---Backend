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
public class EspecieDTO {
    private Long idEspecie;
    @NotNull(message = "La familia es obligatoria")
    private Long idFamilia;
    private String nombreFamilia;
    @NotNull(message = "La categoria de conservacion es obligatoria")
    private Long idCategoria;
    private String nombreCategoria;
    @NotBlank(message = "El nombre comun es obligatorio")
    @Size(max = 100)
    private String nombreComun;
    @NotBlank(message = "El nombre cientifico es obligatorio")
    @Size(max = 150)
    private String nombreCientifico;
    private String imagenReferencia;
    private String pistasIdentificacion;
}
