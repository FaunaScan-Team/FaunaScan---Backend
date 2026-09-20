package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Services.AvistamientoService;
import com.upc.faunascan.Services.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avistamientos")
@RequiredArgsConstructor
public class AvistamientoController {

    private final AvistamientoService avistamientoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<Avistamiento> listar() {
        return avistamientoService.listar();
    }

    @GetMapping("/{id}")
    public Avistamiento obtener(@PathVariable Long id) {
        return avistamientoService.obtenerPorId(id);
    }

    // US06/US09/US43: registrar avistamiento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avistamiento registrar(@RequestBody Avistamiento avistamiento) {
        return avistamientoService.registrar(avistamiento);
    }

    // US14: historial del usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<Avistamiento> listarPorUsuario(@PathVariable Long idUsuario) {
        return avistamientoService.listarPorUsuario(idUsuario);
    }

    // US17/US25: filtros
    @GetMapping("/especie/{idEspecie}")
    public List<Avistamiento> listarPorEspecie(@PathVariable Long idEspecie) {
        return avistamientoService.listarPorEspecie(idEspecie);
    }

    @GetMapping("/ubicacion/{idUbicacion}")
    public List<Avistamiento> listarPorUbicacion(@PathVariable Long idUbicacion) {
        return avistamientoService.listarPorUbicacion(idUbicacion);
    }

    // US45: cola de revision del investigador
    @GetMapping("/pendientes")
    public List<Avistamiento> listarPendientes() {
        return avistamientoService.listarPendientesDeValidacion();
    }

    // US45: aprobar o rechazar un avistamiento
    @PatchMapping("/{id}/validar")
    public Avistamiento validar(@PathVariable Long id, @RequestBody ValidarRequest request) {
        Usuario investigador = usuarioService.obtenerPorId(request.getIdInvestigador());
        return avistamientoService.validar(id, investigador, request.getEstado());
    }

    @PutMapping("/{id}")
    public Avistamiento actualizar(@PathVariable Long id, @RequestBody Avistamiento avistamiento) {
        return avistamientoService.actualizar(id, avistamiento);
    }

    // US49: eliminar avistamiento
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoService.eliminar(id);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ValidarRequest {
        private Long idInvestigador;
        private String estado; // "validado" | "rechazado"
    }
}
