package com.upc.faunascan.Services;

import com.upc.faunascan.dto.PerfilUsuarioDTO;

public interface PerfilService {
    PerfilUsuarioDTO obtenerPerfil(String correo);
    PerfilUsuarioDTO actualizarPerfil(String correo, PerfilUsuarioDTO dto);
}
