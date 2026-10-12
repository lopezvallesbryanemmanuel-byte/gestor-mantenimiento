package com.bryan.mantenimiento.dto;

import com.bryan.mantenimiento.entity.enums.Rol;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        Rol rol
) { }
