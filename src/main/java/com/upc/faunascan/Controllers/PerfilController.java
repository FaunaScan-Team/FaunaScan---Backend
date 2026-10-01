package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.PerfilService;
import com.upc.faunascan.dto.PerfilDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/{id}")
    public PerfilDTO obtener(@PathVariable Long id) {
        return perfilService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilDTO crear(@RequestBody PerfilDTO perfilDTO) {
        return perfilService.crear(perfilDTO);
    }

    // US05: editar perfil
    @PutMapping("/{id}")
    public PerfilDTO actualizar(@PathVariable Long id, @RequestBody PerfilDTO perfilDTO) {
        return perfilService.actualizar(id, perfilDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        perfilService.eliminar(id);
    }
}
