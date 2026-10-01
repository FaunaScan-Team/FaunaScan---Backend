package com.upc.faunascan;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.dto.AvistamientoDTO;
import com.upc.faunascan.dto.UsuarioDTO;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MapeoDtoTest {

    private final ModelMapper modelMapper = crear();

    private ModelMapper crear() {
        ModelMapper m = new ModelMapper();
        m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return m;
    }

    @Test
    void usuarioNoExponeLaContrasena() {
        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombre("investigador");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(7L);
        usuario.setNombre("Cesar");
        usuario.setApellido("Mendoza");
        usuario.setCorreo("cesar@faunascan.test");
        usuario.setContrasena("secreta123");
        usuario.setRol(rol);
        usuario.setEstado(true);

        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);

        assertEquals(7L, dto.getIdUsuario());
        assertEquals("Cesar", dto.getNombre());
        assertEquals("cesar@faunascan.test", dto.getCorreo());
        // El DTO no tiene campo contrasena: se verifica que la clase no lo declare.
        assertThrows(NoSuchFieldException.class, () -> UsuarioDTO.class.getDeclaredField("contrasena"));
    }

    @Test
    void avistamientoNoMezclaLosDosUsuarios() {
        Usuario autor = new Usuario();
        autor.setIdUsuario(10L);
        Usuario validador = new Usuario();
        validador.setIdUsuario(99L);
        Especie especie = new Especie();
        especie.setIdEspecie(4L);

        Avistamiento avistamiento = new Avistamiento();
        avistamiento.setIdAvistamiento(55L);
        avistamiento.setUsuario(autor);
        avistamiento.setInvestigadorValidador(validador);
        avistamiento.setEspecie(especie);
        avistamiento.setEstadoValidacion("pendiente");
        avistamiento.setFechaAvistamiento(LocalDateTime.now());

        // Con la estrategia STRICT, ModelMapper copia solo los campos planos;
        // las relaciones las completa el servicio, sin ambiguedad entre
        // "usuario" e "investigadorValidador".
        AvistamientoDTO dto = modelMapper.map(avistamiento, AvistamientoDTO.class);

        assertEquals(55L, dto.getIdAvistamiento());
        assertEquals("pendiente", dto.getEstadoValidacion());
        assertNull(dto.getIdUsuario());
        assertNull(dto.getIdInvestigadorValidador());
    }

    @Test
    void dtoVuelveAEntidadSinRelaciones() {
        AvistamientoDTO dto = new AvistamientoDTO();
        dto.setIdUsuario(10L);
        dto.setIdEspecie(4L);
        dto.setObservaciones("Hembra con cria");
        dto.setSincronizadoLocal(true);

        Avistamiento avistamiento = modelMapper.map(dto, Avistamiento.class);

        assertEquals("Hembra con cria", avistamiento.getObservaciones());
        assertTrue(avistamiento.getSincronizadoLocal());
        assertNull(avistamiento.getUsuario());
        assertNull(avistamiento.getEspecie());
    }
}
