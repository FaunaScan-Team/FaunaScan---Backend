package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Entities.Perfil;
import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.PerfilRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.PerfilUsuarioDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PerfilServiceImpl perfilService;

    @Test
    void obtenerPerfil_DeberiaRetornarLosDatosDelUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2L);
        usuario.setNombre("Ana");
        usuario.setApellido("Torres");
        usuario.setCorreo("ana.torres@faunascan.pe");
        usuario.setRol(new Rol(3L, "ROLE_VOLUNTARIO", "Voluntario"));
        usuario.setPerfil(new Perfil(1L, usuario, "Voluntaria del Colca desde 2024", "Arequipa", LocalDateTime.now()));
        when(usuarioRepository.findByCorreo("ana.torres@faunascan.pe")).thenReturn(Optional.of(usuario));

        // When
        PerfilUsuarioDTO perfil = perfilService.obtenerPerfil("ana.torres@faunascan.pe");

        // Then
        assertThat(perfil.getNombre()).isEqualTo("Ana");
        assertThat(perfil.getNombreRol()).isEqualTo("ROLE_VOLUNTARIO");
        assertThat(perfil.getBiografia()).isEqualTo("Voluntaria del Colca desde 2024");
        assertThat(perfil.getUbicacion()).isEqualTo("Arequipa");
        verify(usuarioRepository, times(1)).findByCorreo("ana.torres@faunascan.pe");
    }

    @Test
    void obtenerPerfil_DeberiaLanzarErrorSiElCorreoNoExiste() {
        // Given
        when(usuarioRepository.findByCorreo("noexiste@faunascan.pe")).thenReturn(Optional.empty());

        // When
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,
                () -> perfilService.obtenerPerfil("noexiste@faunascan.pe"));

        // Then
        assertThat(error.getMessage()).isEqualTo("Usuario no encontrado con correo: noexiste@faunascan.pe");
    }

    @Test
    void actualizarPerfil_DeberiaCrearElPerfilSiElUsuarioNoTiene() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2L);
        usuario.setNombre("Ana");
        usuario.setApellido("Torres");
        usuario.setCorreo("ana.torres@faunascan.pe");
        PerfilUsuarioDTO cambios = new PerfilUsuarioDTO();
        cambios.setNombre("Ana María");
        cambios.setBiografia("Voluntaria del Colca desde 2024");
        cambios.setUbicacion("Arequipa");
        Perfil perfilGuardado = new Perfil(1L, null, "Voluntaria del Colca desde 2024", "Arequipa", LocalDateTime.now());
        when(usuarioRepository.findByCorreo("ana.torres@faunascan.pe")).thenReturn(Optional.of(usuario));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfilGuardado);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // When
        PerfilUsuarioDTO resultado = perfilService.actualizarPerfil("ana.torres@faunascan.pe", cambios);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Ana María");
        assertThat(resultado.getApellido()).isEqualTo("Torres");
        assertThat(resultado.getBiografia()).isEqualTo("Voluntaria del Colca desde 2024");
        assertThat(usuario.getPerfil()).isEqualTo(perfilGuardado);
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }
}
