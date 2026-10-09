package com.bryan.mantenimiento.repository;

import com.bryan.mantenimiento.entity.Mantenimiento;
import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByEquipoIdOrderByFechaDesc(Long equipoId);

    List<Mantenimiento> findByEstado(EstadoMantenimiento estado);

    List<Mantenimiento> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<Mantenimiento> findByEstadoAndFechaBetween(EstadoMantenimiento estado, LocalDate desde, LocalDate hasta);

    // Consulta JPQL personalizada (usa nombres de clases y atributos, no de tablas)
    @Query("""
            SELECT m.equipo.sitio.nombre, COUNT(m)
            FROM Mantenimiento m
            WHERE m.estado = :estado
            GROUP BY m.equipo.sitio.nombre
            """)
    List<Object[]> contarPorSitioYEstado(@Param("estado") EstadoMantenimiento estado);
}