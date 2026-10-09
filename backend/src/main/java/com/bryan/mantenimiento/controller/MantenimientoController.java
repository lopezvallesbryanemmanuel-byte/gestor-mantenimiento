package com.bryan.mantenimiento.controller;

import com.bryan.mantenimiento.dto.CambioEstadoRequest;
import com.bryan.mantenimiento.dto.MantenimientoRequest;
import com.bryan.mantenimiento.dto.MantenimientoResponse;
import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import com.bryan.mantenimiento.service.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService service;

    public MantenimientoController(MantenimientoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MantenimientoResponse> buscar(
            @RequestParam(required = false) EstadoMantenimiento estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.filtrar(estado, desde, hasta);
    }

    @PutMapping("/{id}")
    public MantenimientoResponse actualizar(@PathVariable Long id,
                                            @Valid @RequestBody MantenimientoRequest req) {
        return service.actualizar(id, req);
    }

    @PatchMapping("/{id}/estado")
    public MantenimientoResponse cambiarEstado(@PathVariable Long id,
                                                @Valid @RequestBody CambioEstadoRequest req) {
        return service.cambiarEstado(id, req);
    }
}