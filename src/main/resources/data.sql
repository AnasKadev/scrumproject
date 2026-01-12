-- Données d'exemple pour l'application Scrum Management
-- Ce fichier peut être utilisé pour initialiser la base de données avec des données de test

SET search_path TO scrum;

-- IMPORTANT: Ces inserts ne seront pas automatiquement exécutés
-- Utilisez l'Initializer.java à la place ou exécutez manuellement

-- ========================================
-- USERS (mot de passe: "password123" - à hasher en production)
-- ========================================
INSERT INTO app_user (id, firstname, lastname, username, pwd, email, role, is_active, created_at, updated_at) VALUES
(1, 'Admin', 'System', 'admin', 'password123', 'admin@scrum.com', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'John', 'Doe', 'jdoe', 'password123', 'jdoe@scrum.com', 'PRODUCT_OWNER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Jane', 'Smith', 'jsmith', 'password123', 'jsmith@scrum.com', 'SCRUM_MASTER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Bob', 'Martin', 'bmartin', 'password123', 'bmartin@scrum.com', 'DEVELOPER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Alice', 'Johnson', 'ajohnson', 'password123', 'ajohnson@scrum.com', 'DEVELOPER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- PROJECT
-- ========================================
INSERT INTO project (id, name, description, start_date, end_date, is_active, created_at, updated_at) VALUES
(1, 'E-Commerce Platform', 'Plateforme de commerce électronique moderne avec gestion des commandes et paiements', '2026-01-01', '2026-12-31', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- PROJECT_USER (Affectation des membres au projet)
-- ========================================
INSERT INTO project_user (id, user_id, project_id, role, joined_date, is_active, created_at, updated_at) VALUES
(1, 2, 1, 'PRODUCT_OWNER', '2026-01-01', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 3, 1, 'SCRUM_MASTER', '2026-01-01', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 4, 1, 'DEVELOPER', '2026-01-01', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 5, 1, 'DEVELOPER', '2026-01-01', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- PRODUCT_BACKLOG
-- ========================================
INSERT INTO product_backlog (id, nom, description, project_id, created_at, updated_at) VALUES
(1, 'Product Backlog - E-Commerce', 'Backlog principal du projet E-Commerce Platform', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- EPIC
-- ========================================
INSERT INTO epic (id, title, description, color, product_backlog_id, created_at, updated_at) VALUES
(1, 'Gestion des Utilisateurs', 'Fonctionnalités liées à la gestion des comptes utilisateurs', '#3498db', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Catalogue Produits', 'Gestion et affichage du catalogue de produits', '#2ecc71', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Panier et Commandes', 'Processus d''achat et gestion des commandes', '#e74c3c', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- USER_STORY
-- ========================================
INSERT INTO user_story (id, title, description, status, priority, priority_order, story_points, business_value,
                        acceptance_criteria, estimated_hours, actual_hours, epic_id, product_backlog_id,
                        sprint_backlog_id, created_at, updated_at) VALUES
(1, 'Inscription utilisateur', 'En tant qu''utilisateur, je veux pouvoir créer un compte pour accéder à la plateforme',
    'TO_DO', 'MUST_HAVE', 1, 8, 10,
    '- Formulaire avec email, nom, prénom, mot de passe\n- Validation email unique\n- Confirmation par email',
    16, 0, 1, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Connexion utilisateur', 'En tant qu''utilisateur, je veux pouvoir me connecter pour accéder à mon compte',
    'TO_DO', 'MUST_HAVE', 2, 5, 10,
    '- Formulaire de connexion\n- Gestion des erreurs\n- Session persistante',
    10, 0, 1, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 'Affichage du catalogue', 'En tant que visiteur, je veux voir la liste des produits disponibles',
    'TO_DO', 'MUST_HAVE', 3, 13, 9,
    '- Liste paginée des produits\n- Filtrage par catégorie\n- Recherche par nom',
    24, 0, 2, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 'Détail produit', 'En tant que visiteur, je veux voir les détails d''un produit',
    'TO_DO', 'MUST_HAVE', 4, 5, 8,
    '- Page avec photos, description, prix\n- Disponibilité du stock\n- Bouton ajouter au panier',
    12, 0, 2, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(5, 'Ajouter au panier', 'En tant qu''utilisateur, je veux ajouter des produits à mon panier',
    'TO_DO', 'MUST_HAVE', 5, 8, 9,
    '- Bouton ajouter au panier\n- Mise à jour du compteur\n- Notification de confirmation',
    16, 0, 3, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- SPRINT_BACKLOG
-- ========================================
INSERT INTO sprint_backlog (id, name, description, sprint_goal, status, start_date, end_date,
                            sprint_number, planned_velocity, actual_velocity, project_id,
                            created_at, updated_at) VALUES
(1, 'Sprint 1', 'Premier sprint - Authentification',
    'Implémenter l''authentification de base des utilisateurs',
    'PLANNED', '2026-01-15', '2026-01-29', 1, 13, 0, 1,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- On pourrait déplacer des User Stories vers le Sprint
-- UPDATE user_story SET sprint_backlog_id = 1 WHERE id IN (1, 2);

-- ========================================
-- TASK (Exemples pour User Story 1)
-- ========================================
INSERT INTO task (id, title, description, status, estimated_hours, actual_hours, remaining_hours,
                 task_order, user_story_id, sprint_backlog_id, assigned_to, created_at, updated_at) VALUES
(1, 'Créer l''entité User', 'Créer la classe User avec JPA annotations',
    'TO_DO', 2, 0, 2, 1, 1, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Créer UserRepository', 'Interface repository pour User',
    'TO_DO', 1, 0, 1, 2, 1, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 'Créer UserService', 'Service pour la logique métier de l''utilisateur',
    'TO_DO', 4, 0, 4, 3, 1, NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 'Créer API REST inscription', 'Endpoint POST /api/users/register',
    'TO_DO', 3, 0, 3, 4, 1, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(5, 'Tests unitaires', 'Tests pour UserService et UserController',
    'TO_DO', 4, 0, 4, 5, 1, NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- COMMENT (Exemples)
-- ========================================
INSERT INTO comment (id, content, user_id, user_story_id, task_id, is_edited, created_at, updated_at) VALUES
(1, 'Attention: vérifier que l''email n''existe pas déjà avant d''enregistrer',
    2, 1, NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Prévoir la validation côté serveur ET côté client',
    3, 1, NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- ACTIVITY_LOG (Exemples)
-- ========================================
INSERT INTO activity_log (id, entity_type, entity_id, action, old_value, new_value, description,
                         user_id, project_id, created_at, updated_at) VALUES
(1, 'UserStory', 1, 'CREATED', NULL, 'TO_DO', 'User story "Inscription utilisateur" créée',
    2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Task', 1, 'ASSIGNED', NULL, 'Bob Martin', 'Tâche assignée à Bob Martin',
    3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Réinitialiser les séquences (si nécessaire)
-- SELECT setval('scrum.app_user_id_seq', (SELECT MAX(id) FROM scrum.app_user));
-- SELECT setval('scrum.project_id_seq', (SELECT MAX(id) FROM scrum.project));
-- etc...

