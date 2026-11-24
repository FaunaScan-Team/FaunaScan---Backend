package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.CategoriaConservacionService;
import com.upc.faunascan.dto.CategoriaConservacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-conservacion")
@RequiredArgsConstructor
public class CategoriaConservacionController {
    private final CategoriaConservacionService categoriaConservacionService;

    @GetMapping
    @Operation(summary = "Listar las categorías de conservación")
    public List<CategoriaConservacionDTO> listar() {
        return categoriaConservacionService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría de conservación por su id")
    public CategoriaConservacionDTO obtener(@PathVariable Long id) {
        return categoriaConservacionService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una categoría de conservación (solo ADMIN)")
    public CategoriaConservacionDTO crear(@Valid @RequestBody CategoriaConservacionDTO categoriaConservacionDTO) {
        return categoriaConservacionService.crear(categoriaConservacionDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una categoría de conservación (solo ADMIN)")
    public CategoriaConservacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaConservacionDTO categoriaConservacionDTO) {
        return categoriaConservacionService.actualizar(id, categoriaConservacionDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una categoría de conservación (solo ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        categoriaConservacionService.eliminar(id);
    }
}
