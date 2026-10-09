package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.EquipoRequest;
import com.bryan.mantenimiento.dto.EquipoResponse;
import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.Sitio;
import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.exception.ReglaNegocioException;
import com.bryan.mantenimiento.mapper.EntityMapper;
import com.bryan.mantenimiento.repository.EquipoRepository;
import com.bryan.mantenimiento.repository.SitioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepo;
    private final SitioRepository sitioRepo;

    public EquipoService(EquipoRepository equipoRepo, SitioRepository sitioRepo) {
        this.equipoRepo = equipoRepo; // inyección por constructor
        this.sitioRepo = sitioRepo;
    }

    @Transactional
    public EquipoResponse crear(EquipoRequest req) {
        // R1: número de serie único
        if (equipoRepo.existsByNumeroSerie(req.numeroSerie())) {
            throw new ReglaNegocioException(
                    "Ya existe un equipo con el número de serie " + req.numeroSerie());
        }
        Sitio sitio = buscarSitio(req.sitioId());

        Equipo equipo = new Equipo();
        equipo.setSitio(sitio);
        equipo.setTipo(req.tipo());
        equipo.setMarca(req.marca());
        equipo.setModelo(req.modelo());
        equipo.setNumeroSerie(req.numeroSerie());
        equipo.setFechaInstalacion(req.fechaInstalacion());
        return EntityMapper.toResponse(equipoRepo.save(equipo));
    }

    @Transactional(readOnly = true)
    public List<EquipoResponse> listar(Long sitioId, TipoEquipo tipo) {
        List<Equipo> equipos;
        if (sitioId != null && tipo != null) {
            equipos = equipoRepo.findBySitioIdAndTipo(sitioId, tipo);
        } else if (sitioId != null) {
            equipos = equipoRepo.findBySitioId(sitioId);
        } else if (tipo != null) {
            equipos = equipoRepo.findByTipo(tipo);
        } else {
            equipos = equipoRepo.findAll();
        }
        return equipos.stream().map(e -> EntityMapper.toResponse(e)).toList();
    }

    @Transactional(readOnly = true)
    public EquipoResponse obtener(Long id) {
        return EntityMapper.toResponse(buscar(id));
    }

    @Transactional
    public EquipoResponse actualizar(Long id, EquipoRequest req) {
        Equipo equipo = buscar(id);

        // R1: solo se valida si el número de serie cambió
        if (!equipo.getNumeroSerie().equals(req.numeroSerie())
                && equipoRepo.existsByNumeroSerie(req.numeroSerie())) {
            throw new ReglaNegocioException(
                    "Ya existe un equipo con el número de serie " + req.numeroSerie());
        }
        if (!equipo.getSitio().getId().equals(req.sitioId())) {
            equipo.setSitio(buscarSitio(req.sitioId()));
        }
        equipo.setTipo(req.tipo());
        equipo.setMarca(req.marca());
        equipo.setModelo(req.modelo());
        equipo.setNumeroSerie(req.numeroSerie());
        equipo.setFechaInstalacion(req.fechaInstalacion());
        return EntityMapper.toResponse(equipoRepo.save(equipo));
    }

    // R5: borrado lógico, el equipo conserva su historial
    @Transactional
    public void desactivar(Long id) {
        Equipo equipo = buscar(id);
        equipo.setActivo(false);
        equipoRepo.save(equipo);
    }

    private Equipo buscar(Long id) {
        return equipoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo " + id + " no encontrado"));
    }

    private Sitio buscarSitio(Long id) {
        return sitioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sitio " + id + " no encontrado"));
    }
}