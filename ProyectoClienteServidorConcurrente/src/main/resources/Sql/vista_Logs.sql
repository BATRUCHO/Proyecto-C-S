CREATE VIEW  vista_auditoria_completa AS
select
    l.id_log,
    l.id_usuario,
    IFNULL(CONCAT(u.nombre, ' ', u.apellido), 'Sistema / Anónimo') AS nombre_completo,
    IFNULL(r.nombre, 'N/A') AS rol_usuario,
    l.accion,
    l.detalles,
    l.fecha_hora
FROM logs_sistema l
left join usuarios u ON l.id_usuario = u.id_usuario
left join roles r ON u.id_rol = r.id_rol;