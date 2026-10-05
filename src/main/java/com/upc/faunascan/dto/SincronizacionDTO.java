package com.upc.faunascan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// HU-36: lote de registros offline a sincronizar
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SincronizacionDTO {
    @NotEmpty(message = "Debe enviar al menos un registro")
    @Valid
    private List<RegistroOfflineDTO> registros;
}
