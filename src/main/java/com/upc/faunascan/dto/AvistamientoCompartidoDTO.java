package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvistamientoCompartidoDTO {
    @Schema(example = "1")
    private Long idCompartido;
    @Schema(example = "1")
    private Long idAvistamiento;
    @Schema(example = "3")
    private Long idInvestigadorOrigen;
    @NotNull(message = "El investigador destino es obligatorio")
    @Schema(example = "4")
    private Long idInvestigadorDestino;
    @Size(max = 500)
    @Schema(example = "Revisa el plumaje juvenil en la foto 2")
    private String mensaje;
    @Schema(example = "2026-11-12T15:20:00")
    private LocalDateTime fechaCompartido;
}
