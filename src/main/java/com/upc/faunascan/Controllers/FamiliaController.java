package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Familia;
import com.upc.faunascan.Services.FamiliaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/familias")
@RequiredArgsConstructor
public class FamiliaController {

    private final FamiliaService familiaService;

    @GetMapping
    public List<Familia> listar() {
        return familiaService.listar();
    }

    @GetMapping("/{id}")
    public Familia obtener(@PathVariable Long id) {
        return familiaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Familia crear(@RequestBody Familia familia) {
        return familiaService.crear(familia);
    }

    @PutMapping("/{id}")
    public Familia actualizar(@PathVariable Long id, @RequestBody Familia familia) {
        return familiaService.actualizar(id, familia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        familiaService.eliminar(id);
    }
}
