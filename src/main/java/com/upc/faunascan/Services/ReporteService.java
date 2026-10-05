package com.upc.faunascan.Services;

import com.upc.faunascan.dto.EnlaceReporteDTO;
import com.upc.faunascan.dto.ReporteDTO;
import com.upc.faunascan.dto.ReporteDetalleDTO;
import com.upc.faunascan.dto.TendenciasDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    ReporteDetalleDTO generarBiodiversidad(String correo, String area, LocalDate desde, LocalDate hasta);
    ReporteDetalleDTO generarResumenMensual(String correo);
    TendenciasDTO obtenerTendencias();
    byte[] exportarPdf(Long idReporte);
    EnlaceReporteDTO compartir(Long idReporte);
    ReporteDetalleDTO verCompartido(String token);
    List<ReporteDTO> listarMios(String correo);
}
