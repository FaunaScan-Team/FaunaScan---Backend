package com.upc.faunascan.security.dtos;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String jwt;
    private Long idUsuario;
    private String rol;
}
