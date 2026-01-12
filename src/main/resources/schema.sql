-- Script SQL pour créer le schéma et les tables de la base de données
-- Application de Gestion Scrum
-- Base de données: PostgreSQL

-- Créer le schéma s'il n'existe pas
CREATE SCHEMA IF NOT EXISTS scrum;

-- Utiliser le schéma scrum
SET search_path TO scrum;

-- Note: Les tables seront créées automatiquement par Hibernate
-- Ce script peut être utilisé pour initialiser la base de données manuellement si nécessaire

-- Script de vérification
DO $$
BEGIN
    RAISE NOTICE 'Schéma scrum créé avec succès';
END $$;

-- Commandes utiles pour la gestion de la base de données

-- Voir toutes les tables du schéma scrum
-- SELECT table_name FROM information_schema.tables WHERE table_schema = 'scrum';

-- Supprimer toutes les tables (ATTENTION: perte de données)
-- DROP SCHEMA scrum CASCADE;
-- CREATE SCHEMA scrum;

-- Voir la structure d'une table
-- \d scrum.user_story

-- Voir toutes les contraintes
-- SELECT * FROM information_schema.table_constraints WHERE table_schema = 'scrum';

