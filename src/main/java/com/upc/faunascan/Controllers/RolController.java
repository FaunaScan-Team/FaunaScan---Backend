package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.RolService;
import com.upc.faunascan.dto.RolDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RolDTO> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public RolDTO obtener(@PathVariable Long id) {
        return rolService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RolDTO crear(@Valid @RequestBody RolDTO rolDTO) {
        return rolService.crear(rolDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RolDTO actualizar(@PathVariable Long id, @Valid @RequestBody RolDTO rolDTO) {
        return rolService.actualizar(id, rolDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
    }
}
