package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.CategoriaConservacion;
import com.upc.faunascan.Services.CategoriaConservacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-conservacion")
@RequiredArgsConstructor
public class CategoriaConservacionController {

    private final CategoriaConservacionService categoriaConservacionService;

    @GetMapping
    public List<CategoriaConservacion> listar() {
        return categoriaConservacionService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaConservacion obtener(@PathVariable Long id) {
        return categoriaConservacionService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaConservacion crear(@RequestBody CategoriaConservacion categoria) {
        return categoriaConservacionService.crear(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaConservacion actualizar(@PathVariable Long id, @RequestBody CategoriaConservacion categoria) {
        return categoriaConservacionService.actualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        categoriaConservacionService.eliminar(id);
    }
}
