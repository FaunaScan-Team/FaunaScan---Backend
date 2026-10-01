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
public class PerfilDTO {
    private Long idPerfil;
    private Long idUsuario;
    private String biografia;
    private String ubicacion;
    private LocalDateTime fechaActualizacion;
}
