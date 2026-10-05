package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// HU-53: panel principal del voluntario
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardVoluntarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String rol;
    private ProgresoDTO progreso;
    private List<GuiaIdentificacionDTO> guias;
}
