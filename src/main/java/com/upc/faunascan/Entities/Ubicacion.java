package com.upc.faunascan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ubicaciones")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @Column(name = "latitud", precision = 9, scale = 6, nullable = false)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6, nullable = false)
    private BigDecimal longitud;

    @Column(name = "direccion")
    private String direccion;
}
