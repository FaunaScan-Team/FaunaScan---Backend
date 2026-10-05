package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// HU-52: panel principal del investigador
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInvestigadorDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String rol;
    private Long pendientesValidacion;
    private Long validadosPorMi;
    private Long reportesGenerados;
}
