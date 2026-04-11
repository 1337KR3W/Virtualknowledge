-- -----------------------------------------------------
-- Initialization script: database.sql
-- -----------------------------------------------------

-- Create database if it does not exist (safety)
CREATE DATABASE IF NOT EXISTS db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db;

-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);
-- -----------------------------------------------------
-- Table: roles
-- -----------------------------------------------------
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);
-- -----------------------------------------------------
-- Table: user_roles
-- -----------------------------------------------------
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
-- -----------------------------------------------------
-- Table: projects
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS projects (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: api_keys
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS api_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) NOT NULL UNIQUE,
    api_secret VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- Table: api_keys
-- -----------------------------------------------------
INSERT INTO api_keys (api_key, api_secret, service_name, active) 
VALUES (
    'mi-app-local', 
    '$2a$10$WPX3cmIBX1ATF5lJWTB76uOKDmrG.LIQmRGOlGAJFh0iMptA0zTHa', 
    'Servicio-SSO-Fijo', 
    true
);
-- -----------------------------------------------------
-- Sample data with BCrypt passwords
-- -----------------------------------------------------
-- Password "123" codificado con BCrypt: $2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu
INSERT INTO users (name, email, password, registration_date, role, status) VALUES
('pepetardo', 'pepetardo@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('test', 'test@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE'),
('pepito', 'pepito@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu', CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1),
(2, 1),
(3, 2);
-- Projects
INSERT INTO projects (name, description, user_id) VALUES
('Virtualknowledge', 'Project to share knowledge between users', 3),
('Inventory System', 'Project to manage products', 3),
('E-commerce Platform', 'Full-stack online store with payment integration', 3),
('Inventory System', 'Project to manage warehouse products and stock', 3),
('Customer Portal', 'Help desk and ticketing system for clients', 3),
('Mobile Fitness App', 'Android/iOS app to track daily workouts', 3),
('Data Analytics Dashboard', 'Visualizing sales data with real-time charts', 3),
('Task Management Tool', 'Kanban style board for team collaboration', 3),
('Virtual Learning Environment', 'LMS for online courses and student grading', 3),
('Smart Home Controller', 'IoT project to manage lighting and temperature', 3),
('Financial Tracker', 'Personal finance app with expense categorization', 3),
('AI Chatbot Service', 'NLP-based bot for automated customer service', 3),
('Recipe Book App', 'Social platform for sharing and saving cooking recipes', 3),
('Supply Chain Logger', 'Blockchain project for tracking goods transit', 3),
('Fleet Management', 'GPS tracking and maintenance for company vehicles', 3),
('Web Application', 'Internal web app development', 2),
('Testing with JUnit', 'Learn basics of testing with JUnit', 1);
