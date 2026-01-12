# Conception de Base de Données - Application de Gestion Scrum

## Vue d'ensemble

Cette base de données est conçue pour gérer les projets Agile/Scrum avec toutes les exigences du cahier des charges.

## Schéma de Base de Données

### 1. **User** (Utilisateurs)
Stocke les informations des utilisateurs du système.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `firstname` - Prénom
- `lastname` - Nom
- `username` - Nom d'utilisateur (unique)
- `pwd` - Mot de passe (à hasher)
- `email` - Email (unique)
- `role` - Rôle (ADMIN, PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER)
- `is_active` - Statut actif
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- OneToMany avec `ProjectUser` (projets assignés)
- OneToMany avec `Task` (tâches assignées)
- OneToMany avec `Comment` (commentaires créés)
- OneToMany avec `ActivityLog` (actions effectuées)

---

### 2. **Project** (Projets)
Représente un projet Agile.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `name` - Nom du projet
- `description` - Description du projet
- `start_date` - Date de début
- `end_date` - Date de fin
- `is_active` - Statut actif
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- OneToOne avec `ProductBacklog` (backlog du produit)
- OneToMany avec `SprintBacklog` (sprints)
- OneToMany avec `ProjectUser` (membres du projet)
- OneToMany avec `ActivityLog` (historique)

---

### 3. **ProjectUser** (Affectation des utilisateurs aux projets)
Table de liaison pour les membres du projet avec leurs rôles spécifiques.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `user_id` (FK) - Référence à User
- `project_id` (FK) - Référence à Project
- `role` - Rôle dans le projet
- `joined_date` - Date d'ajout
- `is_active` - Statut actif
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Contraintes:**
- Unique(user_id, project_id) - Un utilisateur ne peut pas être ajouté deux fois au même projet

---

### 4. **ProductBacklog** (Backlog Produit)
Contient toutes les User Stories et Epics d'un projet.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `nom` - Nom du backlog
- `description` - Description
- `project_id` (FK) - Référence à Project (OneToOne)
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- OneToOne avec `Project`
- OneToMany avec `Epic`
- OneToMany avec `UserStory`

---

### 5. **Epic** (Epics)
Représente une grande fonctionnalité regroupant plusieurs User Stories.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `title` - Titre de l'Epic
- `description` - Description détaillée
- `color` - Couleur pour l'affichage visuel
- `product_backlog_id` (FK) - Référence à ProductBacklog
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `ProductBacklog`
- OneToMany avec `UserStory`

---

### 6. **UserStory** (User Stories)
Représente une fonctionnalité utilisateur.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `title` - Titre de la User Story
- `description` - Description détaillée
- `status` - Statut (TO_DO, IN_PROGRESS, DONE, BLOCKED)
- `priority` - Priorité MoSCoW (MUST_HAVE, SHOULD_HAVE, COULD_HAVE, WONT_HAVE)
- `priority_order` - Ordre de priorité numérique
- `story_points` - Points d'effort (Fibonacci: 1,2,3,5,8,13,21)
- `business_value` - Valeur métier (1-10)
- `acceptance_criteria` - Critères d'acceptation
- `estimated_hours` - Estimation en heures
- `actual_hours` - Heures réelles
- `epic_id` (FK) - Référence à Epic (optionnel)
- `product_backlog_id` (FK) - Référence à ProductBacklog
- `sprint_backlog_id` (FK) - Référence à SprintBacklog (optionnel)
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `Epic`
- ManyToOne avec `ProductBacklog`
- ManyToOne avec `SprintBacklog`
- OneToMany avec `Task`
- OneToMany avec `Comment`

**Facteurs de Priorisation (selon cahier des charges):**
1. Valeur Métier (`business_value`)
2. Urgence (via `priority_order`)
3. Complexité/Coût (`story_points`)
4. Priorité MoSCoW (`priority`)

---

### 7. **SprintBacklog** (Backlog Sprint)
Représente un sprint avec ses User Stories sélectionnées.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `name` - Nom du sprint
- `description` - Description
- `sprint_goal` - Objectif du sprint
- `status` - Statut (PLANNED, ACTIVE, COMPLETED, CANCELLED)
- `start_date` - Date de début
- `end_date` - Date de fin
- `sprint_number` - Numéro du sprint
- `planned_velocity` - Vélocité planifiée (story points)
- `actual_velocity` - Vélocité réelle (story points)
- `project_id` (FK) - Référence à Project
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `Project`
- OneToMany avec `UserStory`
- OneToMany avec `Task`

