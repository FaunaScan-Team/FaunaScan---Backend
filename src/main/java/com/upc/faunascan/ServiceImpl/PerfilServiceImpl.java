package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.PerfilService;
import com.upc.faunascan.Entities.Perfil;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.PerfilRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.PerfilUsuarioDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    // HU-04: ver perfil del usuario autenticado
    @Transactional(readOnly = true)
    @Override
    public PerfilUsuarioDTO obtenerPerfil(String correo) {
        return aDTO(buscarUsuario(correo));
    }

    // HU-05: editar perfil; si el usuario aun no tiene perfil se crea y se vincula
    @Transactional
    @Override
    public PerfilUsuarioDTO actualizarPerfil(String correo, PerfilUsuarioDTO dto) {
        Usuario usuario = buscarUsuario(correo);

        Perfil perfil = usuario.getPerfil();
        if (perfil == null) {
            perfil = new Perfil();
        }
        perfil.setBiografia(dto.getBiografia());
        perfil.setUbicacion(dto.getUbicacion());
        perfil.setFechaActualizacion(LocalDateTime.now());
        usuario.setPerfil(perfilRepository.save(perfil));

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        return aDTO(usuarioRepository.save(usuario));
    }

    private Usuario buscarUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo));
    }

    private PerfilUsuarioDTO aDTO(Usuario usuario) {
        PerfilUsuarioDTO dto = new PerfilUsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        if (usuario.getRol() != null) {
            dto.setNombreRol(usuario.getRol().getNombre());
        }
        Perfil perfil = usuario.getPerfil();
        if (perfil != null) {
            dto.setBiografia(perfil.getBiografia());
            dto.setUbicacion(perfil.getUbicacion());
            dto.setFechaActualizacion(perfil.getFechaActualizacion());
        }
        return dto;
    }
}
