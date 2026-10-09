package com.bryan.mantenimiento.dto;

import java.time.LocalDateTime;

public record SitioResponse(
        Long id,
        String nombre,
        String direccion,
        String cliente,
        LocalDateTime creadoEn
) { }