---

### 8. **Task** (Tâches)
Représente une tâche technique associée à une User Story.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `title` - Titre de la tâche
- `description` - Description détaillée
- `status` - Statut (TO_DO, IN_PROGRESS, DONE, BLOCKED)
- `estimated_hours` - Estimation en heures
- `actual_hours` - Heures réelles
- `remaining_hours` - Heures restantes
- `task_order` - Ordre de la tâche
- `user_story_id` (FK) - Référence à UserStory
- `sprint_backlog_id` (FK) - Référence à SprintBacklog
- `assigned_to` (FK) - Référence à User (développeur)
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `UserStory`
- ManyToOne avec `SprintBacklog`
- ManyToOne avec `User` (développeur assigné)
- OneToMany avec `Comment`

---

### 9. **Comment** (Commentaires)
Permet aux utilisateurs de commenter les User Stories et Tasks.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `content` - Contenu du commentaire
- `user_id` (FK) - Référence à User (auteur)
- `user_story_id` (FK) - Référence à UserStory (optionnel)
- `task_id` (FK) - Référence à Task (optionnel)
- `is_edited` - Indicateur de modification
- `created_at` - Date de création
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `User`
- ManyToOne avec `UserStory`
- ManyToOne avec `Task`

---

### 10. **ActivityLog** (Journal d'activité)
Enregistre toutes les actions effectuées dans le système pour l'audit et le suivi.

