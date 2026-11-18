package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.UbicacionService;
import com.upc.faunascan.dto.UbicacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

    @GetMapping
    @Operation(summary = "Listar las ubicaciones")
    public List<UbicacionDTO> listar() {
        return ubicacionService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una ubicación por su id")
    public UbicacionDTO obtener(@PathVariable Long id) {
        return ubicacionService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una ubicación")
    public UbicacionDTO crear(@Valid @RequestBody UbicacionDTO ubicacionDTO) {
        return ubicacionService.crear(ubicacionDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una ubicación")
    public UbicacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody UbicacionDTO ubicacionDTO) {
        return ubicacionService.actualizar(id, ubicacionDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una ubicación")
    public void eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
    }
}
