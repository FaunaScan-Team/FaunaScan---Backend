package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "especies")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie")
    private Long idEspecie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_familia", nullable = false)
    private Familia familia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaConservacion categoriaConservacion;

    @Column(name = "nombre_comun", length = 100, nullable = false)
    private String nombreComun;

    @Column(name = "nombre_cientifico", length = 150, nullable = false)
    private String nombreCientifico;

    @Column(name = "imagen_referencia")
    private String imagenReferencia;

    @Column(name = "pistas_identificacion")
    private String pistasIdentificacion;
}
