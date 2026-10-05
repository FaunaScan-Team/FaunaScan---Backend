package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoFavoritoService;
import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.AvistamientoFavoritoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// HU-26: marcar y quitar avistamientos favoritos del usuario autenticado
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvistamientoFavoritoController {
    private final AvistamientoFavoritoService avistamientoFavoritoService;
    private final UsuarioService usuarioService;

    @GetMapping("/usuarios/{idUsuario}/favoritos")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<AvistamientoFavoritoDTO> listar(@PathVariable Long idUsuario) {
        return avistamientoFavoritoService.listarPorUsuario(idUsuario);
    }

    @PostMapping("/avistamientos/{idAvistamiento}/favorito")
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoFavoritoDTO marcar(@PathVariable Long idAvistamiento, Authentication authentication) {
        AvistamientoFavoritoDTO favoritoDTO = new AvistamientoFavoritoDTO();
        favoritoDTO.setIdUsuario(usuarioService.obtenerIdPorCorreo(authentication.getName()));
        favoritoDTO.setIdAvistamiento(idAvistamiento);
        return avistamientoFavoritoService.marcar(favoritoDTO);
    }

    @DeleteMapping("/avistamientos/{idAvistamiento}/favorito")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desmarcar(@PathVariable Long idAvistamiento, Authentication authentication) {
        Long idUsuario = usuarioService.obtenerIdPorCorreo(authentication.getName());
        avistamientoFavoritoService.desmarcar(idUsuario, idAvistamiento);
    }
}
