DELETE FROM notificaciones;

INSERT INTO notificaciones (asunto, destino, mensaje, leida) VALUES
('Bienvenida', 'test@example.com', '¡Hola y bienvenido!', FALSE),
('Recordatorio', 'user@example.com', 'No olvides revisar tu bandeja.', TRUE);
