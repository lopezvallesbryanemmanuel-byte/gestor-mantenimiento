USE mantenimiento_db;
GO

CREATE TABLE sitio (
    id        BIGINT IDENTITY(1,1) NOT NULL,
    nombre    VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NULL,
    cliente   VARCHAR(100) NOT NULL,
    creado_en DATETIME2 NOT NULL CONSTRAINT df_sitio_creado DEFAULT SYSDATETIME(),
    CONSTRAINT pk_sitio PRIMARY KEY (id)
);

CREATE TABLE equipo (
    id                BIGINT IDENTITY(1,1) NOT NULL,
    sitio_id          BIGINT NOT NULL,
    tipo              VARCHAR(30) NOT NULL,
    marca             VARCHAR(50) NOT NULL,
    modelo            VARCHAR(50) NOT NULL,
    numero_serie      VARCHAR(50) NOT NULL,
    fecha_instalacion DATE NOT NULL,
    activo            BIT NOT NULL CONSTRAINT df_equipo_activo DEFAULT 1,
    CONSTRAINT pk_equipo PRIMARY KEY (id),
    CONSTRAINT fk_equipo_sitio FOREIGN KEY (sitio_id) REFERENCES sitio(id),
    CONSTRAINT uq_equipo_serie UNIQUE (numero_serie),
    CONSTRAINT ck_equipo_tipo CHECK (tipo IN ('CAMARA','PANEL_ALARMA','LECTOR_ACCESO',
                                              'DETECTOR_HUMO','OTRO'))
);

CREATE TABLE mantenimiento (
    id          BIGINT IDENTITY(1,1) NOT NULL,
    equipo_id   BIGINT NOT NULL,
    fecha       DATE NOT NULL,
    tipo        VARCHAR(20) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    tecnico     VARCHAR(100) NOT NULL,
    estado      VARCHAR(20) NOT NULL CONSTRAINT df_mant_estado DEFAULT 'PENDIENTE',
    CONSTRAINT pk_mantenimiento PRIMARY KEY (id),
    CONSTRAINT fk_mant_equipo FOREIGN KEY (equipo_id) REFERENCES equipo(id) ON DELETE CASCADE,
    CONSTRAINT ck_mant_tipo CHECK (tipo IN ('PREVENTIVO','CORRECTIVO')),
    CONSTRAINT ck_mant_estado CHECK (estado IN ('PENDIENTE','EN_PROCESO','TERMINADO'))
);

CREATE TABLE usuario (
    id       BIGINT IDENTITY(1,1) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,  -- aquí se guarda el HASH BCrypt, nunca la contraseña
    nombre   VARCHAR(100) NOT NULL,
    rol      VARCHAR(20) NOT NULL,
    activo   BIT NOT NULL CONSTRAINT df_usuario_activo DEFAULT 1,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_username UNIQUE (username),
    CONSTRAINT ck_usuario_rol CHECK (rol IN ('ADMIN','TECNICO'))
);

-- Índices para las búsquedas más comunes
CREATE INDEX ix_equipo_sitio ON equipo(sitio_id);
CREATE INDEX ix_mantenimiento_equipo ON mantenimiento(equipo_id);
CREATE INDEX ix_mantenimiento_estado ON mantenimiento(estado);
GO