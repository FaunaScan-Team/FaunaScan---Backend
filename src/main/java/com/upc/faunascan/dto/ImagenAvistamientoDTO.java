package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImagenAvistamientoDTO {
    private Long idImagen;
    private Long idAvistamiento;
    private String rutaImagen;
    private Boolean esPrincipal;
    private String resultadoIa;
}
