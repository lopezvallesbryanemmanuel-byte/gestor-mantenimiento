package com.bryan.mantenimiento.mapper;

import com.bryan.mantenimiento.dto.EquipoResponse;
import com.bryan.mantenimiento.dto.MantenimientoResponse;
import com.bryan.mantenimiento.dto.SitioResponse;
import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.Mantenimiento;
import com.bryan.mantenimiento.entity.Sitio;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static SitioResponse toResponse(Sitio s) {
        return new SitioResponse(
                s.getId(),
                s.getNombre(),
                s.getDireccion(),
                s.getCliente(),
                s.getCreadoEn());
    }

    public static EquipoResponse toResponse(Equipo e) {
        return new EquipoResponse(
                e.getId(),
                e.getSitio().getId(),
                e.getSitio().getNombre(),
                e.getTipo(),
                e.getMarca(),
                e.getModelo(),
                e.getNumeroSerie(),
                e.getFechaInstalacion(),
                e.getActivo());
    }

    public static MantenimientoResponse toResponse(Mantenimiento m) {
        return new MantenimientoResponse(
                m.getId(),
                m.getEquipo().getId(),
                m.getEquipo().getNumeroSerie(),
                m.getFecha(),
                m.getTipo(),
                m.getDescripcion(),
                m.getTecnico(),
                m.getEstado());
    }
}