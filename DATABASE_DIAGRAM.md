# Diagramme de la Base de Données - Scrum Management

## Diagramme Entité-Relations (ER)

```
┌─────────────────────┐
│       User          │
├─────────────────────┤
│ id (PK)             │
│ firstname           │
│ lastname            │
│ username (UNIQUE)   │
│ pwd                 │
│ email (UNIQUE)      │
│ role (ENUM)         │
│ is_active           │
│ created_at          │
│ updated_at          │
└─────────────────────┘
         │ 1
         │
         │ N
┌─────────────────────┐         1 ┌─────────────────────┐
│   ProjectUser       │───────────│      Project        │
├─────────────────────┤           ├─────────────────────┤
│ id (PK)             │           │ id (PK)             │
│ user_id (FK)        │           │ name                │
│ project_id (FK)     │           │ description         │
│ role (ENUM)         │           │ start_date          │
│ joined_date         │           │ end_date            │
│ is_active           │           │ is_active           │
│ created_at          │           │ created_at          │
│ updated_at          │           │ updated_at          │
└─────────────────────┘           └─────────────────────┘
                                           │ 1
                                           │
                                  ┌────────┴────────┐
                                  │ 1               │ N
                        ┌─────────────────────┐ ┌─────────────────────┐
                        │  ProductBacklog     │ │   SprintBacklog     │
                        ├─────────────────────┤ ├─────────────────────┤
                        │ id (PK)             │ │ id (PK)             │
                        │ nom                 │ │ name                │
                        │ description         │ │ description         │
                        │ project_id (FK)     │ │ sprint_goal         │
                        │ created_at          │ │ status (ENUM)       │
                        │ updated_at          │ │ start_date          │
                        └─────────────────────┘ │ end_date            │
                                 │ 1             │ sprint_number       │
                                 │               │ planned_velocity    │
                        ┌────────┴────────┐     │ actual_velocity     │
                        │ 1               │ N   │ project_id (FK)     │
              ┌─────────────────────┐           │ created_at          │
              │       Epic          │           │ updated_at          │
              ├─────────────────────┤           └─────────────────────┘
              │ id (PK)             │                    │ 1
              │ title               │                    │
              │ description         │                    │ N
              │ color               │           ┌─────────────────────┐
              │ product_backlog_id  │     ┌─────│     UserStory       │
              │ created_at          │     │     ├─────────────────────┤
              │ updated_at          │     │     │ id (PK)             │
              └─────────────────────┘     │     │ title               │
                       │ 1                │     │ description         │
                       │                  │     │ status (ENUM)       │
                       │                  │     │ priority (ENUM)     │
                       │ N                │     │ priority_order      │
              ┌─────────────────────┐     │     │ story_points        │
              │     UserStory       │─────┘     │ business_value      │
              │   (voir ci-contre)  │           │ acceptance_criteria │
              └─────────────────────┘           │ estimated_hours     │
                       │ 1                      │ actual_hours        │
                       │                        │ epic_id (FK)        │
                       │ N                      │ product_backlog_id  │
              ┌─────────────────────┐           │ sprint_backlog_id   │
              │       Task          │           │ created_at          │
              ├─────────────────────┤           │ updated_at          │
              │ id (PK)             │           └─────────────────────┘
              │ title               │                    │ 1
              │ description         │                    │
              │ status (ENUM)       │                    │ N
              │ estimated_hours     │           ┌─────────────────────┐
              │ actual_hours        │           │      Comment        │
              │ remaining_hours     │           ├─────────────────────┤
              │ task_order          │           │ id (PK)             │
              │ user_story_id (FK)  │           │ content             │
              │ sprint_backlog_id   │           │ user_id (FK)        │
              │ assigned_to (FK)    │───┐       │ user_story_id (FK)  │
              │ created_at          │   │       │ task_id (FK)        │
              │ updated_at          │   │       │ is_edited           │
              └─────────────────────┘   │       │ created_at          │
                                        │       │ updated_at          │
                                        │       └─────────────────────┘
                                        │
                                        └─────→ User (assigned_to)


┌─────────────────────┐
│   ActivityLog       │
├─────────────────────┤
│ id (PK)             │
│ entity_type         │
│ entity_id           │
│ action              │
│ old_value           │
│ new_value           │
│ description         │
│ user_id (FK)        │
│ project_id (FK)     │
│ created_at          │
│ updated_at          │
└─────────────────────┘
```

## Relations Principales

### 1. Projet et Équipe
- **Project** (1) ←→ (N) **ProjectUser** : Un projet a plusieurs membres
- **User** (1) ←→ (N) **ProjectUser** : Un utilisateur peut être dans plusieurs projets

### 2. Hiérarchie des Backlogs
- **Project** (1) ←→ (1) **ProductBacklog** : Un projet a un backlog produit
- **Project** (1) ←→ (N) **SprintBacklog** : Un projet a plusieurs sprints
- **ProductBacklog** (1) ←→ (N) **Epic** : Un backlog contient plusieurs epics
- **ProductBacklog** (1) ←→ (N) **UserStory** : Un backlog contient plusieurs user stories

### 3. Organisation des User Stories
- **Epic** (1) ←→ (N) **UserStory** : Un epic regroupe plusieurs user stories
- **UserStory** (1) ←→ (N) **Task** : Une user story se décompose en tâches

### 4. Sprint et Exécution
- **SprintBacklog** (1) ←→ (N) **UserStory** : Un sprint contient plusieurs user stories
- **SprintBacklog** (1) ←→ (N) **Task** : Un sprint contient plusieurs tâches
- **User** (1) ←→ (N) **Task** : Un développeur a plusieurs tâches assignées

