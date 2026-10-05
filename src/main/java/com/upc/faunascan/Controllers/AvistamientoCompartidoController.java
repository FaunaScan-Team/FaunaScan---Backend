package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoCompartidoService;
import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.AvistamientoCompartidoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// HU-56: compartir avistamiento con el equipo de investigacion
@RestController
@RequestMapping("/api/avistamientos")
@RequiredArgsConstructor
public class AvistamientoCompartidoController {
    private final AvistamientoCompartidoService avistamientoCompartidoService;
    private final UsuarioService usuarioService;

    // el body lleva idInvestigadorDestino y mensaje; el origen es el usuario autenticado
    @PostMapping("/{idAvistamiento}/compartir")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoCompartidoDTO compartir(@PathVariable Long idAvistamiento,
                                               @Valid @RequestBody AvistamientoCompartidoDTO compartidoDTO,
                                               Authentication authentication) {
        compartidoDTO.setIdAvistamiento(idAvistamiento);
        compartidoDTO.setIdInvestigadorOrigen(usuarioService.obtenerIdPorCorreo(authentication.getName()));
        return avistamientoCompartidoService.compartir(compartidoDTO);
    }

    @GetMapping("/compartidos/recibidos/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<AvistamientoCompartidoDTO> listarRecibidos(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarRecibidos(idUsuario);
    }

    @GetMapping("/compartidos/enviados/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<AvistamientoCompartidoDTO> listarEnviados(@PathVariable Long idUsuario) {
        return avistamientoCompartidoService.listarEnviados(idUsuario);
    }

    @DeleteMapping("/compartidos/{id}")
    @PreAuthorize("@autorizacion.esOrigenCompartido(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        avistamientoCompartidoService.eliminar(id);
    }
}
