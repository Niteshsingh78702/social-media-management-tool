-- Flintzy Social Media Management Tool
-- Database Schema
-- MySQL Database Dump

-- Create Database
CREATE DATABASE IF NOT EXISTS flintzy;
USE flintzy;

-- Table structure for table `users`
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `provider` VARCHAR(50) NOT NULL COMMENT 'OAuth provider (e.g., GOOGLE)',
  `provider_id` VARCHAR(255) NOT NULL COMMENT 'Provider user ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table structure for table `social_accounts`
CREATE TABLE IF NOT EXISTS `social_accounts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `page_id` VARCHAR(255) NOT NULL COMMENT 'Social media page/account ID',
  `page_name` VARCHAR(255) NOT NULL COMMENT 'Name of the page',
  `page_access_token` VARCHAR(500) NOT NULL COMMENT 'Access token for the page',
  `platform` VARCHAR(50) NOT NULL COMMENT 'Platform name (e.g., FACEBOOK, INSTAGRAM)',
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_social_accounts_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for better performance
CREATE INDEX `idx_page_id` ON `social_accounts` (`page_id`);
CREATE INDEX `idx_user_email` ON `social_accounts` (`user_id`);

-- Sample data (optional - remove if not needed)
-- INSERT INTO users (name, email, provider, provider_id) VALUES 
-- ('Test User', 'test@example.com', 'GOOGLE', 'google_12345');
