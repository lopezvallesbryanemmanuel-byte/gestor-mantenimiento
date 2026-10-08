package com.bryan.mantenimiento.repository;

import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findBySitioId(Long sitioId);

    List<Equipo> findByTipo(TipoEquipo tipo);

    List<Equipo> findBySitioIdAndTipo(Long sitioId, TipoEquipo tipo);

    boolean existsByNumeroSerie(String numeroSerie);

    boolean existsBySitioId(Long sitioId);
}