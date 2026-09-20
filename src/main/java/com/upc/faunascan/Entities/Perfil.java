package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfiles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idPerfil;

    // Lado inverso de la relación 1-1 (la FK real está en usuarios.id_perfil)
    @OneToOne(mappedBy = "perfil")
    private Usuario usuario;

    @Column(name = "biografia")
    private String biografia;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
