package com.upc.faunascan.ServiceImpl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.upc.faunascan.Entities.Reporte;
import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.ReporteRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.Services.ReporteService;
import com.upc.faunascan.dto.*;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {
    public static final String TIPO_BIODIVERSIDAD = "biodiversidad";
    public static final String TIPO_MENSUAL = "mensual";
    private static final Locale ES = Locale.of("es", "PE");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ReporteRepository reporteRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;

    // HU-18: especies registradas por area y periodo; se guarda para poder exportarlo y compartirlo
    @Override
    @Transactional
    public ReporteDetalleDTO generarBiodiversidad(String correo, String area, LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new RuntimeException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        Reporte reporte = new Reporte();
        reporte.setUsuario(buscarUsuario(correo));
        reporte.setTipo(TIPO_BIODIVERSIDAD);
        reporte.setArea(area == null || area.isBlank() ? null : area.trim());
        reporte.setFechaInicio(desde);
        reporte.setFechaFin(hasta);
        reporte.setFechaGeneracion(LocalDateTime.now());
        return construirDetalle(reporteRepository.save(reporte));
    }

    // HU-19: resumen mensual de avistamientos validados
    @Override
    @Transactional
    public ReporteDetalleDTO generarResumenMensual(String correo) {
        Reporte reporte = new Reporte();
        reporte.setUsuario(buscarUsuario(correo));
        reporte.setTipo(TIPO_MENSUAL);
        reporte.setFechaGeneracion(LocalDateTime.now());
        return construirDetalle(reporteRepository.save(reporte));
    }

    // HU-57: tendencia mensual por especie y zonas con mayor actividad
    @Override
    public TendenciasDTO obtenerTendencias() {
        return new TendenciasDTO(avistamientoRepository.tendenciaPorEspecie(),
                avistamientoRepository.zonasConMayorActividad());
    }

    // HU-20: el PDF reutiliza las consultas de HU-18 / HU-19 segun el tipo del reporte
    @Override
    @Transactional(readOnly = true)
    public byte[] exportarPdf(Long idReporte) {
        Reporte reporte = buscarReporte(idReporte);
        ReporteDetalleDTO detalle = construirDetalle(reporte);
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(27, 94, 32));
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);
        documento.add(new Paragraph("FaunaScan Perú", titulo));
        documento.add(new Paragraph(TIPO_BIODIVERSIDAD.equals(reporte.getTipo())
                ? "Reporte de biodiversidad por área y período" : "Resumen mensual de biodiversidad",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("Reporte N.° " + reporte.getIdReporte(), normal));
        documento.add(new Paragraph("Generado por: " + reporte.getUsuario().getNombre() + " "
                + reporte.getUsuario().getApellido(), normal));
        documento.add(new Paragraph("Fecha de generación: " + reporte.getFechaGeneracion().format(FECHA_HORA), normal));
        if (TIPO_BIODIVERSIDAD.equals(reporte.getTipo())) {
            documento.add(new Paragraph("Área: " + (reporte.getArea() != null ? reporte.getArea() : "Todas"), normal));
            documento.add(new Paragraph("Período: " + reporte.getFechaInicio().format(FECHA) + " - "
                    + reporte.getFechaFin().format(FECHA), normal));
        }
        documento.add(new Paragraph("Total de avistamientos validados: " + detalle.getTotalRegistros()
                + "   |   Especies: " + detalle.getTotalEspecies(), normal));
        documento.add(new Paragraph(" "));

        if (TIPO_BIODIVERSIDAD.equals(reporte.getTipo())) {
            PdfPTable tabla = crearTabla(new float[]{4, 3, 2}, "Especie", "Estado de conservación", "Registros");
            for (ItemBiodiversidadDTO item : detalle.getItems()) {
                agregarFila(tabla, item.getNombreComun(), valorONd(item.getEstadoConservacion()),
                        String.valueOf(item.getTotalRegistros()));
            }
            agregarTablaOMensaje(documento, tabla, detalle.getItems().isEmpty(), normal);
        } else {
            PdfPTable tabla = crearTabla(new float[]{3, 2, 2}, "Mes", "Avistamientos", "Especies");
            for (ResumenMensualDTO mes : detalle.getMeses()) {
                agregarFila(tabla, nombreMes(mes.getMes()) + " " + mes.getAnio(),
                        String.valueOf(mes.getTotalAvistamientos()), String.valueOf(mes.getTotalEspecies()));
            }
            agregarTablaOMensaje(documento, tabla, detalle.getMeses().isEmpty(), normal);
        }
        documento.close();
        return salida.toByteArray();
    }

    // HU-46: genera (una sola vez) el token del enlace compartible
    @Override
    @Transactional
    public EnlaceReporteDTO compartir(Long idReporte) {
        Reporte reporte = buscarReporte(idReporte);
        if (reporte.getEnlaceCompartido() == null) {
            reporte.setEnlaceCompartido(UUID.randomUUID().toString());
            reporteRepository.save(reporte);
        }
        return new EnlaceReporteDTO(reporte.getIdReporte(), reporte.getEnlaceCompartido(),
                "/api/reportes/compartido/" + reporte.getEnlaceCompartido());
    }

    // HU-46: abrir un reporte desde su enlace
    @Override
    @Transactional(readOnly = true)
    public ReporteDetalleDTO verCompartido(String token) {
        Reporte reporte = reporteRepository.findByEnlaceCompartido(token)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reporte con ese enlace"));
        return construirDetalle(reporte);
    }

    // HU-49: reportes generados por el usuario autenticado
    @Override
    public List<ReporteDTO> listarMios(String correo) {
        return reporteRepository.findByUsuario_IdUsuarioOrderByFechaGeneracionDesc(buscarUsuario(correo).getIdUsuario())
                .stream().map(this::aDTO).toList();
    }

    private ReporteDetalleDTO construirDetalle(Reporte reporte) {
        ReporteDetalleDTO detalle = new ReporteDetalleDTO();
        detalle.setReporte(aDTO(reporte));
        if (TIPO_BIODIVERSIDAD.equals(reporte.getTipo())) {
            List<ItemBiodiversidadDTO> items = avistamientoRepository.reporteBiodiversidad(
                    reporte.getArea() != null ? reporte.getArea() : "",
                    reporte.getFechaInicio().atStartOfDay(), reporte.getFechaFin().atTime(LocalTime.MAX));
            detalle.setItems(items);
            detalle.setTotalRegistros(items.stream().mapToLong(ItemBiodiversidadDTO::getTotalRegistros).sum());
            detalle.setTotalEspecies((long) items.size());
        } else {
            List<ResumenMensualDTO> meses = avistamientoRepository.resumenMensual();
            detalle.setMeses(meses);
            detalle.setTotalRegistros(meses.stream().mapToLong(ResumenMensualDTO::getTotalAvistamientos).sum());
            detalle.setTotalEspecies(avistamientoRepository.contarEspeciesValidadas());
        }
        return detalle;
    }

    private PdfPTable crearTabla(float[] anchos, String... columnas) {
        PdfPTable tabla = new PdfPTable(anchos);
        tabla.setWidthPercentage(100);
        Font cabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String columna : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, cabecera));
            celda.setBackgroundColor(new Color(46, 125, 50));
            celda.setPadding(5);
            tabla.addCell(celda);
        }
        return tabla;
    }

    private void agregarFila(PdfPTable tabla, String... valores) {
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (String valor : valores) {
            PdfPCell celda = new PdfPCell(new Phrase(valor, fuente));
            celda.setPadding(4);
            tabla.addCell(celda);
        }
    }

    private void agregarTablaOMensaje(Document documento, PdfPTable tabla, boolean vacio, Font fuente) {
        documento.add(vacio
                ? new Paragraph("No hay avistamientos validados para los criterios del reporte.", fuente)
                : tabla);
    }

    private String nombreMes(Integer mes) {
        String nombre = java.time.Month.of(mes).getDisplayName(TextStyle.FULL, ES);
        return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
    }

    private String valorONd(String valor) {
        return valor != null ? valor : "Sin categoría";
    }

    private Reporte buscarReporte(Long idReporte) {
        return reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reporte no encontrado con id: " + idReporte));
    }

    private Usuario buscarUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo));
    }

    private ReporteDTO aDTO(Reporte reporte) {
        return new ReporteDTO(reporte.getIdReporte(), reporte.getTipo(), reporte.getArea(), reporte.getFechaInicio(),
                reporte.getFechaFin(), reporte.getFechaGeneracion(), reporte.getEnlaceCompartido());
    }
}
