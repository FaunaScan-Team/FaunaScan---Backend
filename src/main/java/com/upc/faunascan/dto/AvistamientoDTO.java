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
public class AvistamientoDTO {
    @Schema(example = "1")
    private Long idAvistamiento;
    @Schema(example = "2")
    private Long idUsuario;
    @Schema(example = "Ana Torres")
    private String nombreUsuario;
    @NotNull(message = "La especie es obligatoria")
    @Schema(example = "1")
    private Long idEspecie;
    @Schema(example = "Cóndor andino")
    private String nombreEspecie;
    @NotNull(message = "La ubicacion es obligatoria")
    @Schema(example = "1")
    private Long idUbicacion;
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(example = "2026-11-10T08:30:00")
    private LocalDateTime fechaAvistamiento;
    @Schema(example = "Pareja planeando sobre el cañón")
    private String observaciones;
    @Schema(example = "Cielo despejado, viento moderado")
    private String condicionesEntorno;
    @Schema(example = "pendiente")
    private String estadoValidacion;
    @Schema(example = "2026-11-11T10:00:00")
    private LocalDateTime fechaValidacion;
    @Schema(example = "3")
    private Long idInvestigadorValidador;
    @Schema(example = "false")
    private Boolean sincronizadoLocal;
    @Schema(example = "2026-11-10T09:00:00")
    private LocalDateTime fechaRegistro;
}
