package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.SitioRequest;
import com.bryan.mantenimiento.dto.SitioResponse;
import com.bryan.mantenimiento.entity.Sitio;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.exception.ReglaNegocioException;
import com.bryan.mantenimiento.mapper.EntityMapper;
import com.bryan.mantenimiento.repository.EquipoRepository;
import com.bryan.mantenimiento.repository.SitioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SitioService {

    private final SitioRepository sitioRepo;
    private final EquipoRepository equipoRepo;

    public SitioService(SitioRepository sitioRepo, EquipoRepository equipoRepo) {
        this.sitioRepo = sitioRepo;
        this.equipoRepo = equipoRepo;
    }

    @Transactional
    public SitioResponse crear(SitioRequest req) {
        Sitio sitio = new Sitio();
        copiar(req, sitio);
        return EntityMapper.toResponse(sitioRepo.save(sitio));
    }

    @Transactional(readOnly = true)
    public List<SitioResponse> listar() {
        return sitioRepo.findAll().stream()
                .map(s -> EntityMapper.toResponse(s))
                .toList();
    }

    @Transactional(readOnly = true)
    public SitioResponse obtener(Long id) {
        return EntityMapper.toResponse(buscar(id));
    }

    @Transactional
    public SitioResponse actualizar(Long id, SitioRequest req) {
        Sitio sitio = buscar(id);
        copiar(req, sitio);
        return EntityMapper.toResponse(sitioRepo.save(sitio));
    }

    // R5: un sitio solo se elimina si no tiene equipos
    @Transactional
    public void eliminar(Long id) {
        Sitio sitio = buscar(id);
        if (equipoRepo.existsBySitioId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el sitio " + id + " porque tiene equipos registrados");
        }
        sitioRepo.delete(sitio);
    }

    private Sitio buscar(Long id) {
        return sitioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sitio " + id + " no encontrado"));
    }

    private void copiar(SitioRequest req, Sitio sitio) {
        sitio.setNombre(req.nombre());
        sitio.setDireccion(req.direccion());
        sitio.setCliente(req.cliente());
    }
}