package com.bryan.mantenimiento.entity;

import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import com.bryan.mantenimiento.entity.enums.TipoMantenimiento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "mantenimiento")
@Getter
@Setter
@NoArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMantenimiento tipo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String tecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMantenimiento estado = EstadoMantenimiento.PENDIENTE;
}