package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Avistamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avistamiento")
    private Long idAvistamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especie", nullable = false)
    private Especie especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @Column(name = "fecha_avistamiento", nullable = false)
    private LocalDateTime fechaAvistamiento;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "condiciones_entorno")
    private String condicionesEntorno;

    @Column(name = "estado_validacion", length = 50)
    private String estadoValidacion;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    // Investigador (usuario) que valida el avistamiento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_investigador_validador")
    private Usuario investigadorValidador;

    @Column(name = "sincronizado_local")
    private Boolean sincronizadoLocal;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
