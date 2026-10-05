package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.RolRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.PreferenciasNotificacionesDTO;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::aDTO).toList();
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // HU-01: crear cuenta
    @Override
    public UsuarioDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe una cuenta registrada con ese correo");
        }
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + dto.getIdRol()));
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

    // HU-03: recuperar contrasena (genera una temporal; el envio de correo se
    // conecta luego con un servicio de mail / Notificacion)
    @Override
    public String recuperarContrasena(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cuenta con ese correo"));

        String temporal = UUID.randomUUID().toString().substring(0, 8);
        usuario.setContrasena(passwordEncoder.encode(temporal));
        usuarioRepository.save(usuario);
        return temporal;
    }

    // HU-04/HU-05: ver y editar perfil basico
    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarEntidad(id);
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setUrlCredencial(dto.getUrlCredencial());
        usuario.setPreferenciasNotificaciones(dto.getPreferenciasNotificaciones());
        return aDTO(usuarioRepository.save(usuario));
    }

    // HU-08: cerrar sesion -> en un esquema stateless (JWT) el logout se maneja
    // del lado del cliente invalidando el token; este metodo queda como hook
    // por si se agrega una lista de tokens revocados mas adelante.

    // activar o desactivar cuenta
    @Override
    public UsuarioDTO cambiarEstado(Long id, boolean estado) {
        Usuario usuario = buscarEntidad(id);
        usuario.setEstado(estado);
        return aDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.delete(buscarEntidad(id));
    }

    @Override
    public Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    // id del usuario autenticado (el correo viene del token JWT)
    @Override
    public Long obtenerIdPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo))
                .getIdUsuario();
    }

    // HU-31: el investigador registra la URL de su credencial institucional
    @Override
    public UsuarioDTO registrarCredencial(Long idUsuario, String urlCredencial) {
        Usuario usuario = buscarEntidad(idUsuario);
        if (usuario.getRol() == null || !"ROLE_INVESTIGADOR".equals(usuario.getRol().getNombre())) {
            throw new RuntimeException("Solo los investigadores registran una credencial institucional");
        }
        usuario.setUrlCredencial(urlCredencial);
        return aDTO(usuarioRepository.save(usuario));
    }

    // HU-32: consultar los switches de notificaciones (sin configurar = todo activado)
    @Override
    public PreferenciasNotificacionesDTO obtenerPreferencias(Long idUsuario) {
        String json = buscarEntidad(idUsuario).getPreferenciasNotificaciones();
        PreferenciasNotificacionesDTO dto = (json == null || json.isBlank())
                ? new PreferenciasNotificacionesDTO()
                : objectMapper.readValue(json, PreferenciasNotificacionesDTO.class);
        return completarPreferencias(dto);
    }

    // HU-32: guardar los switches como JSON en usuarios.preferencias_notificaciones
    @Override
    public PreferenciasNotificacionesDTO actualizarPreferencias(Long idUsuario, PreferenciasNotificacionesDTO dto) {
        Usuario usuario = buscarEntidad(idUsuario);
        PreferenciasNotificacionesDTO preferencias = completarPreferencias(dto);
        usuario.setPreferenciasNotificaciones(objectMapper.writeValueAsString(preferencias));
        usuarioRepository.save(usuario);
        return preferencias;
    }

    private PreferenciasNotificacionesDTO completarPreferencias(PreferenciasNotificacionesDTO dto) {
        return new PreferenciasNotificacionesDTO(
                dto.getCriticas() == null || dto.getCriticas(),
                dto.getSistema() == null || dto.getSistema(),
                dto.getAlertas() == null || dto.getAlertas());
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
