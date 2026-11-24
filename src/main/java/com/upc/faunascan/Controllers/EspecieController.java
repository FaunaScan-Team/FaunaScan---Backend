package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.EspecieService;
import com.upc.faunascan.dto.EspecieDTO;
import com.upc.faunascan.dto.GuiaIdentificacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/api/especies")
@RequiredArgsConstructor
public class EspecieController {
    private final EspecieService especieService;

    @GetMapping
    @Operation(summary = "Listar todas las especies del catálogo")
    public List<EspecieDTO> listar() {
        return especieService.listar();
    }

    // HU-07: GET /api/especies/buscar?query=condor
    @GetMapping("/buscar")
    @Operation(summary = "Buscar especies por nombre común o científico")
    public List<EspecieDTO> buscar(@RequestParam("query") String texto) {
        return especieService.buscar(texto);
    }

    // HU-30: GET /api/especies/detalles-multiples?ids=12,45,8
    @GetMapping("/detalles-multiples")
    @Operation(summary = "Obtener varias especies por sus ids")
    public List<EspecieDTO> detallesMultiples(@RequestParam List<Long> ids) {
        return especieService.obtenerDetallesMultiples(ids);
    }

    // HU-58: guia visual de identificacion para no expertos
    @GetMapping("/{id}/guia-identificacion")
    @Operation(summary = "Guía visual para identificar una especie")
    public GuiaIdentificacionDTO guiaIdentificacion(@PathVariable Long id) {
        return especieService.obtenerGuia(id);
    }

    @GetMapping("/familia/{idFamilia}")
    @Operation(summary = "Listar especies de una familia")
    public List<EspecieDTO> listarPorFamilia(@PathVariable Long idFamilia) {
        return especieService.listarPorFamilia(idFamilia);
    }

    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Listar especies de una categoría de conservación")
    public List<EspecieDTO> listarPorCategoria(@PathVariable Long idCategoria) {
        return especieService.listarPorCategoria(idCategoria);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una especie por su id")
    public EspecieDTO obtener(@PathVariable Long id) {
        return especieService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una especie (solo ADMIN)")
    public EspecieDTO crear(@Valid @RequestBody EspecieDTO especieDTO) {
        return especieService.crear(especieDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una especie (solo ADMIN)")
    public EspecieDTO actualizar(@PathVariable Long id, @Valid @RequestBody EspecieDTO especieDTO) {
        return especieService.actualizar(id, especieDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una especie (solo ADMIN)")
    public void eliminar(@PathVariable Long id) {
        especieService.eliminar(id);
    }
}
