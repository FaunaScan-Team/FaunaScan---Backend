package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Perfil;
import com.upc.faunascan.Services.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/{id}")
    public Perfil obtener(@PathVariable Long id) {
        return perfilService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil crear(@RequestBody Perfil perfil) {
        return perfilService.crear(perfil);
    }

    // US05: editar perfil
    @PutMapping("/{id}")
    public Perfil actualizar(@PathVariable Long id, @RequestBody Perfil perfil) {
        return perfilService.actualizar(id, perfil);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        perfilService.eliminar(id);
    }
}
