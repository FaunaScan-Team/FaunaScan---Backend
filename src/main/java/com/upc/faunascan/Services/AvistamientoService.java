package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.EspecieRepository;
import com.upc.faunascan.Repositories.UbicacionRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.AvistamientoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoService {

    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecieRepository especieRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ModelMapper modelMapper;

    public List<AvistamientoDTO> listar() {
        return avistamientoRepository.findAll().stream().map(this::aDTO).toList();
    }

    public AvistamientoDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // US06/US09/US43: registrar un avistamiento con notas, condiciones del
    // entorno, etc. Queda "pendiente" hasta que un investigador lo valide.
    public AvistamientoDTO registrar(AvistamientoDTO dto) {
        Avistamiento avistamiento = modelMapper.map(dto, Avistamiento.class);
        avistamiento.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        avistamiento.setEspecie(especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada con id: " + dto.getIdEspecie())));
        avistamiento.setUbicacion(ubicacionRepository.findById(dto.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + dto.getIdUbicacion())));

        avistamiento.setFechaRegistro(LocalDateTime.now());
        if (avistamiento.getFechaAvistamiento() == null) {
            avistamiento.setFechaAvistamiento(LocalDateTime.now());
        }
        avistamiento.setEstadoValidacion("pendiente");
        avistamiento.setSincronizadoLocal(Boolean.TRUE.equals(dto.getSincronizadoLocal()));
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // US14: historial de avistamientos del usuario
    public List<AvistamientoDTO> listarPorUsuario(Long idUsuario) {
        return avistamientoRepository.findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    // US17/US25: filtrar por especie o ubicacion
    public List<AvistamientoDTO> listarPorEspecie(Long idEspecie) {
        return avistamientoRepository.findByEspecie_IdEspecie(idEspecie).stream().map(this::aDTO).toList();
    }

    public List<AvistamientoDTO> listarPorUbicacion(Long idUbicacion) {
        return avistamientoRepository.findByUbicacion_IdUbicacion(idUbicacion).stream().map(this::aDTO).toList();
    }

    // US45: cola de avistamientos pendientes de revision
    public List<AvistamientoDTO> listarPendientesDeValidacion() {
        return avistamientoRepository.findByEstadoValidacion("pendiente").stream().map(this::aDTO).toList();
    }

    // US45: un investigador aprueba o rechaza un avistamiento
    public AvistamientoDTO validar(Long idAvistamiento, Long idInvestigador, String nuevoEstado) {
        Avistamiento avistamiento = buscarEntidad(idAvistamiento);
        avistamiento.setEstadoValidacion(nuevoEstado);
        avistamiento.setInvestigadorValidador(usuarioRepository.findById(idInvestigador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idInvestigador)));
        avistamiento.setFechaValidacion(LocalDateTime.now());
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    public AvistamientoDTO actualizar(Long id, AvistamientoDTO dto) {
        Avistamiento avistamiento = buscarEntidad(id);
        avistamiento.setObservaciones(dto.getObservaciones());
        avistamiento.setCondicionesEntorno(dto.getCondicionesEntorno());
        avistamiento.setFechaAvistamiento(dto.getFechaAvistamiento());
        if (dto.getIdEspecie() != null) {
            avistamiento.setEspecie(especieRepository.findById(dto.getIdEspecie())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada con id: " + dto.getIdEspecie())));
        }
        if (dto.getIdUbicacion() != null) {
            avistamiento.setUbicacion(ubicacionRepository.findById(dto.getIdUbicacion())
                    .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + dto.getIdUbicacion())));
        }
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // US49: eliminar un avistamiento
    public void eliminar(Long id) {
        avistamientoRepository.delete(buscarEntidad(id));
    }

    public Avistamiento buscarEntidad(Long id) {
        return avistamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avistamiento no encontrado con id: " + id));
    }

    private AvistamientoDTO aDTO(Avistamiento avistamiento) {
        AvistamientoDTO dto = modelMapper.map(avistamiento, AvistamientoDTO.class);
        if (avistamiento.getUsuario() != null) {
            dto.setIdUsuario(avistamiento.getUsuario().getIdUsuario());
            dto.setNombreUsuario(avistamiento.getUsuario().getNombre() + " " + avistamiento.getUsuario().getApellido());
        }
        if (avistamiento.getEspecie() != null) {
            dto.setIdEspecie(avistamiento.getEspecie().getIdEspecie());
            dto.setNombreEspecie(avistamiento.getEspecie().getNombreComun());
        }
        if (avistamiento.getUbicacion() != null) {
            dto.setIdUbicacion(avistamiento.getUbicacion().getIdUbicacion());
        }
        if (avistamiento.getInvestigadorValidador() != null) {
            dto.setIdInvestigadorValidador(avistamiento.getInvestigadorValidador().getIdUsuario());
        }
        return dto;
    }
}
