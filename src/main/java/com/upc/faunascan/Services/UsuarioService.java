package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.dto.PreferenciasNotificacionesDTO;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listar();
    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO registrar(UsuarioRegistroDTO dto);
    String recuperarContrasena(String correo);
    UsuarioDTO actualizar(Long id, UsuarioDTO dto);
    UsuarioDTO cambiarEstado(Long id, boolean estado);
    void eliminar(Long id);
    Usuario buscarEntidad(Long id);
    Long obtenerIdPorCorreo(String correo);
    UsuarioDTO registrarCredencial(Long idUsuario, String urlCredencial);
    PreferenciasNotificacionesDTO obtenerPreferencias(Long idUsuario);
    PreferenciasNotificacionesDTO actualizarPreferencias(Long idUsuario, PreferenciasNotificacionesDTO dto);
}
