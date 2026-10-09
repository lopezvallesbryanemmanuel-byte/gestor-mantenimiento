package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.TipoEquipo;

import java.time.LocalDate;

public record EquipoResponse(
        Long id,
        Long sitioId,
        String sitioNombre,
        TipoEquipo tipo,
        String marca,
        String modelo,
        String numeroSerie,
        LocalDate fechaInstalacion,
        Boolean activo
) { }