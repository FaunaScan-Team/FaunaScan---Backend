package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// HU-27/HU-28/HU-38/HU-51: respuesta del modelo de IA
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoIdentificacionDTO {
    private String estado;
    private Double confianzaMaxima;
    private Double umbralConfianza;
    private SugerenciaEspecieDTO especieSugerida;
    private List<SugerenciaEspecieDTO> sugerencias;
    private String mensaje;
    private String modelo;
    private Long tiempoProcesamientoMs;
    private Boolean dentroDelTiempoMaximo;
    private Long idImagen;
}
