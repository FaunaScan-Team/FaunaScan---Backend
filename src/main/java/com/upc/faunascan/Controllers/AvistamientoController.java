package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoService;
import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvistamientoController {
    private final AvistamientoService avistamientoService;
    private final UsuarioService usuarioService;

    @GetMapping("/avistamientos")
    public List<AvistamientoDTO> listar() {
        return avistamientoService.listar();
    }

    @GetMapping("/avistamientos/{id}")
    public AvistamientoDTO obtener(@PathVariable Long id) {
        return avistamientoService.obtenerPorId(id);
    }

    // HU-06: registrar avistamiento; el autor es el usuario autenticado
    @PostMapping("/avistamientos")
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoDTO registrar(@Valid @RequestBody AvistamientoDTO avistamientoDTO,
                                     Authentication authentication) {
        avistamientoDTO.setIdUsuario(usuarioService.obtenerIdPorCorreo(authentication.getName()));
        return avistamientoService.registrar(avistamientoDTO);
    }

    // HU-24: historial de avistamientos del usuario
    @GetMapping("/usuarios/{idUsuario}/avistamientos/historial")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<AvistamientoDTO> historial(@PathVariable Long idUsuario) {
        return avistamientoService.listarPorUsuario(idUsuario);
    }

    // HU-25: historial con filtros opcionales ?especie=&zona=&estado=
    @GetMapping("/usuarios/{idUsuario}/avistamientos/historial/filtros")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<AvistamientoDTO> historialFiltrado(@PathVariable Long idUsuario,
                                                   @RequestParam(required = false) String especie,
                                                   @RequestParam(required = false) String zona,
                                                   @RequestParam(required = false) String estado) {
        return avistamientoService.filtrarHistorial(idUsuario, especie, zona, estado);
    }

    // HU-14/HU-42: marcadores del mapa; HU-17: ?estado= filtra por categoria de conservacion
    @GetMapping("/avistamientos/mapa")
    public List<MapaAvistamientoDTO> mapa(@RequestParam(required = false) String estado) {
        return avistamientoService.listarParaMapa(estado);
    }

    // HU-15: coordenadas para el mapa de calor
    @GetMapping("/avistamientos/densidad")
    public List<CoordenadaDTO> densidad() {
        return avistamientoService.listarDensidad();
    }

    // HU-21: avistamientos recientes de otros usuarios
    @GetMapping("/avistamientos/comunidad/recientes")
    public List<AvistamientoComunidadDTO> recientesDeComunidad(Authentication authentication) {
        return avistamientoService.listarRecientesDeComunidad(
                usuarioService.obtenerIdPorCorreo(authentication.getName()));
    }

    // HU-36: sincronizar registros hechos sin conexion
    @PostMapping("/avistamientos/sincronizar")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AvistamientoDTO> sincronizar(@Valid @RequestBody SincronizacionDTO sincronizacionDTO,
                                             Authentication authentication) {
        return avistamientoService.sincronizar(
                usuarioService.obtenerIdPorCorreo(authentication.getName()), sincronizacionDTO);
    }

    // HU-09/HU-55: agregar notas de observacion
    @PutMapping("/avistamientos/{id}/observaciones")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    public AvistamientoDTO actualizarObservaciones(@PathVariable Long id,
                                                   @Valid @RequestBody ObservacionesDTO observacionesDTO) {
        return avistamientoService.actualizarObservaciones(id, observacionesDTO);
    }

    // HU-29: cambiar la especie sugerida por la IA
    @PutMapping("/avistamientos/{id}/especie")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    public AvistamientoDTO cambiarEspecie(@PathVariable Long id,
                                          @Valid @RequestBody CambiarEspecieDTO cambiarEspecieDTO) {
        return avistamientoService.cambiarEspecie(id, cambiarEspecieDTO.getIdEspecie());
    }

    @GetMapping("/avistamientos/especie/{idEspecie}")
    public List<AvistamientoDTO> listarPorEspecie(@PathVariable Long idEspecie) {
        return avistamientoService.listarPorEspecie(idEspecie);
    }

    @GetMapping("/avistamientos/ubicacion/{idUbicacion}")
    public List<AvistamientoDTO> listarPorUbicacion(@PathVariable Long idUbicacion) {
        return avistamientoService.listarPorUbicacion(idUbicacion);
    }

    // HU-50: solo investigadores ven la cola de validacion
    @GetMapping("/avistamientos/pendientes-validacion")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public List<AvistamientoDTO> listarPendientes() {
        return avistamientoService.listarPendientesDeValidacion();
    }

    // HU-44: validar o rechazar; el investigador es el usuario autenticado
    @PutMapping("/avistamientos/{id}/validacion")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public AvistamientoDTO validar(@PathVariable Long id, @Valid @RequestBody ValidarAvistamientoDTO validarDTO,
                                   Authentication authentication) {
        Long idInvestigador = usuarioService.obtenerIdPorCorreo(authentication.getName());
        return avistamientoService.validar(id, idInvestigador, validarDTO.getEstado());
    }

    // HU-48: editar avistamiento
    @PutMapping("/avistamientos/{id}")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    public AvistamientoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AvistamientoDTO avistamientoDTO) {
        return avistamientoService.actualizar(id, avistamientoDTO);
    }

    // HU-47: eliminar avistamiento
    @DeleteMapping("/avistamientos/{id}")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoService.eliminar(id);
    }
}
