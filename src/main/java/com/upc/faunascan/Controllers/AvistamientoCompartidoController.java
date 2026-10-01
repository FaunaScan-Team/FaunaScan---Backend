package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoCompartidoService;
import com.upc.faunascan.dto.AvistamientoCompartidoDTO;
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
    public AvistamientoCompartidoDTO compartir(@RequestBody AvistamientoCompartidoDTO compartidoDTO) {
        return avistamientoCompartidoService.compartir(compartidoDTO);
    }

    @GetMapping("/recibidos/{idUsuario}")
    public List<AvistamientoCompartidoDTO> listarRecibidos(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarRecibidos(idUsuario);
    }

    @GetMapping("/enviados/{idUsuario}")
    public List<AvistamientoCompartidoDTO> listarEnviados(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarEnviados(idUsuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoCompartidoService.eliminar(id);
    }
}
