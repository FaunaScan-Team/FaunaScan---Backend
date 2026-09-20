package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos_compartidos")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AvistamientoCompartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compartido")
    private Long idCompartido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avistamiento", nullable = false)
    private Avistamiento avistamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_investigador_origen", nullable = false)
    private Usuario investigadorOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_investigador_destino", nullable = false)
    private Usuario investigadorDestino;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_compartido")
    private LocalDateTime fechaCompartido;
}
