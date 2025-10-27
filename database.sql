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
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Sample data with BCrypt passwords
-- -----------------------------------------------------
-- Password "123" codificado con BCrypt: $2a$10$wBvK1lzL0f1uZT/5gzY.5OtXk.0v88ZqIMhA/zGxB1STLclozix7W
INSERT INTO users (name, email, password) VALUES
('pepetardo', 'pepetardo@gmail.com', '$2a$10$0.NZ1S8IlzK6895EQZ5zheK5vvop0i/FMPCMhyAo6gHXsK6OqR7lC'),
('test', 'test@gmail.com', '$2a$10$0.NZ1S8IlzK6895EQZ5zheK5vvop0i/FMPCMhyAo6gHXsK6OqR7lC');

-- Projects
INSERT INTO projects (name, description, user_id) VALUES
('Virtualknowledge', 'Project to share knowledge between users', 1),
('Inventory System', 'Project to manage products', 1),
('Web Application', 'Internal web app development', 2);
