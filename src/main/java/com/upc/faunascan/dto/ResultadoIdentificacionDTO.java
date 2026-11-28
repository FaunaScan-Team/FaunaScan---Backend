package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

// HU-27/HU-28/HU-38/HU-51: respuesta del modelo de IA
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoIdentificacionDTO {
    @Schema(example = "identificada")
    private String estado;
    @Schema(example = "0.91")
    private Double confianzaMaxima;
    @Schema(example = "0.70")
    private Double umbralConfianza;
    private SugerenciaEspecieDTO especieSugerida;
    private List<SugerenciaEspecieDTO> sugerencias;
    @Schema(example = "Especie identificada con alta confianza")
    private String mensaje;
    @Schema(example = "gemini-3.8-flash")
    private String modelo;
    @Schema(example = "850")
    private Long tiempoProcesamientoMs;
    @Schema(example = "true")
    private Boolean dentroDelTiempoMaximo;
    @Schema(example = "5")
    private Long idImagen;
}
