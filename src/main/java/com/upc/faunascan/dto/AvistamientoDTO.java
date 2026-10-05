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
public class AvistamientoDTO {
    private Long idAvistamiento;
    private Long idUsuario;
    private String nombreUsuario;
    @NotNull(message = "La especie es obligatoria")
    private Long idEspecie;
    private String nombreEspecie;
    @NotNull(message = "La ubicacion es obligatoria")
    private Long idUbicacion;
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fechaAvistamiento;
    private String observaciones;
    private String condicionesEntorno;
    private String estadoValidacion;
    private LocalDateTime fechaValidacion;
    private Long idInvestigadorValidador;
    private Boolean sincronizadoLocal;
    private LocalDateTime fechaRegistro;
}
