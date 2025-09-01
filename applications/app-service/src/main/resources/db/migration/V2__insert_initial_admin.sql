-- Inserta el usuario administrador inicial
INSERT INTO users (first_name, last_name, document_number, email, password, role)
VALUES (
    'Admin',
    'User',
    '12345',
    'admin@domain.com',
    -- La contraseña es 'admin123', pero ya está encriptada con BCrypt
    '$2a$10$v9w.gN05lq3C2z2aA.p21e9.3/.8y.i5tYpG9R.oK9J3p.2d.qW8O',
    'ADMIN'
);