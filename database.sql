-- -----------------------------------------------------
-- Initialization script: database.sql
-- -----------------------------------------------------

CREATE DATABASE IF NOT EXISTS db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db;

-- 1. Table: users
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
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
  user_id BIGINT NOT NULL,
  department_id BIGINT NOT NULL,
  CONSTRAINT fk_proj_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_proj_dept FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

-- 6. Table: timesheets
CREATE TABLE IF NOT EXISTS timesheets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    hours DECIMAL(4,2) NOT NULL,
    comment TEXT,
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
INSERT INTO users (name, email, password, registration_date, status) VALUES
('pepetardo', 'pepetardo@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('test', 'test@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('pepito', 'pepito@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE');

-- Roles
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

-- User roles
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1),
(2, 1),
(3, 2);

-- Departments
INSERT INTO departments (id, name) VALUES 
(1, 'Development'), 
(2, 'QA & Testing'),
(3, 'Administration'),
(4, 'RRHH'),
(5, 'Deployment'),
(6, 'Power Apps'),
(7, 'Virtual Knowledge'),
(8, 'SecApp');

-- User departments
INSERT INTO user_departments (user_id, department_id) VALUES 
(1, 1), (1, 2),
(2, 1), (2, 5),
(3, 1), (3, 2), (3, 3), (3, 4),(3, 5), (3, 6), (3, 7), (3, 8);

-- Projects
INSERT INTO projects (name, description, start_date, end_date, user_id, department_id) VALUES
('Virtualknowledge', 'Project to share knowledge between users','2026-01-01', '2026-03-01', 3, 7),
('Inventory System', 'Project to manage products','2026-01-04', NULL, 3, 3),
('E-commerce Platform', 'Full-stack online store with payment integration','2026-01-04', '2026-03-01', 3, 1),
('Logistik', 'Project to manage warehouse products and stock','2026-01-10', '2026-03-01', 3, 1),
('Customer Portal', 'Help desk and ticketing system for clients','2026-01-12', '2026-03-01', 3, 1),
('Mobile Fitness App', 'Android/iOS app to track daily workouts','2026-01-15', NULL, 3, 8),
('Data Analytics Dashboard', 'Visualizing sales data with real-time charts','2026-01-20', '2026-03-01', 3, 2),
('Task Management Tool', 'Kanban style board for team collaboration','2026-02-01', NULL, 3, 2),
('Virtual Learning Environment', 'LMS for online courses and student grading','2026-02-05', '2026-03-01', 3, 4),
('Smart Home Controller', 'IoT project to manage lighting and temperature','2026-02-12', '2026-03-01', 3, 1),
('Financial Tracker', 'Personal finance app with expense categorization','2026-03-01', '2026-04-01', 3, 8),
('AI Chatbot Service', 'NLP-based bot for automated customer service','2026-03-01', '2026-04-01', 3, 1),
('Recipe Book App', 'Social platform for sharing and saving cooking recipes','2026-03-01', NULL, 3, 1),
('Supply Chain Logger', 'Blockchain project for tracking goods transit','2026-04-01', NULL, 3, 1),
('Fleet Management', 'GPS tracking and maintenance for company vehicles','2026-04-01', NULL, 3, 8),
('Web Application', 'Internal web app development','2026-04-01', NULL, 2, 1),
('Testing with JUnit', 'Learn basics of testing with JUnit','2026-04-01', '2026-04-03', 1, 2);