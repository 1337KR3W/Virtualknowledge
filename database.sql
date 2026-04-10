-- -----------------------------------------------------
-- Initialization script: database.sql
-- Creates the 'users' and 'projects' tables
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
  registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
INSERT INTO users (name, email, password) VALUES
('pepetardo', 'pepetardo@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu'),
('test', 'test@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu'),
('pepito', 'pepito@gmail.com', '$2a$10$x8opSxm6b9KFg0d8dRLwwumQgCYZstq0MUM./jBOmefjtjC5QKbXu');

-- Projects
INSERT INTO projects (name, description, user_id) VALUES
('Virtualknowledge', 'Project to share knowledge between users', 1),
('Inventory System', 'Project to manage products', 1),
('E-commerce Platform', 'Full-stack online store with payment integration', 1),
('Inventory System', 'Project to manage warehouse products and stock', 1),
('Customer Portal', 'Help desk and ticketing system for clients', 1),
('Mobile Fitness App', 'Android/iOS app to track daily workouts', 1),
('Data Analytics Dashboard', 'Visualizing sales data with real-time charts', 1),
('Task Management Tool', 'Kanban style board for team collaboration', 1),
('Virtual Learning Environment', 'LMS for online courses and student grading', 1),
('Smart Home Controller', 'IoT project to manage lighting and temperature', 1),
('Financial Tracker', 'Personal finance app with expense categorization', 1),
('AI Chatbot Service', 'NLP-based bot for automated customer service', 1),
('Recipe Book App', 'Social platform for sharing and saving cooking recipes', 1),
('Supply Chain Logger', 'Blockchain project for tracking goods transit', 1),
('Fleet Management', 'GPS tracking and maintenance for company vehicles', 1),
('Web Application', 'Internal web app development', 2),
('Testing with JUnit', 'Learn basics of testing with JUnit', 3);
