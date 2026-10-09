package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.CambioEstadoRequest;
import com.bryan.mantenimiento.dto.MantenimientoRequest;
import com.bryan.mantenimiento.dto.MantenimientoResponse;
import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.Mantenimiento;
import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.exception.ReglaNegocioException;
import com.bryan.mantenimiento.mapper.EntityMapper;
import com.bryan.mantenimiento.repository.EquipoRepository;
import com.bryan.mantenimiento.repository.MantenimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepo;
    private final EquipoRepository equipoRepo;

    public MantenimientoService(MantenimientoRepository mantenimientoRepo,
                                EquipoRepository equipoRepo) {
        this.mantenimientoRepo = mantenimientoRepo;
        this.equipoRepo = equipoRepo;
    }

    @Transactional
    public MantenimientoResponse crear(MantenimientoRequest req) {
        Equipo equipo = buscarEquipoActivo(req.equipoId()); // R2

        Mantenimiento m = new Mantenimiento();
        m.setEquipo(equipo);
        m.setFecha(req.fecha());
        m.setTipo(req.tipo());
        m.setDescripcion(req.descripcion());
        m.setTecnico(req.tecnico());
        // el estado inicial es PENDIENTE (valor por defecto de la entidad)
        return EntityMapper.toResponse(mantenimientoRepo.save(m));
    }

    @Transactional(readOnly = true)
    public List<MantenimientoResponse> listar(Long equipoId, EstadoMantenimiento estado) {
        List<Mantenimiento> lista;
        if (equipoId != null) {
            lista = mantenimientoRepo.findByEquipoIdOrderByFechaDesc(equipoId);
            if (estado != null) {
                lista = lista.stream().filter(m -> m.getEstado() == estado).toList();
            }
        } else if (estado != null) {
            lista = mantenimientoRepo.findByEstado(estado);
        } else {
            lista = mantenimientoRepo.findAll();
        }
        return lista.stream().map(m -> EntityMapper.toResponse(m)).toList();
    }

    @Transactional(readOnly = true)
    public MantenimientoResponse obtener(Long id) {
        return EntityMapper.toResponse(buscar(id));
    }

    @Transactional
    public MantenimientoResponse actualizar(Long id, MantenimientoRequest req) {
        Mantenimiento m = buscar(id);

        // R4: un mantenimiento terminado ya no se edita
        if (m.getEstado() == EstadoMantenimiento.TERMINADO) {
            throw new ReglaNegocioException(
                    "El mantenimiento " + id + " ya está TERMINADO y no se puede editar");
        }
        if (!m.getEquipo().getId().equals(req.equipoId())) {
            m.setEquipo(buscarEquipoActivo(req.equipoId())); // R2 sobre el equipo nuevo
        }
        m.setFecha(req.fecha());
        m.setTipo(req.tipo());
        m.setDescripcion(req.descripcion());
        m.setTecnico(req.tecnico());
        return EntityMapper.toResponse(mantenimientoRepo.save(m));
    }

    @Transactional
    public MantenimientoResponse cambiarEstado(Long id, CambioEstadoRequest req) {
        Mantenimiento m = buscar(id);

        // R3: solo se avanza un paso a la vez
        if (!m.getEstado().puedeCambiarA(req.estado())) {
            throw new ReglaNegocioException(
                    "Transición no permitida: " + m.getEstado() + " -> " + req.estado());
        }
        m.setEstado(req.estado());
        return EntityMapper.toResponse(mantenimientoRepo.save(m));
    }

    private Mantenimiento buscar(Long id) {
        return mantenimientoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento " + id + " no encontrado"));
    }

    private Equipo buscarEquipoActivo(Long equipoId) {
        Equipo equipo = equipoRepo.findById(equipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo " + equipoId + " no encontrado"));
        if (!Boolean.TRUE.equals(equipo.getActivo())) {
            throw new ReglaNegocioException(
                    "El equipo " + equipoId + " está inactivo y no admite mantenimientos");
        }
        return equipo;
    }
}