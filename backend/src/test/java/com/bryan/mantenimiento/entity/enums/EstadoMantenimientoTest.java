package com.bryan.mantenimiento.entity.enums;

import org.junit.jupiter.api.Test;

import static com.bryan.mantenimiento.entity.enums.EstadoMantenimiento.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadoMantenimientoTest {

    @Test
    void avanzaUnPasoALaVez() {
        assertTrue(PENDIENTE.puedeCambiarA(EN_PROCESO));
        assertTrue(EN_PROCESO.puedeCambiarA(TERMINADO));
    }

    @Test
    void noPermiteSaltarRegresarNiRepetir() {
        assertFalse(PENDIENTE.puedeCambiarA(TERMINADO));
        assertFalse(EN_PROCESO.puedeCambiarA(PENDIENTE));
        assertFalse(PENDIENTE.puedeCambiarA(PENDIENTE));
        assertFalse(TERMINADO.puedeCambiarA(PENDIENTE));
        assertFalse(TERMINADO.puedeCambiarA(EN_PROCESO));
    }
}