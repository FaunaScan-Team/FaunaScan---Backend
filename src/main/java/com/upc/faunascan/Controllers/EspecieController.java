package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.EspecieService;
import com.upc.faunascan.dto.EspecieDTO;
import com.upc.faunascan.dto.GuiaIdentificacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // HU-07: GET /api/especies/buscar?query=condor
    @GetMapping("/buscar")
    public List<EspecieDTO> buscar(@RequestParam("query") String texto) {
        return especieService.buscar(texto);
    }

    // HU-30: GET /api/especies/detalles-multiples?ids=12,45,8
    @GetMapping("/detalles-multiples")
    public List<EspecieDTO> detallesMultiples(@RequestParam List<Long> ids) {
        return especieService.obtenerDetallesMultiples(ids);
    }

    // HU-58: guia visual de identificacion para no expertos
    @GetMapping("/{id}/guia-identificacion")
    public GuiaIdentificacionDTO guiaIdentificacion(@PathVariable Long id) {
        return especieService.obtenerGuia(id);
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
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EspecieDTO crear(@Valid @RequestBody EspecieDTO especieDTO) {
        return especieService.crear(especieDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EspecieDTO actualizar(@PathVariable Long id, @Valid @RequestBody EspecieDTO especieDTO) {
        return especieService.actualizar(id, especieDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        especieService.eliminar(id);
    }
}
