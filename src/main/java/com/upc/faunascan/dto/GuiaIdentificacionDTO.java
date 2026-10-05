package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-58: guia visual de identificacion de una especie
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuiaIdentificacionDTO {
    private Long idEspecie;
    private String nombreComun;
    private String nombreCientifico;
    private String imagenReferencia;
    private String pistasIdentificacion;
    private String familia;
    private String estadoConservacion;
}
