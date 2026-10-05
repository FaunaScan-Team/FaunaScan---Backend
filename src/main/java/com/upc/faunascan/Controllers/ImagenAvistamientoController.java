package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.ImagenAvistamientoService;
import com.upc.faunascan.dto.ImagenAvistamientoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// HU-10: fotos adjuntas a un avistamiento
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
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#idAvistamiento) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ImagenAvistamientoDTO agregar(@PathVariable Long idAvistamiento,
                                         @Valid @RequestBody ImagenAvistamientoDTO imagenDTO) {
        imagenDTO.setIdAvistamiento(idAvistamiento);
        return imagenAvistamientoService.agregar(imagenDTO);
    }

    @DeleteMapping("/{idImagen}")
    @PreAuthorize("@autorizacion.esDuenoImagen(#idImagen) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idImagen) {
        imagenAvistamientoService.eliminar(idImagen);
    }
}
