package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // US01: crear cuenta
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("Ya existe una cuenta registrada con ese correo");
        }
        // NOTA: en produccion la contrasena debe guardarse con hash (BCrypt),
        // aqui se guarda tal cual para efectos del alcance del curso.
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstado(true);
        return usuarioRepository.save(usuario);
    }

    // US02: iniciar sesion
    public Usuario iniciarSesion(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo o contrasena incorrectos"));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Correo o contrasena incorrectos");
        }
        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new RuntimeException("La cuenta se encuentra deshabilitada");
        }
        return usuario;
    }

    // US03: recuperar contrasena (genera una temporal; el envio de correo se
    // conecta luego con un servicio de mail / Notificacion)
    public String recuperarContrasena(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        String temporal = java.util.UUID.randomUUID().toString().substring(0, 8);
        usuario.setContrasena(temporal);
        usuarioRepository.save(usuario);
        return temporal;
    }

    // US04/US05: ver y editar perfil basico
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = obtenerPorId(id);
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setUrlCredencial(datos.getUrlCredencial());
        usuario.setPreferenciasNotificaciones(datos.getPreferenciasNotificaciones());
        return usuarioRepository.save(usuario);
    }

    // US08: cerrar sesion -> en un esquema stateless (JWT) el logout se maneja
    // del lado del cliente invalidando el token; este metodo queda como hook
    // por si se agrega una lista de tokens revocados mas adelante.

    // US39/estado: activar o desactivar cuenta
    public Usuario cambiarEstado(Long id, boolean estado) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(obtenerPorId(id));
    }
}
