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
public class AvistamientoFavoritoDTO {
    private Long idFavorito;
    private Long idUsuario;
    private Long idAvistamiento;
    private LocalDateTime fechaMarcado;
}
