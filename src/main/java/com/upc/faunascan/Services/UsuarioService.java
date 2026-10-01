package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.RolRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::aDTO).toList();
    }

    public UsuarioDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // US01: crear cuenta
    public UsuarioDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe una cuenta registrada con ese correo");
        }
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + dto.getIdRol()));
        // el registro es publico, asi que no se permite crear cuentas admin desde aqui
        if (rol.getNombre().equals("ROLE_ADMIN")) {
            throw new RuntimeException("No se puede registrar un usuario administrador");
        }
        usuario.setRol(rol);
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstado(true);
        return aDTO(usuarioRepository.save(usuario));
    }

    // US03: recuperar contrasena (genera una temporal; el envio de correo se
    // conecta luego con un servicio de mail / Notificacion)
    public String recuperarContrasena(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        String temporal = UUID.randomUUID().toString().substring(0, 8);
        usuario.setContrasena(passwordEncoder.encode(temporal));
        usuarioRepository.save(usuario);
        return temporal;
    }

    // US04/US05: ver y editar perfil basico
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarEntidad(id);
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setUrlCredencial(dto.getUrlCredencial());
        usuario.setPreferenciasNotificaciones(dto.getPreferenciasNotificaciones());
        return aDTO(usuarioRepository.save(usuario));
    }

    // US08: cerrar sesion -> en un esquema stateless (JWT) el logout se maneja
    // del lado del cliente invalidando el token; este metodo queda como hook
    // por si se agrega una lista de tokens revocados mas adelante.

    // US39/estado: activar o desactivar cuenta
    public UsuarioDTO cambiarEstado(Long id, boolean estado) {
        Usuario usuario = buscarEntidad(id);
        usuario.setEstado(estado);
        return aDTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(buscarEntidad(id));
    }

    public Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    private UsuarioDTO aDTO(Usuario usuario) {
        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
        if (usuario.getRol() != null) {
            dto.setIdRol(usuario.getRol().getIdRol());
            dto.setNombreRol(usuario.getRol().getNombre());
        }
        if (usuario.getPerfil() != null) {
            dto.setIdPerfil(usuario.getPerfil().getIdPerfil());
        }
        return dto;
    }
}
