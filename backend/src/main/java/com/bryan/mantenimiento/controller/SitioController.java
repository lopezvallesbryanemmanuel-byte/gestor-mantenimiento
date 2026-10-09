package com.bryan.mantenimiento.controller;

import com.bryan.mantenimiento.dto.SitioRequest;
import com.bryan.mantenimiento.dto.SitioResponse;
import com.bryan.mantenimiento.service.SitioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sitios")
public class SitioController {

    private final SitioService service;

    public SitioController(SitioService service) {
        this.service = service;
    }

    @GetMapping
    public List<SitioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public SitioResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SitioResponse crear(@Valid @RequestBody SitioRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public SitioResponse actualizar(@PathVariable Long id,
                                    @Valid @RequestBody SitioRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}