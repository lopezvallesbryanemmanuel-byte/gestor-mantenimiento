package com.bryan.mantenimiento.controller;

import com.bryan.mantenimiento.dto.EquipoRequest;
import com.bryan.mantenimiento.dto.EquipoResponse;
import com.bryan.mantenimiento.dto.MantenimientoRequest;
import com.bryan.mantenimiento.dto.MantenimientoResponse;
import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import com.bryan.mantenimiento.service.EquipoService;
import com.bryan.mantenimiento.service.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService service;
    private final MantenimientoService mantenimientoService;

    public EquipoController(EquipoService service, MantenimientoService mantenimientoService) {
        this.service = service;
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    public List<EquipoResponse> listar(@RequestParam(required = false) Long sitioId,
                                       @RequestParam(required = false) TipoEquipo tipo) {
        return service.listar(sitioId, tipo);
    }

    @GetMapping("/{id}")
    public EquipoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipoResponse crear(@Valid @RequestBody EquipoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public EquipoResponse actualizar(@PathVariable Long id,
                                     @Valid @RequestBody EquipoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) {
        service.desactivar(id);
    }

    // Historial del equipo, del más reciente al más antiguo
    @GetMapping("/{id}/mantenimientos")
    public List<MantenimientoResponse> historial(@PathVariable Long id) {
        return mantenimientoService.listarPorEquipo(id);
    }

    // Registrar un mantenimiento a este equipo
    @PostMapping("/{id}/mantenimientos")
    @ResponseStatus(HttpStatus.CREATED)
    public MantenimientoResponse registrarMantenimiento(@PathVariable Long id,
                                                        @Valid @RequestBody MantenimientoRequest req) {
        return mantenimientoService.crear(id, req);
    }
}