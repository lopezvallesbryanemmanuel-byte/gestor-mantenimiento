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

import java.time.LocalDate;
import java.util.Comparator;
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
    public MantenimientoResponse crear(Long equipoId, MantenimientoRequest req) {
        Equipo equipo = buscarEquipoActivo(equipoId); // R2

        Mantenimiento m = new Mantenimiento();
        m.setEquipo(equipo);
        m.setFecha(req.fecha());
        m.setTipo(req.tipo());
        m.setDescripcion(req.descripcion());
        m.setTecnico(req.tecnico());
        // el estado inicial es PENDIENTE (valor por defecto de la entidad)
        return EntityMapper.toResponse(mantenimientoRepo.save(m));
    }

    // Historial de un equipo, del más reciente al más antiguo
    @Transactional(readOnly = true)
    public List<MantenimientoResponse> listarPorEquipo(Long equipoId) {
        if (!equipoRepo.existsById(equipoId)) { // R6
            throw new RecursoNoEncontradoException("Equipo " + equipoId + " no encontrado");
        }
        return mantenimientoRepo.findByEquipoIdOrderByFechaDesc(equipoId).stream()
                .map(m -> EntityMapper.toResponse(m))
                .toList();
    }

    // Búsqueda por estado y rango de fechas; todos los filtros son opcionales
    @Transactional(readOnly = true)
    public List<MantenimientoResponse> filtrar(EstadoMantenimiento estado,
                                               LocalDate desde, LocalDate hasta) {
        List<Mantenimiento> lista;
        if (desde == null && hasta == null) {
            lista = (estado != null) ? mantenimientoRepo.findByEstado(estado)
                                     : mantenimientoRepo.findAll();
        } else {
            LocalDate d = (desde != null) ? desde : LocalDate.of(1900, 1, 1);
            LocalDate h = (hasta != null) ? hasta : LocalDate.of(9999, 12, 31);
            lista = (estado != null) ? mantenimientoRepo.findByEstadoAndFechaBetween(estado, d, h)
                                     : mantenimientoRepo.findByFechaBetween(d, h);
        }
        return lista.stream()
                .sorted(Comparator.comparing(Mantenimiento::getFecha).reversed())
                .map(m -> EntityMapper.toResponse(m))
                .toList();
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