package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.ImagenAvistamientoService;
import com.upc.faunascan.dto.ImagenAvistamientoDTO;
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
    public List<ImagenAvistamientoDTO> listar(@PathVariable Long idAvistamiento) {
        return imagenAvistamientoService.listarPorAvistamiento(idAvistamiento);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImagenAvistamientoDTO agregar(@PathVariable Long idAvistamiento,
                                         @RequestBody ImagenAvistamientoDTO imagenDTO) {
        // El avistamiento lo define la ruta, no el cuerpo de la peticion.
        imagenDTO.setIdAvistamiento(idAvistamiento);
        return imagenAvistamientoService.agregar(imagenDTO);
    }

    @DeleteMapping("/{idImagen}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idImagen) {
        imagenAvistamientoService.eliminar(idImagen);
    }
}
