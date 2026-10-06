USE mantenimiento_db;

/*-- Equipos por sitio
SELECT s.nombre, COUNT(e.id) AS total_equipos
FROM sitio s LEFT JOIN equipo e ON e.sitio_id = s.id
GROUP BY s.nombre
ORDER BY total_equipos DESC;

-- Mantenimientos pendientes con datos del equipo y del sitio
SELECT m.id, m.fecha, e.numero_serie, e.tipo, s.nombre AS sitio
FROM mantenimiento m
JOIN equipo e ON e.id = m.equipo_id
JOIN sitio s ON s.id = e.sitio_id
WHERE m.estado = 'PENDIENTE'
ORDER BY m.fecha;*/

-- Prueba la restricción UNIQUE: esta inserción DEBE fallar con uq_equipo_serie
INSERT INTO equipo (sitio_id, tipo, marca, modelo, numero_serie, fecha_instalacion)
VALUES (1, 'CAMARA', 'X', 'Y', 'HK-0001', '2025-01-01');