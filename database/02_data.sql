USE mantenimiento_db;
GO

INSERT INTO sitio (nombre, direccion, cliente) VALUES
('Sucursal Centro', 'Av. Obregón 123, Culiacán', 'Farmacias del Valle'),
('Planta Norte', 'Carretera a Imala km 4', 'Empacadora Sinaloa'),
('Oficinas Tres Ríos', 'Blvd. Pedro Infante 2500', 'Grupo Financiero Pacífico');

INSERT INTO equipo (sitio_id, tipo, marca, modelo, numero_serie, fecha_instalacion) VALUES
(1, 'CAMARA', 'Hikvision', 'DS-2CD2043', 'HK-0001', '2025-03-10'),
(1, 'PANEL_ALARMA', 'DSC', 'PowerSeries', 'DSC-1001', '2025-03-11'),
(2, 'LECTOR_ACCESO', 'ZKTeco', 'ProFace X', 'ZK-5001', '2025-06-02'),
(1, 'DETECTOR_HUMO', 'Bosch', 'FAP-420', 'BS-2001', '2025-04-05'),
(2, 'CAMARA', 'Dahua', 'IPC-HFW2431', 'DH-0001', '2025-06-03'),
(2, 'PANEL_ALARMA', 'Honeywell', 'Vista-20P', 'HW-3001', '2025-06-04'),
(3, 'CAMARA', 'Axis', 'M3086-V', 'AX-0001', '2025-09-15'),
(3, 'LECTOR_ACCESO', 'HID', 'iCLASS SE R10', 'HID-7001', '2025-09-16'),
(3, 'DETECTOR_HUMO', 'Honeywell', 'FSP-851', 'HW-4001', '2025-09-17'),
(2, 'OTRO', 'APC', 'Smart-UPS 1500', 'APC-9001', '2025-07-20');

INSERT INTO mantenimiento (equipo_id, fecha, tipo, descripcion, tecnico, estado) VALUES
(1, '2026-01-15', 'PREVENTIVO', 'Limpieza de lente y ajuste de enfoque', 'Juan Pérez', 'TERMINADO'),
(2, '2026-02-20', 'CORRECTIVO', 'Reemplazo de batería de respaldo', 'Ana López', 'EN_PROCESO'),
(3, '2026-03-05', 'PREVENTIVO', 'Actualización de firmware y limpieza del lector', 'Juan Pérez', 'TERMINADO'),
(4, '2026-03-18', 'PREVENTIVO', 'Prueba de sensibilidad y limpieza del detector', 'Carlos Ramírez', 'TERMINADO'),
(5, '2026-04-10', 'CORRECTIVO', 'Reemplazo de fuente de poder por falla intermitente', 'Ana López', 'TERMINADO'),
(6, '2026-04-22', 'PREVENTIVO', 'Revisión de sirenas y comunicador', 'Carlos Ramírez', 'TERMINADO'),
(7, '2026-05-14', 'PREVENTIVO', 'Limpieza de domo y verificación de grabación', 'Juan Pérez', 'TERMINADO'),
(8, '2026-06-02', 'CORRECTIVO', 'Cambio de lector por falla de lectura de tarjetas', 'Ana López', 'TERMINADO'),
(9, '2026-07-08', 'PREVENTIVO', 'Prueba funcional del detector de humo', 'Carlos Ramírez', 'TERMINADO'),
(10, '2026-08-19', 'CORRECTIVO', 'Reemplazo de batería del UPS', 'Juan Pérez', 'TERMINADO'),
(1, '2026-09-10', 'CORRECTIVO', 'Reemplazo de cable de red dañado', 'Ana López', 'EN_PROCESO'),
(5, '2026-10-15', 'PREVENTIVO', 'Limpieza de lente y revisión del ángulo de visión', 'Carlos Ramírez', 'PENDIENTE'),
(3, '2026-10-28', 'PREVENTIVO', 'Revisión de cerradura y fuente del lector', 'Juan Pérez', 'PENDIENTE'),
(7, '2026-11-05', 'PREVENTIVO', 'Actualización de firmware de la cámara', 'Ana López', 'PENDIENTE'),
(10, '2026-11-20', 'PREVENTIVO', 'Prueba de autonomía del UPS', 'Carlos Ramírez', 'PENDIENTE');
GO