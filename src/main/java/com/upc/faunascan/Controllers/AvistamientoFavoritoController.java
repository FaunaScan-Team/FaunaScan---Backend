package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.AvistamientoFavorito;
import com.upc.faunascan.Services.AvistamientoFavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// US26/US49: marcar y quitar avistamientos favoritos de un usuario
@RestController
@RequestMapping("/api/usuarios/{idUsuario}/favoritos")
@RequiredArgsConstructor
public class AvistamientoFavoritoController {

    private final AvistamientoFavoritoService avistamientoFavoritoService;

    @GetMapping
    public List<AvistamientoFavorito> listar(@PathVariable Long idUsuario) {
        return avistamientoFavoritoService.listarPorUsuario(idUsuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoFavorito marcar(@RequestBody AvistamientoFavorito favorito) {
        return avistamientoFavoritoService.marcar(favorito);
    }

    @DeleteMapping("/{idAvistamiento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desmarcar(@PathVariable Long idUsuario, @PathVariable Long idAvistamiento) {
        avistamientoFavoritoService.desmarcar(idUsuario, idAvistamiento);
    }
}
