package com.upc.faunascan.dto;

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
    private Long idInvestigadorDestino;
    private String mensaje;
    private LocalDateTime fechaCompartido;
}
