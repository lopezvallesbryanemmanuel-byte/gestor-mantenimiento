package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import com.bryan.mantenimiento.entity.enums.TipoMantenimiento;

import java.time.LocalDate;

public record MantenimientoResponse(
        Long id,
        Long equipoId,
        String equipoNumeroSerie,
        LocalDate fecha,
        TipoMantenimiento tipo,
        String descripcion,
        String tecnico,
        EstadoMantenimiento estado
) { }