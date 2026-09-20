package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.AvistamientoCompartido;
import com.upc.faunascan.Services.AvistamientoCompartidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// US48: compartir el link de un avistamiento entre investigadores
@RestController
@RequestMapping("/api/avistamientos/compartidos")
@RequiredArgsConstructor
public class AvistamientoCompartidoController {

    private final AvistamientoCompartidoService avistamientoCompartidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoCompartido compartir(@RequestBody AvistamientoCompartido compartido) {
        return avistamientoCompartidoService.compartir(compartido);
    }

    @GetMapping("/recibidos/{idUsuario}")
    public List<AvistamientoCompartido> listarRecibidos(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarRecibidos(idUsuario);
    }

    @GetMapping("/enviados/{idUsuario}")
    public List<AvistamientoCompartido> listarEnviados(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarEnviados(idUsuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoCompartidoService.eliminar(id);
    }
}
