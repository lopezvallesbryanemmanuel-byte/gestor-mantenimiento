package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.CambioEstadoRequest;
import com.bryan.mantenimiento.dto.MantenimientoRequest;
import com.bryan.mantenimiento.dto.MantenimientoResponse;
import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.Mantenimiento;
import com.bryan.mantenimiento.entity.enums.EstadoMantenimiento;
import com.bryan.mantenimiento.entity.enums.TipoMantenimiento;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.exception.ReglaNegocioException;
import com.bryan.mantenimiento.repository.EquipoRepository;
import com.bryan.mantenimiento.repository.MantenimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MantenimientoServiceTest {

    @Mock MantenimientoRepository mantenimientoRepo;
    @Mock EquipoRepository equipoRepo;
    @InjectMocks MantenimientoService service;

    private Mantenimiento mantenimiento(EstadoMantenimiento estado) {
        var equipo = new Equipo();
        equipo.setId(1L);
        equipo.setNumeroSerie("HK-0001");

        var m = new Mantenimiento();
        m.setId(10L);
        m.setEquipo(equipo);
        m.setFecha(LocalDate.now());
        m.setTipo(TipoMantenimiento.PREVENTIVO);
        m.setDescripcion("Revisión general");
        m.setTecnico("Juan Pérez");
        m.setEstado(estado);
        return m;
    }

    private MantenimientoRequest request() {
        return new MantenimientoRequest(LocalDate.now(), TipoMantenimiento.CORRECTIVO,
                "Cambio de batería", "Ana López");
    }

    @Test
    void cambiarEstado_deTerminadoAPendiente_lanzaExcepcion() {
        when(mantenimientoRepo.findById(10L))
                .thenReturn(Optional.of(mantenimiento(EstadoMantenimiento.TERMINADO)));

        assertThrows(ReglaNegocioException.class,
                () -> service.cambiarEstado(10L, new CambioEstadoRequest(EstadoMantenimiento.PENDIENTE)));
        verify(mantenimientoRepo, never()).save(any());
    }

    @Test
    void cambiarEstado_dePendienteAEnProceso_funciona() {
        when(mantenimientoRepo.findById(10L))
                .thenReturn(Optional.of(mantenimiento(EstadoMantenimiento.PENDIENTE)));
        when(mantenimientoRepo.save(any(Mantenimiento.class))).thenAnswer(inv -> inv.getArgument(0));

        MantenimientoResponse resp = service.cambiarEstado(10L,
                new CambioEstadoRequest(EstadoMantenimiento.EN_PROCESO));

        assertEquals(EstadoMantenimiento.EN_PROCESO, resp.estado());
    }

    @Test
    void cambiarEstado_saltandoUnPaso_lanzaExcepcion() {
        when(mantenimientoRepo.findById(10L))
                .thenReturn(Optional.of(mantenimiento(EstadoMantenimiento.PENDIENTE)));

        assertThrows(ReglaNegocioException.class,
                () -> service.cambiarEstado(10L, new CambioEstadoRequest(EstadoMantenimiento.TERMINADO)));
        verify(mantenimientoRepo, never()).save(any());
    }

    @Test
    void cambiarEstado_conIdInexistente_lanzaNoEncontrado() {
        when(mantenimientoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.cambiarEstado(99L, new CambioEstadoRequest(EstadoMantenimiento.EN_PROCESO)));
    }

    @Test
    void actualizar_unMantenimientoTerminado_lanzaExcepcion() {
        when(mantenimientoRepo.findById(10L))
                .thenReturn(Optional.of(mantenimiento(EstadoMantenimiento.TERMINADO)));

        assertThrows(ReglaNegocioException.class, () -> service.actualizar(10L, request()));
        verify(mantenimientoRepo, never()).save(any());
    }

    @Test
    void crear_enUnEquipoInactivo_lanzaExcepcion() {
        var equipo = new Equipo();
        equipo.setId(1L);
        equipo.setActivo(false);
        when(equipoRepo.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(ReglaNegocioException.class, () -> service.crear(1L, request()));
        verify(mantenimientoRepo, never()).save(any());
    }
}