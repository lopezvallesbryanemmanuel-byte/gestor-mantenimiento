package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.EquipoRequest;
import com.bryan.mantenimiento.dto.EquipoResponse;
import com.bryan.mantenimiento.entity.Equipo;
import com.bryan.mantenimiento.entity.Sitio;
import com.bryan.mantenimiento.entity.enums.TipoEquipo;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.exception.ReglaNegocioException;
import com.bryan.mantenimiento.repository.EquipoRepository;
import com.bryan.mantenimiento.repository.SitioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock EquipoRepository equipoRepo;
    @Mock SitioRepository sitioRepo;
    @InjectMocks EquipoService service;

    private EquipoRequest request(Long sitioId, String serie) {
        return new EquipoRequest(sitioId, TipoEquipo.CAMARA, "Hikvision", "X1",
                serie, LocalDate.now());
    }

    @Test
    void crear_conNumeroSerieDuplicado_lanzaExcepcion() {
        var req = request(1L, "HK-0001");
        when(equipoRepo.existsByNumeroSerie("HK-0001")).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> service.crear(req));
        verify(equipoRepo, never()).save(any());
    }

    @Test
    void crear_conSitioInexistente_lanzaExcepcion() {
        var req = request(99L, "HK-0002");
        when(equipoRepo.existsByNumeroSerie("HK-0002")).thenReturn(false);
        when(sitioRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.crear(req));
        verify(equipoRepo, never()).save(any());
    }

    @Test
    void crear_conDatosValidos_guardaElEquipoActivo() {
        var sitio = new Sitio();
        sitio.setId(1L);
        sitio.setNombre("Sucursal Centro");
        when(equipoRepo.existsByNumeroSerie("HK-0003")).thenReturn(false);
        when(sitioRepo.findById(1L)).thenReturn(Optional.of(sitio));
        when(equipoRepo.save(any(Equipo.class))).thenAnswer(inv -> inv.getArgument(0));

        EquipoResponse resp = service.crear(request(1L, "HK-0003"));

        assertEquals("HK-0003", resp.numeroSerie());
        assertEquals("Sucursal Centro", resp.sitioNombre());
        assertTrue(resp.activo());
    }
}