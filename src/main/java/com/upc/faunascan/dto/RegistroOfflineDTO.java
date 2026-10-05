package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
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
    private BigDecimal latitud;
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitud;
    private String direccion;
    @NotNull(message = "La especie es obligatoria")
    private Long idEspecie;
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fechaAvistamiento;
    private String observaciones;
    private String condicionesEntorno;
    @Size(max = 255)
    private String rutaImagen;
}
