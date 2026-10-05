package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.CategoriaConservacionService;
import com.upc.faunascan.dto.CategoriaConservacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-conservacion")
@RequiredArgsConstructor
public class CategoriaConservacionController {
    private final CategoriaConservacionService categoriaConservacionService;

    @GetMapping
    public List<CategoriaConservacionDTO> listar() {
        return categoriaConservacionService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaConservacionDTO obtener(@PathVariable Long id) {
        return categoriaConservacionService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaConservacionDTO crear(@Valid @RequestBody CategoriaConservacionDTO categoriaConservacionDTO) {
        return categoriaConservacionService.crear(categoriaConservacionDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaConservacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaConservacionDTO categoriaConservacionDTO) {
        return categoriaConservacionService.actualizar(id, categoriaConservacionDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        categoriaConservacionService.eliminar(id);
    }
}
