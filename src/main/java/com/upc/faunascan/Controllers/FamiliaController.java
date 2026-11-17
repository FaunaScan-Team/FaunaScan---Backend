package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.FamiliaService;
import com.upc.faunascan.dto.FamiliaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/api/familias")
@RequiredArgsConstructor
public class FamiliaController {
    private final FamiliaService familiaService;

    @GetMapping
    @Operation(summary = "Listar las familias de especies")
    public List<FamiliaDTO> listar() {
        return familiaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una familia por su id")
    public FamiliaDTO obtener(@PathVariable Long id) {
        return familiaService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una familia (solo ADMIN)")
    public FamiliaDTO crear(@Valid @RequestBody FamiliaDTO familiaDTO) {
        return familiaService.crear(familiaDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una familia (solo ADMIN)")
    public FamiliaDTO actualizar(@PathVariable Long id, @Valid @RequestBody FamiliaDTO familiaDTO) {
        return familiaService.actualizar(id, familiaDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una familia (solo ADMIN)")
    public void eliminar(@PathVariable Long id) {
        familiaService.eliminar(id);
    }
}
