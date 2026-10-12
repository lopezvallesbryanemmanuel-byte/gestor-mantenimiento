package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.Rol;

public record LoginResponse(
        String token,
        String tipo,
        long expiraEnMs,
        String username,
        String nombre,
        Rol rol
) { }
