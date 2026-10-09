package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipoRequest(
        @NotNull(message = "El sitio es obligatorio") Long sitioId,
        @NotNull(message = "El tipo es obligatorio") TipoEquipo tipo,
        @NotBlank @Size(max = 50) String marca,
        @NotBlank @Size(max = 50) String modelo,
        @NotBlank @Size(max = 50) String numeroSerie,
        @NotNull @PastOrPresent LocalDate fechaInstalacion
) { }