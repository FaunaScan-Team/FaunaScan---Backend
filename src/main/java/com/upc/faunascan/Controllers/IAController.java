package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.IAService;
import com.upc.faunascan.dto.EvaluacionIADTO;
import com.upc.faunascan.dto.IdentificarEspecieDTO;
import com.upc.faunascan.dto.ResultadoIdentificacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Identificacion automatica de especies (modelo simulado)
@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class IAController {
    private final IAService iaService;

    // HU-27/HU-28/HU-38/HU-51: top 3 de especies con su confianza;
    // si se envia idImagen el resultado se guarda en esa imagen del avistamiento
    @PostMapping("/identificar-especie")
    @PreAuthorize("@autorizacion.esDuenoImagen(#dto.idImagen) or hasRole('ADMIN')")
    public ResultadoIdentificacionDTO identificarEspecie(@Valid @RequestBody IdentificarEspecieDTO dto) {
        return iaService.identificarEspecie(dto);
    }

    // HU-37: precision Top-1 / Top-3 del modelo frente a la meta del 85%
    @GetMapping("/evaluacion")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public EvaluacionIADTO evaluacion() {
        return iaService.evaluarPrecision();
    }
}
