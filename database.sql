CREATE DATABASE IF NOT EXISTS db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db;

CREATE TABLE IF NOT EXISTS departments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(64) NOT NULL,
  last_name VARCHAR(64) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  registration_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  department_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  CONSTRAINT fk_user_dept FOREIGN KEY (department_id) REFERENCES departments(id),
  CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS projects (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  start_date DATETIME NOT NULL,
  end_date DATETIME,
  department_id BIGINT NOT NULL,
  CONSTRAINT fk_project_dept FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS project_users (
  project_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (project_id, user_id),
  CONSTRAINT fk_pu_project FOREIGN KEY (project_id) REFERENCES projects(id),
  CONSTRAINT fk_pu_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS timesheets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  project_id BIGINT NOT NULL,
  work_date DATE NOT NULL,
  hours DECIMAL(5, 2) NOT NULL,
  comment TEXT,
  global_comment TEXT,
  week_id VARCHAR(10),
  CONSTRAINT fk_ts_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_ts_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE INDEX idx_ts_user_date ON timesheets(user_id, work_date);
CREATE INDEX idx_ts_week ON timesheets(week_id);


INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO departments (name) VALUES ('Development'), ('QA & Testing');

INSERT INTO users (first_name, last_name, email, password, department_id, role_id) VALUES
('Juan', 'Admin', 'admin@privatebay.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', 1, 2),
('Ana', 'User', 'ana@privatebay.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', 1, 1),
('Luis', 'User', 'luis@privatebay.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', 2, 1),
('Maria', 'User', 'maria@privatebay.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', 2, 1);

INSERT INTO projects (name, description, start_date, department_id) VALUES
('Proyecto Alfa', 'Sistema de gestión interna', '2026-06-01', 1),
('Proyecto Beta', 'Portal de calidad', '2026-06-01', 2);

INSERT INTO project_users (project_id, user_id) VALUES 
(1, 1), (1, 2), (2, 3), (2, 4);

INSERT INTO timesheets (user_id, project_id, work_date, hours, comment, global_comment, week_id) VALUES
(2, 1, '2026-06-25', 8.0, 'Desarrollo API', 'Trabajo semana anterior', '2026-W26'),
(2, 1, '2026-06-29', 8.0, 'Refactor Controller', 'Trabajo actual', '2026-W27'),
(2, 1, '2026-06-30', 8.0, 'Fix Bugs', 'Trabajo actual', '2026-W27'),
(2, 1, '2026-07-06', 8.0, 'Planificación sprint', 'Trabajo futuro', '2026-W28'),
(2, 1, '2026-07-07', 8.0, 'Testing', 'Trabajo futuro', '2026-W28');