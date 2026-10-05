package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.dto.*;
import java.util.List;

public interface AvistamientoService {
    List<AvistamientoDTO> listar();
    AvistamientoDTO obtenerPorId(Long id);
    AvistamientoDTO registrar(AvistamientoDTO dto);
    List<AvistamientoDTO> listarPorUsuario(Long idUsuario);
    List<AvistamientoDTO> listarPorEspecie(Long idEspecie);
    List<AvistamientoDTO> listarPorUbicacion(Long idUbicacion);
    List<AvistamientoDTO> listarPendientesDeValidacion();
    AvistamientoDTO validar(Long idAvistamiento, Long idInvestigador, String nuevoEstado);
    AvistamientoDTO actualizar(Long id, AvistamientoDTO dto);
    void eliminar(Long id);
    Avistamiento buscarEntidad(Long id);
    AvistamientoDTO actualizarObservaciones(Long id, ObservacionesDTO dto);
    AvistamientoDTO cambiarEspecie(Long id, Long idEspecie);
    List<MapaAvistamientoDTO> listarParaMapa(String estadoConservacion);
    List<CoordenadaDTO> listarDensidad();
    List<AvistamientoComunidadDTO> listarRecientesDeComunidad(Long idUsuarioActual);
    List<AvistamientoDTO> filtrarHistorial(Long idUsuario, String especie, String zona, String estado);
    List<AvistamientoDTO> sincronizar(Long idUsuario, SincronizacionDTO dto);
    ProgresoDTO calcularProgreso(Long idUsuario);
    ContribucionDTO calcularContribucion(Long idUsuario);
}
