package com.upc.faunascan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// HU-18/HU-19/HU-46: datos de un reporte (items si es de biodiversidad, meses si es mensual)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDetalleDTO {
    private ReporteDTO reporte;
    private List<ItemBiodiversidadDTO> items;
    private List<ResumenMensualDTO> meses;
    private Long totalRegistros;
    private Long totalEspecies;
}
