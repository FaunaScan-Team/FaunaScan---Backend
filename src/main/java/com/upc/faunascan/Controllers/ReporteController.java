package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.ReporteService;
import com.upc.faunascan.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;

    // HU-18: reporte de biodiversidad por area y periodo (?area=&desde=2026-01-01&hasta=2026-09-30)
    @GetMapping("/biodiversidad")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public ReporteDetalleDTO biodiversidad(@RequestParam(required = false) String area,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                                           Authentication authentication) {
        return reporteService.generarBiodiversidad(authentication.getName(), area, desde, hasta);
    }

    // HU-19: resumen mensual de biodiversidad
    @GetMapping("/mensuales")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public ReporteDetalleDTO mensuales(Authentication authentication) {
        return reporteService.generarResumenMensual(authentication.getName());
    }

    // HU-57: panel de tendencias y patrones
    @GetMapping("/tendencias")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public TendenciasDTO tendencias() {
        return reporteService.obtenerTendencias();
    }

    // HU-49: mis reportes generados
    @GetMapping("/mios")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    public List<ReporteDTO> mios(Authentication authentication) {
        return reporteService.listarMios(authentication.getName());
    }

    // HU-20: descargar el reporte en PDF
    @GetMapping("/{idReporte}/exportar/pdf")
    @PreAuthorize("@autorizacion.esDuenoReporte(#idReporte) or hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long idReporte) {
        byte[] pdf = reporteService.exportarPdf(idReporte);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("reporte-faunascan-" + idReporte + ".pdf").build().toString())
                .body(pdf);
    }

    // HU-46: generar el enlace para compartir un reporte propio
    @PostMapping("/compartir-link")
    @PreAuthorize("@autorizacion.esDuenoReporte(#dto.idReporte) or hasRole('ADMIN')")
    public EnlaceReporteDTO compartir(@Valid @RequestBody CompartirReporteDTO dto) {
        return reporteService.compartir(dto.getIdReporte());
    }

    // HU-46: cualquier usuario autenticado puede abrir un reporte con su enlace
    @GetMapping("/compartido/{token}")
    public ReporteDetalleDTO verCompartido(@PathVariable String token) {
        return reporteService.verCompartido(token);
    }
}
