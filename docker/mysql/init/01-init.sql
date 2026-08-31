-- FairCart initial schema bootstrap
CREATE DATABASE IF NOT EXISTS faircart
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE faircart;

-- JPA will manage tables; this script ensures DB + charset exist.