**Colonnes:**
- `id` (PK) - Identifiant unique
- `entity_type` - Type d'entité (UserStory, Task, Sprint, etc.)
- `entity_id` - ID de l'entité
- `action` - Action effectuée (CREATED, UPDATED, DELETED, STATUS_CHANGED, etc.)
- `old_value` - Ancienne valeur
- `new_value` - Nouvelle valeur
- `description` - Description de l'action
- `user_id` (FK) - Référence à User (qui a effectué l'action)
- `project_id` (FK) - Référence à Project
- `created_at` - Date de l'action
- `updated_at` - Date de mise à jour

**Relations:**
- ManyToOne avec `User`
- ManyToOne avec `Project`

---

## Énumérations (Enums)

### UserRole
- `ADMIN` - Administrateur système
- `PRODUCT_OWNER` - Propriétaire du produit
- `SCRUM_MASTER` - Scrum Master
- `DEVELOPER` - Développeur

### UserStoryStatus
- `TO_DO` - À faire
- `IN_PROGRESS` - En cours
- `DONE` - Terminé
- `BLOCKED` - Bloqué

### TaskStatus
- `TO_DO` - À faire
- `IN_PROGRESS` - En cours
- `DONE` - Terminé
- `BLOCKED` - Bloqué

### Priority (MoSCoW)
- `MUST_HAVE` - Doit avoir (niveau 1)
- `SHOULD_HAVE` - Devrait avoir (niveau 2)
- `COULD_HAVE` - Pourrait avoir (niveau 3)
- `WONT_HAVE` - N'aura pas (niveau 4)

### SprintStatus
- `PLANNED` - Planifié
- `ACTIVE` - Actif
- `COMPLETED` - Terminé
- `CANCELLED` - Annulé

---

## Flux de Données

### 1. Création d'un Projet
```
Project → ProductBacklog
Project → ProjectUser (assigner les membres)
```

### 2. Gestion du Product Backlog
```
ProductBacklog → Epic (créer des Epics)
ProductBacklog → UserStory (ajouter des User Stories)
UserStory → Epic (lier à un Epic - optionnel)
```

### 3. Planification de Sprint
```
ProductBacklog → UserStory (sélectionner)
UserStory → SprintBacklog (déplacer)
UserStory → Task (décomposer en tâches)
Task → User (assigner aux développeurs)
```

### 4. Exécution de Sprint
```
Task → Status (mettre à jour)
Task → actual_hours (enregistrer le temps)
UserStory → Status (mettre à jour selon les tâches)
```

### 5. Suivi et Reporting
```
ActivityLog → Enregistrer toutes les actions
SprintBacklog → Calculer la vélocité
UserStory → Calculer le burndown
```

---

## Indicateurs de Performance (KPIs)

### Vélocité du Sprint
```sql
SELECT SUM(story_points) 
FROM user_story 
WHERE sprint_backlog_id = ? AND status = 'DONE'
```

### Burndown Chart
Calculer les story points restants par jour dans un sprint.

### Taux de Complétion
```sql
SELECT 
    COUNT(CASE WHEN status = 'DONE' THEN 1 END) * 100.0 / COUNT(*) 
FROM user_story 
WHERE sprint_backlog_id = ?
```

### Charge de Travail par Développeur
```sql
SELECT u.id, u.firstname, u.lastname, COUNT(t.id) as task_count
FROM user u
LEFT JOIN task t ON t.assigned_to = u.id AND t.status IN ('TO_DO', 'IN_PROGRESS')
WHERE u.role = 'DEVELOPER'
GROUP BY u.id
```

---

## Contraintes et Validations

1. **User:**
   - `username` et `email` doivent être uniques
   - `role` est obligatoire

2. **Project:**
   - `name` est obligatoire
   - Un projet ne peut avoir qu'un seul ProductBacklog

3. **UserStory:**
   - Doit appartenir à un ProductBacklog
   - Si assignée à un Sprint, le Sprint doit appartenir au même projet
   - `story_points` doit être un nombre Fibonacci positif
   - `business_value` doit être entre 1 et 10

4. **Task:**
   - Doit appartenir à une UserStory
   - Si assignée à un utilisateur, celui-ci doit être un DEVELOPER
   - `estimated_hours` et `actual_hours` doivent être >= 0

5. **SprintBacklog:**
   - `end_date` doit être après `start_date`
   - Ne peut avoir qu'un seul sprint ACTIVE par projet

---

## Indexes Recommandés

Pour optimiser les performances:

```sql
-- User
CREATE INDEX idx_user_username ON app_user(username);
CREATE INDEX idx_user_email ON app_user(email);
CREATE INDEX idx_user_role ON app_user(role);

-- UserStory
CREATE INDEX idx_userstory_status ON user_story(status);
CREATE INDEX idx_userstory_product_backlog ON user_story(product_backlog_id);
CREATE INDEX idx_userstory_sprint_backlog ON user_story(sprint_backlog_id);
CREATE INDEX idx_userstory_priority_order ON user_story(priority_order);

-- Task
CREATE INDEX idx_task_status ON task(status);
CREATE INDEX idx_task_assigned_to ON task(assigned_to);
CREATE INDEX idx_task_user_story ON task(user_story_id);

-- SprintBacklog
CREATE INDEX idx_sprint_status ON sprint_backlog(status);
CREATE INDEX idx_sprint_project ON sprint_backlog(project_id);
CREATE INDEX idx_sprint_dates ON sprint_backlog(start_date, end_date);

-- ProjectUser
CREATE INDEX idx_project_user_user ON project_user(user_id);
CREATE INDEX idx_project_user_project ON project_user(project_id);
```

---

## Conformité avec le Cahier des Charges

✅ **Section 4.1 - Gestion du Product Backlog**
- Tables: `ProductBacklog`, `UserStory`, `Epic`
- Priorisation via `priority_order` et `business_value`

✅ **Section 4.2 - Critères de Priorisation**
- `business_value` - Valeur Métier
- `priority` - Priorité MoSCoW
- `story_points` - Complexité/Coût
- `priority_order` - Ordre personnalisé

✅ **Section 4.3 - Gestion des Epics**
- Table `Epic` avec relation vers `UserStory`

✅ **Section 4.4 - Gestion du Sprint Backlog**
- Table `SprintBacklog` avec dates, statut, vélocité
- Sélection de User Stories depuis ProductBacklog

✅ **Section 4.5 - Gestion des User Stories**
- Table `UserStory` avec tous les champs requis
- Critères d'acceptation
- Suivi du statut

✅ **Section 4.6 - Gestion des Tasks**
- Table `Task` avec affectation aux développeurs
- Suivi du statut et des heures

✅ **Section 7 - Gestion des Utilisateurs**
- Table `User` avec rôles (PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER)
- Table `ProjectUser` pour les permissions par projet

✅ **Section 8 - Suivi et Reporting**
- Table `ActivityLog` pour l'historique
- Champs pour calculer burndown chart et vélocité

---

## Script de Création (à exécuter)

Voir le fichier `application.properties` pour la configuration de la base de données.
La structure sera créée automatiquement par Hibernate avec `spring.jpa.hibernate.ddl-auto=update`.