### 5. Collaboration
- **User** (1) ←→ (N) **Comment** : Un utilisateur peut créer plusieurs commentaires
- **UserStory** (1) ←→ (N) **Comment** : Une user story peut avoir plusieurs commentaires
- **Task** (1) ←→ (N) **Comment** : Une tâche peut avoir plusieurs commentaires

### 6. Audit
- **User** (1) ←→ (N) **ActivityLog** : Toutes les actions d'un utilisateur sont enregistrées
- **Project** (1) ←→ (N) **ActivityLog** : Tous les événements d'un projet sont journalisés

## Cardinalités

| Relation | Type | Description |
|----------|------|-------------|
| Project → ProductBacklog | 1:1 | Chaque projet a exactement un Product Backlog |
| Project → SprintBacklog | 1:N | Un projet peut avoir plusieurs sprints |
| Project → ProjectUser | 1:N | Un projet a plusieurs membres |
| User → ProjectUser | 1:N | Un utilisateur peut être dans plusieurs projets |
| ProductBacklog → Epic | 1:N | Un backlog contient plusieurs epics |
| ProductBacklog → UserStory | 1:N | Un backlog contient plusieurs user stories |
| Epic → UserStory | 1:N | Un epic contient plusieurs user stories (optionnel) |
| UserStory → Task | 1:N | Une user story a plusieurs tâches |
| SprintBacklog → UserStory | 1:N | Un sprint contient plusieurs user stories |
| SprintBacklog → Task | 1:N | Un sprint contient plusieurs tâches |
| User → Task | 1:N | Un développeur peut être assigné à plusieurs tâches |
| User → Comment | 1:N | Un utilisateur peut créer plusieurs commentaires |
| UserStory → Comment | 1:N | Une user story peut avoir plusieurs commentaires |
| Task → Comment | 1:N | Une tâche peut avoir plusieurs commentaires |

## Flux de Données Typique

### Phase 1: Initialisation du Projet
```
1. Créer Project
2. Créer ProductBacklog (lié au Project)
3. Créer ProjectUser (assigner les membres avec leurs rôles)
```

### Phase 2: Construction du Backlog
```
4. Créer Epic (facultatif, dans ProductBacklog)
5. Créer UserStory (dans ProductBacklog, lier à Epic si nécessaire)
6. Définir priority, story_points, business_value
7. Trier par priority_order
```

### Phase 3: Planification de Sprint
```
8. Créer SprintBacklog
9. Sélectionner UserStory du ProductBacklog
10. Assigner UserStory au SprintBacklog
11. Créer Task pour chaque UserStory
12. Assigner Task aux User (développeurs)
```

### Phase 4: Exécution de Sprint
```
13. Mettre à jour Task.status (TO_DO → IN_PROGRESS → DONE)
14. Enregistrer Task.actual_hours
15. Ajouter Comment si nécessaire
16. ActivityLog enregistre toutes les modifications
17. Mettre à jour UserStory.status quand toutes les tâches sont terminées
```

### Phase 5: Clôture de Sprint
```
18. Marquer SprintBacklog.status = COMPLETED
19. Calculer SprintBacklog.actual_velocity
20. Déplacer UserStory non terminées vers le prochain sprint
```

## Contraintes d'Intégrité Référentielle

1. **CASCADE sur DELETE** :
   - Project → ProductBacklog, SprintBacklog, ProjectUser, ActivityLog
   - ProductBacklog → Epic, UserStory
   - UserStory → Task, Comment
   - Task → Comment

2. **SET NULL sur DELETE** :
   - Epic → UserStory (epic_id peut être null)
   - SprintBacklog → UserStory (sprint_backlog_id peut être null si pas encore assignée)
   - User → Task (assigned_to peut être null si non assignée)

3. **RESTRICT sur DELETE** :
   - User → ProjectUser (ne pas supprimer un user s'il est dans des projets actifs)

## Indexes Recommandés

Pour optimiser les performances des requêtes fréquentes :

```sql
-- Recherche par username/email
CREATE INDEX idx_user_username ON scrum.app_user(username);
CREATE INDEX idx_user_email ON scrum.app_user(email);

-- Filtrage par statut
CREATE INDEX idx_userstory_status ON scrum.user_story(status);
CREATE INDEX idx_task_status ON scrum.task(status);
CREATE INDEX idx_sprint_status ON scrum.sprint_backlog(status);

-- Jointures fréquentes
CREATE INDEX idx_userstory_product_backlog ON scrum.user_story(product_backlog_id);
CREATE INDEX idx_userstory_sprint_backlog ON scrum.user_story(sprint_backlog_id);
CREATE INDEX idx_task_user_story ON scrum.task(user_story_id);
CREATE INDEX idx_task_assigned_to ON scrum.task(assigned_to);

-- Tri par priorité
CREATE INDEX idx_userstory_priority_order ON scrum.user_story(priority_order);

-- Recherche par période
CREATE INDEX idx_sprint_dates ON scrum.sprint_backlog(start_date, end_date);

-- Audit
CREATE INDEX idx_activity_log_entity ON scrum.activity_log(entity_type, entity_id);
CREATE INDEX idx_activity_log_created ON scrum.activity_log(created_at);
```

## Taille Estimée des Tables

| Table | Estimation | Croissance |
|-------|------------|------------|
| User | Dizaines | Lente |
| Project | Dizaines | Lente |
| ProjectUser | Centaines | Moyenne |
| ProductBacklog | Dizaines | Lente |
| SprintBacklog | Centaines | Moyenne |
| Epic | Centaines | Moyenne |
| UserStory | Milliers | Rapide |
| Task | Dizaines de milliers | Rapide |
| Comment | Milliers | Rapide |
| ActivityLog | Centaines de milliers | Très rapide |

**Recommandation** : Archiver les ActivityLog après 6 mois pour maintenir la performance.

