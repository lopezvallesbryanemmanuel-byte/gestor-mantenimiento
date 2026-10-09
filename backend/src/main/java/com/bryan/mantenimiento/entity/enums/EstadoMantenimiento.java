package com.bryan.mantenimiento.entity.enums;

public enum EstadoMantenimiento {
    PENDIENTE,
    EN_PROCESO,
    TERMINADO;

    /** Solo se avanza un paso: PENDIENTE -> EN_PROCESO -> TERMINADO. */
    public boolean puedeCambiarA(EstadoMantenimiento nuevo) {
        return switch (this) {
            case PENDIENTE -> nuevo == EN_PROCESO;
            case EN_PROCESO -> nuevo == TERMINADO;
            case TERMINADO -> false;
        };
    }
}