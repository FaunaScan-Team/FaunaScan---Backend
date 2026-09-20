package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Services.ImagenAvistamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// US10: fotos adjuntas a un avistamiento
@RestController
@RequestMapping("/api/avistamientos/{idAvistamiento}/imagenes")
@RequiredArgsConstructor
public class ImagenAvistamientoController {

    private final ImagenAvistamientoService imagenAvistamientoService;

    @GetMapping
    public List<ImagenAvistamiento> listar(@PathVariable Long idAvistamiento) {
        return imagenAvistamientoService.listarPorAvistamiento(idAvistamiento);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImagenAvistamiento agregar(@RequestBody ImagenAvistamiento imagen) {
        return imagenAvistamientoService.agregar(imagen);
    }

    @DeleteMapping("/{idImagen}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idImagen) {
        imagenAvistamientoService.eliminar(idImagen);
    }
}
