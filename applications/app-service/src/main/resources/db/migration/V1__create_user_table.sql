
    DROP TABLE IF EXISTS users;

    -- Crear la tabla de usuarios con el esquema que espera la aplicación
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        document_number VARCHAR(50) UNIQUE,
        first_name VARCHAR(100),
        last_name VARCHAR(100),
        email VARCHAR(255) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(50) NOT NULL DEFAULT 'CLIENT',
        birth_date DATE,
        address VARCHAR(255),
        phone_number VARCHAR(20),
        base_salary NUMERIC(15, 2),
        failed_login_attempts INTEGER DEFAULT 0,
        account_locked_until TIMESTAMP
    );


INSERT INTO users (id, first_name, last_name, email, password, role, birth_date, address, phone_number, base_salary, document_number, failed_login_attempts, account_locked_until) VALUES
(18, 'Milú', 'Agudelo', 'milu@example.com', '$2a$10$I648fx9uHkeZnamaf4qn7uvvttlQd4rnch3bPXXEaJpmpbU8TV/ym', 'CLIENT', '2021-10-20', 'Avenida Felina', '300000001', 3500000.00, '123456789', 0, NULL),
(16, 'Admin', 'User', 'admin@domain.com', '$2a$10$HJtAr2Mg9m8Vgm00VEzY0exCdJAvJW8KecjdamSzmo8vlk9V7o/lW', 'ADMIN', NULL, NULL, NULL, NULL, '12345', 0, NULL),
(19, 'Otto', 'Agudelo', 'totto@example.com', '$2a$10$qiws/B4VG0w9a7jslBs9wOFzvqFohMWpnAMQVFsy/tflYH1bpM2oS', 'ADVISER', '2021-10-20', 'Avenida Felina', '300000001', 3500000.00, '1234567890', 0, NULL),
(1, 'Ana', 'García', 'ana.garcia@example.com', 'unacontraseñasegura', 'CLIENT', '1995-05-15', NULL, NULL, 50000.00, NULL, 0, NULL),
(2, 'Carlos', 'Ramírez', 'carlos.ramirez@example.com', 'miClaveSegura123', 'CLIENT', '1990-11-20', 'Avenida Siempre Viva 742', '3109876543', 75000.00, NULL, 0, NULL),
(3, NULL, 'Válido', 'nombrevacio@example.com', 'password123', 'CLIENT', NULL, NULL, NULL, 50000.00, NULL, 0, NULL),
(4, 'Válido', NULL, 'apellidovacio@example.com', 'password123', 'CLIENT', NULL, NULL, NULL, 50000.00, NULL, 0, NULL),
(5, 'Usuario', 'Email', 'email-invalido', 'password123', 'CLIENT', NULL, NULL, NULL, 60000.00, NULL, 0, NULL), -- Nota: email-invalido como valor, no como tipo de dato
(6, 'Usuario', 'Salario', 'salarionulo@example.com', 'password123', 'CLIENT', NULL, NULL, NULL, NULL, NULL, 0, NULL),
(7, 'Usuario', 'Salario Alto', 'salarioalto@example.com', 'password123', 'CLIENT', NULL, NULL, NULL, 20000000.00, NULL, 0, NULL),
(8, 'Daniel', 'Agudelo', 'daniel.agudelo@example.com', '$2a$10$/Ngz6pA8gBuMX0z6V.9POeGZ8kzqYuvW6PMA9CBIqg3qC9bykZ2cu', 'CLIENT', '1990-05-15', 'Calle 10 # 43A-20', '3012345678', 4500000.00, NULL, 0, NULL),
(9, 'Otro Nombre', 'Otro Apellido', 'prueba.exitosa@example.com', '$2a$10$an.7ImPSLyQuyx9pegoF6uN8PLa8f3cXso2so8kGB0TWl5Wh6qDji', 'CLIENT', '1995-01-01', 'Otra Dirección', '3009876543', 3000000.00, NULL, 0, NULL),
(10, 'Daniel', 'Agudelo', 'danielito.prueba@example.com', '$2a$10$Huz85/xkxW3oNYLnXscFjulZsgU1jvojp7qRZRF0NOlCM7USdxr9m', 'CLIENT', '1990-05-15', 'Calle 10 # 43A-20', '3012345678', 4500000.00, NULL, 0, NULL),
(11, 'Carlos', 'Perez', 'carlos.perez@example.com', '$2a$10$8//VLTQqA.yRCWfOYAv/.eUz7fPe6a3YF68NCJKhfskc2ydGeka0C', 'CLIENT', '1995-10-20', 'Avenida Siempre Viva 742', '3001112233', 3500000.00, '1037654321', 0, NULL),
(13, 'Carlos', 'Perez', 'carlos1.perez@example.com', '$2a$10$UMIxcqDk1PvZsXEnsK3JZ./3Npc8FG2tW3HrQBRdQzmavLotMeWze', 'CLIENT', '1995-10-20', 'Avenida Siempre Viva 742', '3001112233', 3500000.00, '1037654322', 0, NULL),
(14, 'Carlos', 'Perez', 'carlos110.perez@example.com', '$2a$10$B4Z04Iq3IWqMjI8TwgY8kOW/sDa9KJ9zFuEqGvP0fm.KLm7pE7e', 'CLIENT', '1995-10-20', 'Avenida Siempre Viva 742', '3001112233', 3500000.00, '10376543221', 0, NULL),
(15, 'Carlos', 'Perez', 'carlos111.perez@example.com', '$2a$10$41ZwvQlUob5/RClp239mJuzAUB6eplnoixWubjvTpA8Zz.eMg1ksq', 'CLIENT', '1995-10-20', 'Avenida Siempre Viva 742', '3001112233', 3500000.00, '10376543223', 0, NULL),
(17, 'Usuario', 'DePrueba', 'test.user@example.com', '$2a$10$E.q1zYx1ZQZ1Z5Z5Z5Z5Z.Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z', 'CLIENT', NULL, NULL, NULL, NULL, '112233', 0, '2025-09-01 12:14:35.838'); -- Último campo de tus datos originales parecía una fecha/hora
-- Nota: el último '3' en el registro 17 no tiene un campo claro en el nuevo esquema, lo omití.

-- Ajustar la secuencia del ID para que las inserciones futuras continúen desde el máximo ID existente
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));