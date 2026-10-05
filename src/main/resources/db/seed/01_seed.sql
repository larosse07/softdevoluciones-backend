CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- USUARIOS
-- Contraseña de las cuentas demo:
-- Cliente123!
-- Operador123!
-- Admin123!
-- ============================================================

INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Cliente Demo', 'cliente@softdevoluciones.com',
       crypt('Cliente123!', gen_salt('bf')), 'CLIENTE'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios
    WHERE email = 'cliente@softdevoluciones.com'
);

INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Operador Demo', 'operador@softdevoluciones.com',
       crypt('Operador123!', gen_salt('bf')), 'OPERADOR'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios
    WHERE email = 'operador@softdevoluciones.com'
);

INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Administrador Demo', 'admin@softdevoluciones.com',
       crypt('Admin123!', gen_salt('bf')), 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios
    WHERE email = 'admin@softdevoluciones.com'
);


-- ============================================================
-- PRODUCTOS
-- ============================================================

INSERT INTO productos (nombre, descripcion, activo)
SELECT 'Laptop Lenovo IdeaPad',
       'Laptop Lenovo para uso personal y empresarial',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM productos
    WHERE nombre = 'Laptop Lenovo IdeaPad'
);

INSERT INTO productos (nombre, descripcion, activo)
SELECT 'Mouse Logitech M185',
       'Mouse inalámbrico Logitech',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM productos
    WHERE nombre = 'Mouse Logitech M185'
);

INSERT INTO productos (nombre, descripcion, activo)
SELECT 'Teclado Logitech K380',
       'Teclado inalámbrico compacto',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM productos
    WHERE nombre = 'Teclado Logitech K380'
);

INSERT INTO productos (nombre, descripcion, activo)
SELECT 'Monitor LG 24 pulgadas',
       'Monitor Full HD de 24 pulgadas',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM productos
    WHERE nombre = 'Monitor LG 24 pulgadas'
);


-- ============================================================
-- COMPRA DEL CLIENTE DEMO
-- ============================================================

INSERT INTO compras (fecha, total, estado, usuario_id)
SELECT
    '2026-09-25 10:30:00',
    2679.97,
    'COMPLETADA',
    u.id
FROM usuarios u
WHERE u.email = 'cliente@softdevoluciones.com'
  AND NOT EXISTS (
      SELECT 1
      FROM compras c
      WHERE c.usuario_id = u.id
        AND c.fecha = '2026-09-25 10:30:00'
  );


-- ============================================================
-- DETALLE DE COMPRA
-- ============================================================

INSERT INTO detalle_compras
    (cantidad, precio_unitario, compra_id, producto_id)
SELECT
    1,
    2399.99,
    c.id,
    p.id
FROM compras c
JOIN usuarios u ON u.id = c.usuario_id
JOIN productos p ON p.nombre = 'Laptop Lenovo IdeaPad'
WHERE u.email = 'cliente@softdevoluciones.com'
  AND c.fecha = '2026-09-25 10:30:00'
  AND NOT EXISTS (
      SELECT 1
      FROM detalle_compras dc
      WHERE dc.compra_id = c.id
        AND dc.producto_id = p.id
  );


INSERT INTO detalle_compras
    (cantidad, precio_unitario, compra_id, producto_id)
SELECT
    2,
    89.99,
    c.id,
    p.id
FROM compras c
JOIN usuarios u ON u.id = c.usuario_id
JOIN productos p ON p.nombre = 'Mouse Logitech M185'
WHERE u.email = 'cliente@softdevoluciones.com'
  AND c.fecha = '2026-09-25 10:30:00'
  AND NOT EXISTS (
      SELECT 1
      FROM detalle_compras dc
      WHERE dc.compra_id = c.id
        AND dc.producto_id = p.id
  );


INSERT INTO detalle_compras
    (cantidad, precio_unitario, compra_id, producto_id)
SELECT
    1,
    99.99,
    c.id,
    p.id
FROM compras c
JOIN usuarios u ON u.id = c.usuario_id
JOIN productos p ON p.nombre = 'Teclado Logitech K380'
WHERE u.email = 'cliente@softdevoluciones.com'
  AND c.fecha = '2026-09-25 10:30:00'
  AND NOT EXISTS (
      SELECT 1
      FROM detalle_compras dc
      WHERE dc.compra_id = c.id
        AND dc.producto_id = p.id
  );

INSERT INTO detalle_compras
    (cantidad, precio_unitario, compra_id, producto_id)
SELECT
    1,
    0.01,
    c.id,
    p.id
FROM compras c
JOIN usuarios u ON u.id = c.usuario_id
JOIN productos p ON p.nombre = 'Monitor LG 24 pulgadas'
WHERE u.email = 'cliente@softdevoluciones.com'
  AND c.fecha = '2026-09-25 10:30:00'
  AND NOT EXISTS (
      SELECT 1
      FROM detalle_compras dc
      WHERE dc.compra_id = c.id
        AND dc.producto_id = p.id
  );