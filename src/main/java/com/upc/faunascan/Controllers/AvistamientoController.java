package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoService;
import com.upc.faunascan.dto.AvistamientoDTO;
import com.upc.faunascan.dto.ValidarAvistamientoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avistamientos")
@RequiredArgsConstructor
public class AvistamientoController {
    private final AvistamientoService avistamientoService;

    @GetMapping
    public List<AvistamientoDTO> listar() {
        return avistamientoService.listar();
    }

    @GetMapping("/{id}")
    public AvistamientoDTO obtener(@PathVariable Long id) {
        return avistamientoService.obtenerPorId(id);
    }

    // US06/US09/US43: registrar avistamiento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoDTO registrar(@RequestBody AvistamientoDTO avistamientoDTO) {
        return avistamientoService.registrar(avistamientoDTO);
    }

    // US14: historial del usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<AvistamientoDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return avistamientoService.listarPorUsuario(idUsuario);
    }

    // US17/US25: filtros
    @GetMapping("/especie/{idEspecie}")
    public List<AvistamientoDTO> listarPorEspecie(@PathVariable Long idEspecie) {
        return avistamientoService.listarPorEspecie(idEspecie);
    }

    @GetMapping("/ubicacion/{idUbicacion}")
    public List<AvistamientoDTO> listarPorUbicacion(@PathVariable Long idUbicacion) {
        return avistamientoService.listarPorUbicacion(idUbicacion);
    }

    // US45: cola de revision del investigador
    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public List<AvistamientoDTO> listarPendientes() {
        return avistamientoService.listarPendientesDeValidacion();
    }

    // US45: cola de revision del investigador
    @PatchMapping("/{id}/validar")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public AvistamientoDTO validar(@PathVariable Long id, @RequestBody ValidarAvistamientoDTO validarDTO) {
        return avistamientoService.validar(id, validarDTO.getIdInvestigador(), validarDTO.getEstado());
    }

    @PutMapping("/{id}")
    public AvistamientoDTO actualizar(@PathVariable Long id, @RequestBody AvistamientoDTO avistamientoDTO) {
        return avistamientoService.actualizar(id, avistamientoDTO);
    }

    // US49: eliminar avistamiento
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoService.eliminar(id);
    }
}
