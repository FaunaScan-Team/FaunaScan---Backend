package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvistamientoCompartidoDTO {
    private Long idCompartido;
    private Long idAvistamiento;
    private Long idInvestigadorOrigen;
    @NotNull(message = "El investigador destino es obligatorio")
    private Long idInvestigadorDestino;
    @Size(max = 500)
    private String mensaje;
    private LocalDateTime fechaCompartido;
}
