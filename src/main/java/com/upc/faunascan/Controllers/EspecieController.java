package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.EspecieService;
import com.upc.faunascan.dto.EspecieDTO;
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
    public List<EspecieDTO> listar() {
        return especieService.listar();
    }

    // US46: GET /api/especies/buscar?texto=condor
    @GetMapping("/buscar")
    public List<EspecieDTO> buscar(@RequestParam String texto) {
        return especieService.buscar(texto);
    }

    @GetMapping("/familia/{idFamilia}")
    public List<EspecieDTO> listarPorFamilia(@PathVariable Long idFamilia) {
        return especieService.listarPorFamilia(idFamilia);
    }

    @GetMapping("/categoria/{idCategoria}")
    public List<EspecieDTO> listarPorCategoria(@PathVariable Long idCategoria) {
        return especieService.listarPorCategoria(idCategoria);
    }

    @GetMapping("/{id}")
    public EspecieDTO obtener(@PathVariable Long id) {
        return especieService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EspecieDTO crear(@RequestBody EspecieDTO especieDTO) {
        return especieService.crear(especieDTO);
    }

    @PutMapping("/{id}")
    public EspecieDTO actualizar(@PathVariable Long id, @RequestBody EspecieDTO especieDTO) {
        return especieService.actualizar(id, especieDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        especieService.eliminar(id);
    }
}
