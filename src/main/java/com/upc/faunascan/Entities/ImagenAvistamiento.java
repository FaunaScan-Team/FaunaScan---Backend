package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagenes_avistamiento")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ImagenAvistamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avistamiento", nullable = false)
    private Avistamiento avistamiento;

    @Column(name = "ruta_imagen", length = 255, nullable = false)
    private String rutaImagen;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    // Resultado del análisis de IA sobre la imagen (JSON)
    @Column(name = "resultado_ia", columnDefinition = "json")
    private String resultadoIa;
}
