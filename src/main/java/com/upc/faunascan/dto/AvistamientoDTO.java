package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


/**
 * Avistamiento plano: usuario, especie y ubicacion viajan como id en vez de como objetos
 * anidados, para no arrastrar todo el grafo de entidades en cada respuesta.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvistamientoDTO {
    private Long idAvistamiento;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idEspecie;
    private String nombreEspecie;
    private Long idUbicacion;
    private LocalDateTime fechaAvistamiento;
    private String observaciones;
    private String condicionesEntorno;
    private String estadoValidacion;
    private LocalDateTime fechaValidacion;
    private Long idInvestigadorValidador;
    private Boolean sincronizadoLocal;
    private LocalDateTime fechaRegistro;
}
