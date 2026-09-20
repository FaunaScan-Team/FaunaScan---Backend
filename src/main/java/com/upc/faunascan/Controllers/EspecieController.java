package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Services.EspecieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especies")
@RequiredArgsConstructor
public class EspecieController {

    private final EspecieService especieService;

    @GetMapping
    public List<Especie> listar() {
        return especieService.listar();
    }

    // US46: GET /api/especies/buscar?texto=condor
    @GetMapping("/buscar")
    public List<Especie> buscar(@RequestParam String texto) {
        return especieService.buscar(texto);
    }

    @GetMapping("/familia/{idFamilia}")
    public List<Especie> listarPorFamilia(@PathVariable Long idFamilia) {
        return especieService.listarPorFamilia(idFamilia);
    }

    @GetMapping("/categoria/{idCategoria}")
    public List<Especie> listarPorCategoria(@PathVariable Long idCategoria) {
        return especieService.listarPorCategoria(idCategoria);
    }

    @GetMapping("/{id}")
    public Especie obtener(@PathVariable Long id) {
        return especieService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especie crear(@RequestBody Especie especie) {
        return especieService.crear(especie);
    }

    @PutMapping("/{id}")
    public Especie actualizar(@PathVariable Long id, @RequestBody Especie especie) {
        return especieService.actualizar(id, especie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        especieService.eliminar(id);
    }
}
