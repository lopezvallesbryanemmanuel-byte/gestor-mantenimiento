package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoRequest(
        @NotNull(message = "El estado es obligatorio") EstadoMantenimiento estado
) { }