package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.RolRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void registrar_DeberiaGuardarUsuarioConContrasenaEncriptada() {
        // Given
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(3L, "Ana", "Torres", "ana.torres@faunascan.pe", "Fauna2026!", null);
        Rol voluntario = new Rol(3L, "ROLE_VOLUNTARIO", "Voluntario");
        Usuario usuario = new Usuario();
        when(usuarioRepository.existsByCorreo("ana.torres@faunascan.pe")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(usuario);
        when(rolRepository.findById(3L)).thenReturn(Optional.of(voluntario));
        when(passwordEncoder.encode("Fauna2026!")).thenReturn("$2a$10$claveEncriptada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(new UsuarioDTO());

        // When
        UsuarioDTO resultado = usuarioService.registrar(dto);

        // Then
        assertThat(usuario.getContrasena()).isEqualTo("$2a$10$claveEncriptada");
        assertThat(usuario.getEstado()).isTrue();
        assertThat(resultado.getNombreRol()).isEqualTo("ROLE_VOLUNTARIO");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void registrar_DeberiaLanzarErrorSiElCorreoYaExiste() {
        // Given
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(3L, "Ana", "Torres", "ana.torres@faunascan.pe", "Fauna2026!", null);
        when(usuarioRepository.existsByCorreo("ana.torres@faunascan.pe")).thenReturn(true);

        // When
        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.registrar(dto));

        // Then
        assertThat(error.getMessage()).isEqualTo("Ya existe una cuenta registrada con ese correo");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrar_NoDeberiaPermitirCrearUnAdministrador() {
        // Given
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(1L, "Luis", "Rojas", "luis.rojas@faunascan.pe", "Fauna2026!", null);
        Rol admin = new Rol(1L, "ROLE_ADMIN", "Administrador");
        when(usuarioRepository.existsByCorreo("luis.rojas@faunascan.pe")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(new Usuario());
        when(rolRepository.findById(1L)).thenReturn(Optional.of(admin));

        // When
        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.registrar(dto));

        // Then
        assertThat(error.getMessage()).isEqualTo("No se puede registrar un usuario administrador");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void recuperarContrasena_DeberiaGenerarUnaClaveTemporal() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setCorreo("ana.torres@faunascan.pe");
        usuario.setContrasena("$2a$10$claveAnterior");
        when(usuarioRepository.findByCorreo("ana.torres@faunascan.pe")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$claveTemporal");

        // When
        String temporal = usuarioService.recuperarContrasena("ana.torres@faunascan.pe");

        // Then
        assertThat(temporal).hasSize(8);
        assertThat(usuario.getContrasena()).isEqualTo("$2a$10$claveTemporal");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void recuperarContrasena_DeberiaLanzarErrorSiElCorreoNoExiste() {
        // Given
        when(usuarioRepository.findByCorreo("noexiste@faunascan.pe")).thenReturn(Optional.empty());

        // When
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.recuperarContrasena("noexiste@faunascan.pe"));

        // Then
        assertThat(error.getMessage()).isEqualTo("No existe una cuenta con ese correo");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarCredencial_DeberiaGuardarLaUrlDelInvestigador() {
        // Given
        Usuario investigador = new Usuario();
        investigador.setIdUsuario(2L);
        investigador.setRol(new Rol(2L, "ROLE_INVESTIGADOR", "Investigador"));
        String url = "https://res.cloudinary.com/demo/credencial-upc.pdf";
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(investigador));
        when(usuarioRepository.save(investigador)).thenReturn(investigador);
        when(modelMapper.map(investigador, UsuarioDTO.class)).thenReturn(new UsuarioDTO());

        // When
        UsuarioDTO resultado = usuarioService.registrarCredencial(2L, url);

        // Then
        assertThat(investigador.getUrlCredencial()).isEqualTo(url);
        assertThat(resultado.getNombreRol()).isEqualTo("ROLE_INVESTIGADOR");
        verify(usuarioRepository, times(1)).save(investigador);
    }

    @Test
    void registrarCredencial_DeberiaRechazarAUnVoluntario() {
        // Given
        Usuario voluntario = new Usuario();
        voluntario.setIdUsuario(3L);
        voluntario.setRol(new Rol(3L, "ROLE_VOLUNTARIO", "Voluntario"));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(voluntario));

        // When
        RuntimeException error = assertThrows(RuntimeException.class,
                () -> usuarioService.registrarCredencial(3L, "https://res.cloudinary.com/demo/credencial.pdf"));

        // Then
        assertThat(error.getMessage()).isEqualTo("Solo los investigadores registran una credencial institucional");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
