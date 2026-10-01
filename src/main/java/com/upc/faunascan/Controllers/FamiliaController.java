package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.FamiliaService;
import com.upc.faunascan.dto.FamiliaDTO;
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
    public List<FamiliaDTO> listar() {
        return familiaService.listar();
    }

    @GetMapping("/{id}")
    public FamiliaDTO obtener(@PathVariable Long id) {
        return familiaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FamiliaDTO crear(@RequestBody FamiliaDTO familiaDTO) {
        return familiaService.crear(familiaDTO);
    }

    @PutMapping("/{id}")
    public FamiliaDTO actualizar(@PathVariable Long id, @RequestBody FamiliaDTO familiaDTO) {
        return familiaService.actualizar(id, familiaDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        familiaService.eliminar(id);
    }
}
