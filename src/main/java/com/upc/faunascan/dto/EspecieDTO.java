package com.upc.faunascan.dto;

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
    private Long idFamilia;
    private String nombreFamilia;
    private Long idCategoria;
    private String nombreCategoria;
    private String nombreComun;
    private String nombreCientifico;
    private String imagenReferencia;
    private String pistasIdentificacion;
}
