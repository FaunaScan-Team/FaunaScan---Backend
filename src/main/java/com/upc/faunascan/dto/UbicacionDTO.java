package com.upc.faunascan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionDTO {
    @Schema(example = "1")
    private Long idUbicacion;
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
}
