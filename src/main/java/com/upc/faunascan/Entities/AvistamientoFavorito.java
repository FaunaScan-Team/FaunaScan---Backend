package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos_favoritos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_avistamiento"}))
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AvistamientoFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avistamiento", nullable = false)
    private Avistamiento avistamiento;

    @Column(name = "fecha_marcado")
    private LocalDateTime fechaMarcado;
}
