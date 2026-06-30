-- -----------------------------------------------------
-- Initialization script: database.sql
-- -----------------------------------------------------

CREATE DATABASE IF NOT EXISTS db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db;

-- 1. Table: users
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- 2. Table: roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- 3. Table: departments
CREATE TABLE IF NOT EXISTS departments (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Table: user_roles
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 5. Table: projects
CREATE TABLE IF NOT EXISTS projects (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  start_date DATE NOT NULL,
  end_date DATE DEFAULT NULL,
  department_id BIGINT NOT NULL,
  CONSTRAINT fk_proj_dept FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);
-- Table: project_users
CREATE TABLE IF NOT EXISTS project_users (
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_pu_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_pu_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 6. Table: timesheets
CREATE TABLE IF NOT EXISTS timesheets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    hours DECIMAL(4,2) NOT NULL,
    comment TEXT,
    global_comment TEXT NULL,
    week_id VARCHAR(10) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ts_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- 7. Table: user_departments
CREATE TABLE IF NOT EXISTS user_departments (
	user_id BIGINT NOT NULL,
	department_id BIGINT NOT NULL,
	PRIMARY KEY (user_id, department_id),
	CONSTRAINT fk_ud_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT fk_ud_dept FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- DATA INSERTION
-- -----------------------------------------------------

-- Users (Password: "123")
INSERT INTO users (first_name, last_name, email, password, registration_date, status) VALUES
('Tester','Number One', 'test1@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Tester','Numer Two', 'test2@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Pepe','Rojas', 'pepito@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Liam', 'Smith', 'liam.smith@example.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Emma', 'Johnson', 'emma.johnson@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Hiroshi', 'Tanaka', 'h.tanaka@yahoo.co.jp', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Elena', 'Rodriguez', 'elena.rod@outlook.es', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Kwame', 'Osei', 'k.osei@ghana.net', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Sofia', 'Müller', 'sofia.muller@web.de', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Luca', 'Rossi', 'luca.rossi@libero.it', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Anya', 'Ivanova', 'anya.ivan@mail.ru', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Mateo', 'Fernandez', 'mateo.f@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Chloe', 'Dubois', 'c.dubois@orange.fr', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Arjun', 'Patel', 'arjun.patel@rediff.in', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Fatima', 'Al-Sayed', 'fatima.as@email.ae', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Oliver', 'Brown', 'obrown@hotmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Mei', 'Lin', 'mei.lin@qq.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Carlos', 'Silva', 'csilva@terra.com.br', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Ingrid', 'Johansson', 'ingrid.j@telia.se', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Diego', 'Martinez', 'diego.m@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Yuki', 'Sato', 'y.sato@docomo.ne.jp', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Sarah', 'Williams', 'sarah.w@outlook.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Hans', 'Schmidt', 'h.schmidt@t-online.de', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Amara', 'Diallo', 'a.diallo@senemail.sn', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Javier', 'Lopez', 'javi.lopez@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Olga', 'Petrova', 'olga.p@yandex.ru', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Liam', 'Wilson', 'lwilson@me.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Kenji', 'Yamamoto', 'k.yama@jp-net.jp', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Maria', 'Garcia', 'm.garcia@mail.es', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Erik', 'Nielsen', 'erik.n@dansk.dk', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Camila', 'Torres', 'cami.torres@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Pierre', 'Lefebvre', 'p.lefebvre@free.fr', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Ananya', 'Sharma', 'ananya.s@email.in', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Thomas', 'Clark', 'tclark@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Zainab', 'Hassan', 'z.hassan@email.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Alejandro', 'Diaz', 'ale.diaz@yahoo.es', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Min-jun', 'Kim', 'minjun.kim@naver.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Anna', 'Wozniak', 'a.wozniak@pl.pl', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Lucas', 'Moreira', 'lucas.moreira@uol.br', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Freja', 'Hansen', 'freja.h@mail.dk', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Oscar', 'Vargas', 'o.vargas@outlook.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('Isabelle', 'Martin', 'isabelle.m@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('David', 'Cohen', 'd.cohen@email.il', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE');

-- Roles
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

-- User roles
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1),
(2, 1),
(3, 2),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 1),
(12, 1),
(13, 1),
(14, 1),
(15, 1),
(16, 1),
(17, 1),
(18, 1),
(19, 1),
(20, 1),
(21, 1),
(22, 1),
(23, 1),
(24, 1),
(25, 1),
(26, 1),
(27, 1),
(28, 1),
(29, 1),
(30, 1),
(31, 1),
(32, 1),
(33, 1),
(34, 1),
(35, 1),
(36, 1),
(37, 1),
(38, 1),
(39, 1),
(40, 1);

-- Departments
INSERT INTO departments (name) VALUES
('Software Engineering'),
('Quality Assurance'),
('Product Management'),
('Design & UX'),
('Infrastructure & DevOps'),
('Data Science & AI'),
('Cybersecurity'),
('Customer Success'),
('Human Resources');

-- User departments
INSERT INTO user_departments (user_id, department_id) VALUES 
(1, 1), (1, 2),
(2, 1), (2, 5),
(3, 1), (3, 2), (3, 3), (3, 4),(3, 5), (3, 6), (3, 7), (3, 8), (3, 9),
(4, 1), 
(5, 2), 
(6, 3), 
(7, 4), 
(8, 5), 
(9, 6), 
(10, 7), 
(11, 8), 
(12, 9),
(13, 1), 
(14, 2), 
(15, 3), 
(16, 4), 
(17, 5), 
(18, 6), 
(19, 7), 
(20, 8), 
(21, 9),
(22, 1), 
(23, 2), 
(24, 3), 
(25, 4), 
(26, 5), 
(27, 6), 
(28, 7), 
(29, 8), 
(30, 9),
(31, 1), 
(32, 2), 
(33, 3), 
(34, 4), 
(35, 5), 
(36, 6), 
(37, 7), 
(38, 8), 
(39, 9),
(40, 1);

-- Projects
INSERT INTO projects (name, description, start_date, end_date, department_id) VALUES
('Cloud Migration', 'Migrating legacy servers to AWS', '2026-07-01', '2026-10-01', 5),
('AI Chatbot Beta', 'Integration of NLP for support', '2026-07-05', '2026-11-05', 6),
('Security Audit', 'Comprehensive vulnerability testing', '2026-07-10', '2026-09-10', 7),
('UI Redesign Phase 1', 'Modernizing user dashboard', '2026-07-15', '2026-12-15', 4),
('Mobile App Refactor', 'Transition to Flutter framework', '2026-07-20', '2026-12-20', 1),
('Customer Feedback API', 'Real-time survey integration', '2026-08-01', '2026-10-01', 8),
('Team Building Offsite', 'Corporate culture enhancement', '2026-08-10', '2026-08-15', 9),
('Data Warehouse Setup', 'ETL pipelines configuration', '2026-08-15', '2026-11-15', 6),
('Automated Testing Suite', 'Selenium coverage expansion', '2026-09-01', '2026-12-01', 2),
('Internal Wiki', 'Centralized docs for Devs', '2026-07-01', '2026-09-01', 1),
('Inventory Automation', 'IoT sensors integration', '2026-07-12', '2026-10-12', 5),
('Q3 Performance Review', 'Annual HR process update', '2026-09-01', '2026-10-01', 9),
('Payment Gateway V2', 'Stripe API integration', '2026-07-20', '2026-11-20', 1),
('User Access Control', 'Identity management upgrade', '2026-08-05', '2026-10-05', 7),
('E-commerce Checkout', 'Streamlining shopping cart', '2026-08-20', '2026-11-20', 1),
('DevOps Dashboard', 'Observability via Grafana', '2026-09-01', '2026-12-01', 5),
('Marketing Automation', 'CRM sync with social leads', '2026-09-15', '2026-11-15', 3),
('QA Load Testing', 'Stress testing core services', '2026-07-10', '2026-08-10', 2),
('Legacy System Deprecation', 'Cleaning up old codebase', '2026-09-01', '2026-12-30', 1),
('Design System Library', 'Figma components standardization', '2026-07-05', '2026-09-05', 4),
('HR Portal Enhancement', 'Employee benefits module', '2026-08-01', '2026-10-01', 9),
('Customer Loyalty Program', 'Rewards backend engine', '2026-08-15', '2026-11-15', 8),
('Threat Intelligence', 'Real-time security monitoring', '2026-07-20', '2026-10-20', 7),
('Predictive Analytics', 'Forecasting seasonal demand', '2026-09-01', '2026-12-01', 6),
('Project Management Tool', 'Custom task tracking features', '2026-07-01', '2026-10-01', 3);

INSERT INTO project_users (project_id, user_id) VALUES
(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(7,3),
(1,1),
(5,2),
(10,3),
(15,4),
(20,5),
(30,9),
(22,7),
(35,6),
(12,9),
(40,2);

INSERT INTO timesheets (user_id, project_id, work_date, hours, comment, global_comment, week_id) VALUES
(3, 1, '2026-06-29', 8.00, 'Desarrollo backend', '', '2026-W27'),
(3, 2, '2026-06-30', 8.00, 'Frontend ajustes', '', '2026-W27'),
(3, 3, '2026-07-01', 7.50, 'Reunión de equipo', '', '2026-W27'),
(3, 4, '2026-07-02', 8.00, 'Testing unitario', '', '2026-W27'),
(3, 5, '2026-07-03', 6.50, 'Documentación', '', '2026-W27'),
(3, 6, '2026-07-04', 0.00, '', '', '2026-W27'),
(3, 7, '2026-07-05', 0.00, '', 'Contactar con frontend', '2026-W27'),
(3, 1, '2026-07-06', 8.00, 'Validaciones', '', '2026-W28'),
(3, 2, '2026-07-07', 8.00, 'Fin sprint 1', 'Vacaciones', '2026-W28'),
(3, 3, '2026-07-13', 7.50, 'Planificación sprint', '', '2026-W29'),
(3, 4, '2026-07-20', 8.00, 'Reunión cliente', '', '2026-W30'),
(3, 5, '2026-07-27', 6.50, '', 'Doc. pendiente', '2026-W31'),
(3, 6, '2026-07-28', 0.00, '', 'Doc. pendiente', '2026-W31'),
(3, 7, '2026-08-03', 0.00, 'Lanzamiento app', '', '2026-W32'),
(3, 1, '2026-08-10', 8.00, 'Desarrollo backend', 'Curso testing', '2026-W33'),
(3, 2, '2026-08-17', 8.00, 'Política privacidad', '', '2026-W34'),
(3, 3, '2026-08-18', 7.50, 'Documentación entregada', '', '2026-W34'),
(3, 4, '2026-08-24', 8.00, 'Reunión becarios', '', '2026-W35'),
(3, 5, '2026-08-25', 6.50, 'Videollamada Alemania', '', '2026-W35'),
(3, 6, '2026-08-31', 0.00, '', '', '2026-W36'),
(3, 7, '2026-09-28', 0.00, '', 'Fin de contrato', '2026-W40'),
(1, 1, '2026-06-22', 8.00, 'Setup entorno cloud', 'Refactor infra', '2026-W26'),
(1, 1, '2026-06-23', 8.00, 'Migración de base de datos', 'Sin errores', '2026-W26'),
(5, 2, '2026-06-25', 4.00, 'Entrenamiento modelo IA', 'Ajuste de hiperparámetros', '2026-W26'),
(10, 3, '2026-06-29', 8.00, 'Escaneo vulnerabilidades', 'Auditoría mensual', '2026-W27'),
(10, 3, '2026-06-30', 8.00, 'Parcheo servidores', 'Crítico', '2026-W27'),
(15, 4, '2026-06-29', 6.00, 'Diseño prototipo UI', 'Revisión en Figma', '2026-W27'),
(20, 5, '2026-06-30', 8.00, 'Refactor componente navegación', 'Migración exitosa', '2026-W27'),
(30, 9, '2026-07-01', 4.00, 'Entrevista candidatos', 'Selección HR', '2026-W27'),
(5, 2, '2026-07-06', 8.00, 'Integración API', 'Testing final', '2026-W28'),
(22, 7, '2026-07-13', 8.00, 'Implementación RBAC', 'Seguridad nivel 2', '2026-W29'),
(35, 6, '2026-07-20', 7.50, 'Configuración ETL', 'Optimización de queries', '2026-W30'),
(12, 9, '2026-08-03', 0.00, 'Vacaciones', 'Descanso estival', '2026-W32'),
(12, 9, '2026-08-04', 0.00, 'Vacaciones', 'Descanso estival', '2026-W32'),
(40, 2, '2026-12-21', 8.00, 'Mantenimiento preventivo', 'Cierre de año', '2026-W52'),
(40, 2, '2026-12-22', 8.00, 'Reporte anual QA', 'Documentación final', '2026-W52');