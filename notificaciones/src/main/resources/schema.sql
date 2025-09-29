CREATE TABLE IF NOT EXISTS notificaciones (
  id IDENTITY PRIMARY KEY,
  asunto  VARCHAR(255)   NOT NULL,
  destino VARCHAR(255)   NOT NULL,
  mensaje VARCHAR(2000)  NOT NULL,
  leida   BOOLEAN        NOT NULL
);
