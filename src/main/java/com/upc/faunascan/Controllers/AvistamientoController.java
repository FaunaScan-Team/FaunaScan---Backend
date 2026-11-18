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
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvistamientoController {
    private final AvistamientoService avistamientoService;
    private final UsuarioService usuarioService;

    @GetMapping("/avistamientos")
    @Operation(summary = "Listar todos los avistamientos")
    public List<AvistamientoDTO> listar() {
        return avistamientoService.listar();
    }

    @GetMapping("/avistamientos/{id}")
    @Operation(summary = "Obtener un avistamiento por su id")
    public AvistamientoDTO obtener(@PathVariable Long id) {
        return avistamientoService.obtenerPorId(id);
    }

    // HU-06: registrar avistamiento; el autor es el usuario autenticado
    @PostMapping("/avistamientos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un avistamiento a nombre del usuario autenticado (HU-06)")
    public AvistamientoDTO registrar(@Valid @RequestBody AvistamientoDTO avistamientoDTO,
                                     Authentication authentication) {
        avistamientoDTO.setIdUsuario(usuarioService.obtenerIdPorCorreo(authentication.getName()));
        return avistamientoService.registrar(avistamientoDTO);
    }

    // HU-24: historial de avistamientos del usuario
    @GetMapping("/usuarios/{idUsuario}/avistamientos/historial")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    @Operation(summary = "Ver el historial de avistamientos de un usuario (HU-24)")
    public List<AvistamientoDTO> historial(@PathVariable Long idUsuario) {
        return avistamientoService.listarPorUsuario(idUsuario);
    }

    // HU-25: historial con filtros opcionales ?especie=&zona=&estado=
    @GetMapping("/usuarios/{idUsuario}/avistamientos/historial/filtros")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    @Operation(summary = "Filtrar el historial por especie, zona o estado (HU-25)")
    public List<AvistamientoDTO> historialFiltrado(@PathVariable Long idUsuario,
                                                   @RequestParam(required = false) String especie,
                                                   @RequestParam(required = false) String zona,
                                                   @RequestParam(required = false) String estado) {
        return avistamientoService.filtrarHistorial(idUsuario, especie, zona, estado);
    }

    // HU-14/HU-42: marcadores del mapa; HU-17: ?estado= filtra por categoria de conservacion
    @GetMapping("/avistamientos/mapa")
    @Operation(summary = "Listar los marcadores del mapa, con filtro opcional por categoría de conservación (HU-14, HU-17)")
    public List<MapaAvistamientoDTO> mapa(@RequestParam(required = false) String estado) {
        return avistamientoService.listarParaMapa(estado);
    }

    // HU-15: coordenadas para el mapa de calor
    @GetMapping("/avistamientos/densidad")
    @Operation(summary = "Obtener las coordenadas del mapa de calor (HU-15)")
    public List<CoordenadaDTO> densidad() {
        return avistamientoService.listarDensidad();
    }

    // HU-21: avistamientos recientes de otros usuarios
    @GetMapping("/avistamientos/comunidad/recientes")
    @Operation(summary = "Listar los avistamientos recientes de otros usuarios (HU-21)")
    public List<AvistamientoComunidadDTO> recientesDeComunidad(Authentication authentication) {
        return avistamientoService.listarRecientesDeComunidad(
                usuarioService.obtenerIdPorCorreo(authentication.getName()));
    }

    // HU-36: sincronizar registros hechos sin conexion
    @PostMapping("/avistamientos/sincronizar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sincronizar registros hechos sin conexión (HU-36)")
    public List<AvistamientoDTO> sincronizar(@Valid @RequestBody SincronizacionDTO sincronizacionDTO,
                                             Authentication authentication) {
        return avistamientoService.sincronizar(
                usuarioService.obtenerIdPorCorreo(authentication.getName()), sincronizacionDTO);
    }

    // HU-09/HU-55: agregar notas de observacion
    @PutMapping("/avistamientos/{id}/observaciones")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    @Operation(summary = "Agregar notas de observación y condiciones del entorno (HU-09)")
    public AvistamientoDTO actualizarObservaciones(@PathVariable Long id,
                                                   @Valid @RequestBody ObservacionesDTO observacionesDTO) {
        return avistamientoService.actualizarObservaciones(id, observacionesDTO);
    }

    // HU-29: cambiar la especie sugerida por la IA
    @PutMapping("/avistamientos/{id}/especie")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    @Operation(summary = "Cambiar la especie sugerida por la IA (HU-29)")
    public AvistamientoDTO cambiarEspecie(@PathVariable Long id,
                                          @Valid @RequestBody CambiarEspecieDTO cambiarEspecieDTO) {
        return avistamientoService.cambiarEspecie(id, cambiarEspecieDTO.getIdEspecie());
    }

    @GetMapping("/avistamientos/especie/{idEspecie}")
    @Operation(summary = "Listar los avistamientos de una especie")
    public List<AvistamientoDTO> listarPorEspecie(@PathVariable Long idEspecie) {
        return avistamientoService.listarPorEspecie(idEspecie);
    }

    @GetMapping("/avistamientos/ubicacion/{idUbicacion}")
    @Operation(summary = "Listar los avistamientos de una ubicación")
    public List<AvistamientoDTO> listarPorUbicacion(@PathVariable Long idUbicacion) {
        return avistamientoService.listarPorUbicacion(idUbicacion);
    }

    // HU-50: solo investigadores ven la cola de validacion
    @GetMapping("/avistamientos/pendientes-validacion")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    @Operation(summary = "Listar avistamientos pendientes de validación (INVESTIGADOR o ADMIN, HU-50)")
    public List<AvistamientoDTO> listarPendientes() {
        return avistamientoService.listarPendientesDeValidacion();
    }

    // HU-44: validar o rechazar; el investigador es el usuario autenticado
    @PutMapping("/avistamientos/{id}/validacion")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    @Operation(summary = "Validar o rechazar un avistamiento (INVESTIGADOR o ADMIN, HU-44)")
    public AvistamientoDTO validar(@PathVariable Long id, @Valid @RequestBody ValidarAvistamientoDTO validarDTO,
                                   Authentication authentication) {
        Long idInvestigador = usuarioService.obtenerIdPorCorreo(authentication.getName());
        return avistamientoService.validar(id, idInvestigador, validarDTO.getEstado());
    }

    // HU-48: editar avistamiento
    @PutMapping("/avistamientos/{id}")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    @Operation(summary = "Editar un avistamiento (dueño o ADMIN, HU-48)")
    public AvistamientoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AvistamientoDTO avistamientoDTO) {
        return avistamientoService.actualizar(id, avistamientoDTO);
    }

    // HU-47: eliminar avistamiento
    @DeleteMapping("/avistamientos/{id}")
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un avistamiento (dueño o ADMIN, HU-47)")
    public void eliminar(@PathVariable Long id) {
        avistamientoService.eliminar(id);
    }
}
