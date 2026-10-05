package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// HU-18/HU-19/HU-20/HU-46/HU-49: reportes generados por un usuario
@Entity
@Table(name = "reportes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // biodiversidad, mensual o tendencias
    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "area", length = 150)
    private String area;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    // token del enlace compartible (HU-46)
    @Column(name = "enlace_compartido", length = 100, unique = true)
    private String enlaceCompartido;
}
