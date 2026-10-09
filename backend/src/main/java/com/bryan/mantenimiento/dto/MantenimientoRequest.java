package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.TipoMantenimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MantenimientoRequest(
        @NotNull(message = "El equipo es obligatorio") Long equipoId,
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotNull(message = "El tipo es obligatorio") TipoMantenimiento tipo,
        @NotBlank @Size(max = 500) String descripcion,
        @NotBlank @Size(max = 100) String tecnico
) { }