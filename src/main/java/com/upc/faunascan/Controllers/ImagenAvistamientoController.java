package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.ImagenAvistamientoService;
import com.upc.faunascan.dto.ImagenAvistamientoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

// HU-10: fotos adjuntas a un avistamiento
@RestController
@RequestMapping("/api/avistamientos/{idAvistamiento}/imagenes")
@RequiredArgsConstructor
public class ImagenAvistamientoController {
    private final ImagenAvistamientoService imagenAvistamientoService;

    @GetMapping
    @Operation(summary = "Listar las fotos de un avistamiento (HU-10)")
    public List<ImagenAvistamientoDTO> listar(@PathVariable Long idAvistamiento) {
        return imagenAvistamientoService.listarPorAvistamiento(idAvistamiento);
    }

    @PostMapping
    @PreAuthorize("@autorizacion.esDuenoAvistamiento(#idAvistamiento) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adjuntar una foto a un avistamiento (dueño o ADMIN)")
    public ImagenAvistamientoDTO agregar(@PathVariable Long idAvistamiento,
                                         @Valid @RequestBody ImagenAvistamientoDTO imagenDTO) {
        imagenDTO.setIdAvistamiento(idAvistamiento);
        return imagenAvistamientoService.agregar(imagenDTO);
    }

    @DeleteMapping("/{idImagen}")
    @PreAuthorize("@autorizacion.esDuenoImagen(#idImagen) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una foto de un avistamiento (dueño o ADMIN)")
    public void eliminar(@PathVariable Long idImagen) {
        imagenAvistamientoService.eliminar(idImagen);
    }
}
