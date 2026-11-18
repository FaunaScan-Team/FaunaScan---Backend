package com.upc.faunascan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "125")
    private Long totalRegistros;
    @Schema(example = "19")
    private Long totalEspecies;
}
