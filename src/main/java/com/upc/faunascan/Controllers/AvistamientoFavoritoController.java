package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoFavoritoService;
import com.upc.faunascan.dto.AvistamientoFavoritoDTO;
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
    public List<AvistamientoFavoritoDTO> listar(@PathVariable Long idUsuario) {
        return avistamientoFavoritoService.listarPorUsuario(idUsuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvistamientoFavoritoDTO marcar(@PathVariable Long idUsuario,
                                          @RequestBody AvistamientoFavoritoDTO favoritoDTO) {
        favoritoDTO.setIdUsuario(idUsuario);
        return avistamientoFavoritoService.marcar(favoritoDTO);
    }

    @DeleteMapping("/{idAvistamiento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desmarcar(@PathVariable Long idUsuario, @PathVariable Long idAvistamiento) {
        avistamientoFavoritoService.desmarcar(idUsuario, idAvistamiento);
    }
}
