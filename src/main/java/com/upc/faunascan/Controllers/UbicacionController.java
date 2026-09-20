package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.Services.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public List<Ubicacion> listar() {
        return ubicacionService.listar();
    }

    @GetMapping("/{id}")
    public Ubicacion obtener(@PathVariable Long id) {
        return ubicacionService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ubicacion crear(@RequestBody Ubicacion ubicacion) {
        return ubicacionService.crear(ubicacion);
    }

    @PutMapping("/{id}")
    public Ubicacion actualizar(@PathVariable Long id, @RequestBody Ubicacion ubicacion) {
        return ubicacionService.actualizar(id, ubicacion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
    }
}
