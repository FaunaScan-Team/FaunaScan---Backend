package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoService {

    private final AvistamientoRepository avistamientoRepository;

    public List<Avistamiento> listar() {
        return avistamientoRepository.findAll();
    }

    public Avistamiento obtenerPorId(Long id) {
        return avistamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avistamiento no encontrado con id: " + id));
    }

    // US06/US09/US43: registrar un avistamiento con notas, condiciones del
    // entorno, etc. Queda "pendiente" hasta que un investigador lo valide.
    public Avistamiento registrar(Avistamiento avistamiento) {
        avistamiento.setFechaRegistro(LocalDateTime.now());
        if (avistamiento.getFechaAvistamiento() == null) {
            avistamiento.setFechaAvistamiento(LocalDateTime.now());
        }
        avistamiento.setEstadoValidacion("pendiente");
        avistamiento.setSincronizadoLocal(Boolean.TRUE.equals(avistamiento.getSincronizadoLocal()));
        return avistamientoRepository.save(avistamiento);
    }

    // US14: historial de avistamientos del usuario
    public List<Avistamiento> listarPorUsuario(Long idUsuario) {
        return avistamientoRepository.findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(idUsuario);
    }

    // US17/US25: filtrar por especie o ubicacion
    public List<Avistamiento> listarPorEspecie(Long idEspecie) {
        return avistamientoRepository.findByEspecie_IdEspecie(idEspecie);
    }

    public List<Avistamiento> listarPorUbicacion(Long idUbicacion) {
        return avistamientoRepository.findByUbicacion_IdUbicacion(idUbicacion);
    }

    // US45: cola de avistamientos pendientes de revision
    public List<Avistamiento> listarPendientesDeValidacion() {
        return avistamientoRepository.findByEstadoValidacion("pendiente");
    }

    // US45: un investigador aprueba o rechaza un avistamiento
    public Avistamiento validar(Long idAvistamiento, Usuario investigador, String nuevoEstado) {
        Avistamiento avistamiento = obtenerPorId(idAvistamiento);
        avistamiento.setEstadoValidacion(nuevoEstado);
        avistamiento.setInvestigadorValidador(investigador);
        avistamiento.setFechaValidacion(LocalDateTime.now());
        return avistamientoRepository.save(avistamiento);
    }

    public Avistamiento actualizar(Long id, Avistamiento datos) {
        Avistamiento avistamiento = obtenerPorId(id);
        avistamiento.setObservaciones(datos.getObservaciones());
        avistamiento.setCondicionesEntorno(datos.getCondicionesEntorno());
        avistamiento.setFechaAvistamiento(datos.getFechaAvistamiento());
        avistamiento.setEspecie(datos.getEspecie());
        avistamiento.setUbicacion(datos.getUbicacion());
        return avistamientoRepository.save(avistamiento);
    }

    // US49: eliminar un avistamiento
    public void eliminar(Long id) {
        avistamientoRepository.delete(obtenerPorId(id));
    }
}
