package com.bryan.mantenimiento.entity;

import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipo")
@Getter
@Setter
@NoArgsConstructor
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // corresponde a IDENTITY(1,1) de SQL Server
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sitio_id", nullable = false)
    private Sitio sitio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEquipo tipo;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(name = "numero_serie", nullable = false, unique = true, length = 50)
    private String numeroSerie;

    @Column(name = "fecha_instalacion", nullable = false)
    private LocalDate fechaInstalacion;

    @Column(nullable = false)
    private Boolean activo = true; // corresponde a BIT

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mantenimiento> mantenimientos = new ArrayList<>();
}