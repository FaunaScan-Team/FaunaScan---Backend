package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.AvistamientoService;
import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.EspecieRepository;
import com.upc.faunascan.Repositories.UbicacionRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import com.upc.faunascan.Services.NotificacionService;
import com.upc.faunascan.dto.*;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoServiceImpl implements AvistamientoService {
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecieRepository especieRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ImagenAvistamientoRepository imagenAvistamientoRepository;
    private final NotificacionService notificacionService;
    private final ModelMapper modelMapper;

    @Override
    public List<AvistamientoDTO> listar() {
        return avistamientoRepository.findAll().stream().map(this::aDTO).toList();
    }

    @Override
    public AvistamientoDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // HU-06/HU-09/HU-36: registrar un avistamiento con notas, condiciones del
    // entorno, etc. Queda "pendiente" hasta que un investigador lo valide.
    @Override
    public AvistamientoDTO registrar(AvistamientoDTO dto) {
        Avistamiento avistamiento = modelMapper.map(dto, Avistamiento.class);
        avistamiento.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        avistamiento.setEspecie(especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada con id: " + dto.getIdEspecie())));
        avistamiento.setUbicacion(ubicacionRepository.findById(dto.getIdUbicacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Ubicacion no encontrada con id: " + dto.getIdUbicacion())));

        avistamiento.setFechaRegistro(LocalDateTime.now());
        if (avistamiento.getFechaAvistamiento() == null) {
            avistamiento.setFechaAvistamiento(LocalDateTime.now());
        }
        avistamiento.setEstadoValidacion("pendiente");
        avistamiento.setSincronizadoLocal(Boolean.TRUE.equals(dto.getSincronizadoLocal()));
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // HU-24: historial de avistamientos del usuario
    @Override
    public List<AvistamientoDTO> listarPorUsuario(Long idUsuario) {
        return avistamientoRepository.findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    // HU-17/HU-25: filtrar por especie o ubicacion
    @Override
    public List<AvistamientoDTO> listarPorEspecie(Long idEspecie) {
        return avistamientoRepository.findByEspecie_IdEspecie(idEspecie).stream().map(this::aDTO).toList();
    }

    @Override
    public List<AvistamientoDTO> listarPorUbicacion(Long idUbicacion) {
        return avistamientoRepository.findByUbicacion_IdUbicacion(idUbicacion).stream().map(this::aDTO).toList();
    }

    // HU-50: cola de avistamientos pendientes de revision
    @Override
    public List<AvistamientoDTO> listarPendientesDeValidacion() {
        return avistamientoRepository.findByEstadoValidacion("pendiente").stream().map(this::aDTO).toList();
    }

    // HU-44: validar o rechazar un avistamiento
    // HU-13/HU-34: al validar se avisa al autor y, si la especie es vulnerable, a todos
    @Override
    @Transactional
    public AvistamientoDTO validar(Long idAvistamiento, Long idInvestigador, String nuevoEstado) {
        Avistamiento avistamiento = buscarEntidad(idAvistamiento);
        avistamiento.setEstadoValidacion(nuevoEstado);
        avistamiento.setInvestigadorValidador(usuarioRepository.findById(idInvestigador)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + idInvestigador)));
        avistamiento.setFechaValidacion(LocalDateTime.now());
        AvistamientoDTO resultado = aDTO(avistamientoRepository.save(avistamiento));
        notificacionService.notificarValidacion(idAvistamiento);
        return resultado;
    }

    @Override
    public AvistamientoDTO actualizar(Long id, AvistamientoDTO dto) {
        Avistamiento avistamiento = buscarEntidad(id);
        avistamiento.setObservaciones(dto.getObservaciones());
        avistamiento.setCondicionesEntorno(dto.getCondicionesEntorno());
        avistamiento.setFechaAvistamiento(dto.getFechaAvistamiento());
        if (dto.getIdEspecie() != null) {
            avistamiento.setEspecie(especieRepository.findById(dto.getIdEspecie())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada con id: " + dto.getIdEspecie())));
        }
        if (dto.getIdUbicacion() != null) {
            avistamiento.setUbicacion(ubicacionRepository.findById(dto.getIdUbicacion())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Ubicacion no encontrada con id: " + dto.getIdUbicacion())));
        }
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // HU-47: eliminar un avistamiento
    @Override
    public void eliminar(Long id) {
        avistamientoRepository.delete(buscarEntidad(id));
    }

    @Override
    public Avistamiento buscarEntidad(Long id) {
        return avistamientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Avistamiento no encontrado con id: " + id));
    }

    // HU-09/HU-55: notas de observacion y condiciones del entorno
    @Override
    public AvistamientoDTO actualizarObservaciones(Long id, ObservacionesDTO dto) {
        Avistamiento avistamiento = buscarEntidad(id);
        avistamiento.setObservaciones(dto.getObservaciones());
        avistamiento.setCondicionesEntorno(dto.getCondicionesEntorno());
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // HU-29: reemplazar la especie sugerida por la IA por una elegida del catalogo
    @Override
    public AvistamientoDTO cambiarEspecie(Long id, Long idEspecie) {
        Avistamiento avistamiento = buscarEntidad(id);
        avistamiento.setEspecie(especieRepository.findById(idEspecie)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada con id: " + idEspecie)));
        return aDTO(avistamientoRepository.save(avistamiento));
    }

    // HU-14/HU-17/HU-42: marcadores del mapa, opcionalmente por categoria de conservacion
    @Override
    public List<MapaAvistamientoDTO> listarParaMapa(String estadoConservacion) {
        if (estadoConservacion == null || estadoConservacion.isBlank()) {
            return avistamientoRepository.listarParaMapa();
        }
        return avistamientoRepository.listarParaMapaPorEstado(estadoConservacion.trim());
    }

    // HU-15: coordenadas para la capa de mapa de calor
    @Override
    public List<CoordenadaDTO> listarDensidad() {
        return avistamientoRepository.listarCoordenadasValidadas();
    }

    // HU-21: ultimos 10 avistamientos validados de otros usuarios
    @Override
    public List<AvistamientoComunidadDTO> listarRecientesDeComunidad(Long idUsuarioActual) {
        return avistamientoRepository.listarRecientesDeComunidad(idUsuarioActual, PageRequest.of(0, 10));
    }

    // HU-25: filtros opcionales del historial (especie, zona y estado de validacion)
    @Override
    public List<AvistamientoDTO> filtrarHistorial(Long idUsuario, String especie, String zona, String estado) {
        return avistamientoRepository.filtrarHistorial(idUsuario, limpiar(especie), limpiar(zona), limpiar(estado).toLowerCase())
                .stream().map(this::aDTO).toList();
    }

    // HU-36: crea ubicacion, avistamiento e imagen por cada registro hecho sin conexion
    @Override
    @Transactional
    public List<AvistamientoDTO> sincronizar(Long idUsuario, SincronizacionDTO dto) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + idUsuario));
        List<AvistamientoDTO> sincronizados = new ArrayList<>();
        for (RegistroOfflineDTO registro : dto.getRegistros()) {
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setLatitud(registro.getLatitud());
            ubicacion.setLongitud(registro.getLongitud());
            ubicacion.setDireccion(registro.getDireccion());

            Avistamiento avistamiento = new Avistamiento();
            avistamiento.setUsuario(usuario);
            avistamiento.setEspecie(especieRepository.findById(registro.getIdEspecie())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada con id: " + registro.getIdEspecie())));
            avistamiento.setUbicacion(ubicacionRepository.save(ubicacion));
            avistamiento.setFechaAvistamiento(registro.getFechaAvistamiento() != null
                    ? registro.getFechaAvistamiento() : LocalDateTime.now());
            avistamiento.setObservaciones(registro.getObservaciones());
            avistamiento.setCondicionesEntorno(registro.getCondicionesEntorno());
            avistamiento.setEstadoValidacion("pendiente");
            avistamiento.setSincronizadoLocal(true);
            avistamiento.setFechaRegistro(LocalDateTime.now());
            avistamiento = avistamientoRepository.save(avistamiento);

            if (registro.getRutaImagen() != null && !registro.getRutaImagen().isBlank()) {
                ImagenAvistamiento imagen = new ImagenAvistamiento();
                imagen.setAvistamiento(avistamiento);
                imagen.setRutaImagen(registro.getRutaImagen());
                imagen.setEsPrincipal(true);
                imagenAvistamientoRepository.save(imagen);
            }
            sincronizados.add(aDTO(avistamiento));
        }
        return sincronizados;
    }

    // HU-23: avistamientos, especies unicas y zonas unicas del usuario
    @Override
    public ProgresoDTO calcularProgreso(Long idUsuario) {
        return avistamientoRepository.calcularProgreso(idUsuario);
    }

    // HU-54: avistamientos del usuario por estado de validacion
    @Override
    public ContribucionDTO calcularContribucion(Long idUsuario) {
        return avistamientoRepository.calcularContribucion(idUsuario);
    }

    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
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
