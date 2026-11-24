package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// HU-36: avistamiento guardado sin conexion en el dispositivo
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroOfflineDTO {
    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Schema(example = "-15.6122")
    private BigDecimal latitud;
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Schema(example = "-71.9060")
    private BigDecimal longitud;
    @Schema(example = "Cruz del Cóndor, Cañón del Colca, Arequipa")
    private String direccion;
    @NotNull(message = "La especie es obligatoria")
    @Schema(example = "1")
    private Long idEspecie;
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(example = "2026-11-10T08:30:00")
    private LocalDateTime fechaAvistamiento;
    @Schema(example = "Pareja planeando sobre el cañón")
    private String observaciones;
    @Schema(example = "Cielo despejado, viento moderado")
    private String condicionesEntorno;
    @Size(max = 255)
    @Schema(example = "https://res.cloudinary.com/demo/condor-colca.jpg")
    private String rutaImagen;
}
