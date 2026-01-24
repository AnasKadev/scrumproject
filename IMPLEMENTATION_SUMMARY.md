# Application de Gestion de Projets Agile - Services et Contrôleurs

## Services et Contrôleurs Implémentés

Cette implémentation comprend tous les services et contrôleurs pour la gestion du Product Backlog, Epics, User Stories et Tasks selon la documentation fournie.

### 1. ProductBacklogService et ProductBacklogController

**Fonctionnalités implémentées :**
- ✅ Création et gestion des Product Backlog
- ✅ Association automatique avec un projet
- ✅ Modification et suppression
- ✅ Consultation par ID ou par projet

**Endpoints REST :**
- `POST /api/product-backlogs` - Créer un product backlog
- `PUT /api/product-backlogs/{id}` - Modifier un product backlog
- `GET /api/product-backlogs/{id}` - Récupérer un product backlog
- `GET /api/product-backlogs/project/{projectId}` - Récupérer par projet
- `GET /api/product-backlogs` - Lister tous les product backlogs
- `DELETE /api/product-backlogs/{id}` - Supprimer un product backlog

### 2. EpicService et EpicController

**Fonctionnalités implémentées :**
- ✅ Création et gestion des Epics
- ✅ Lien automatique avec Product Backlog
- ✅ Visualisation des User Stories liées
- ✅ Modification et suppression

**Endpoints REST :**
- `POST /api/epics` - Créer un epic
- `PUT /api/epics/{id}` - Modifier un epic
- `GET /api/epics/{id}` - Récupérer un epic
- `GET /api/epics` - Lister tous les epics
- `GET /api/epics/product-backlog/{productBacklogId}` - Lister par product backlog
- `DELETE /api/epics/{id}` - Supprimer un epic

### 3. UserStoryService et UserStoryController

**Fonctionnalités implémentées :**
- ✅ Ajout, modification et suppression des User Stories
- ✅ Lien avec des Epics (optionnel)
- ✅ Définition des critères d'acceptation
- ✅ Suivi du statut (Active, In Progress, Completed, Inactive)
- ✅ Priorisation avec ordre numérique et priorité MoSCoW
- ✅ Gestion des Story Points et valeur métier
- ✅ Déplacement vers Sprint Backlog
- ✅ Tri automatique par priorité

**Endpoints REST :**
- `POST /api/user-stories` - Créer une user story
- `PUT /api/user-stories/{id}` - Modifier une user story
- `GET /api/user-stories/{id}` - Récupérer une user story
- `GET /api/user-stories` - Lister toutes les user stories
- `GET /api/user-stories/product-backlog/{productBacklogId}` - Lister par product backlog (triées par priorité)
- `GET /api/user-stories/epic/{epicId}` - Lister par epic
- `GET /api/user-stories/sprint-backlog/{sprintBacklogId}` - Lister par sprint
- `PATCH /api/user-stories/{id}/priority?priorityOrder={order}` - Modifier la priorité
- `PATCH /api/user-stories/{id}/move-to-sprint?sprintBacklogId={id}` - Déplacer vers un sprint
- `DELETE /api/user-stories/{id}` - Supprimer une user story

### 4. TaskService et TaskController

**Fonctionnalités implémentées :**
- ✅ Création et gestion des Tasks
- ✅ Association avec User Stories
- ✅ Suivi du statut (To Do, In Progress, Done, Blocked)
- ✅ Gestion des heures estimées, réelles et restantes
- ✅ Assignment aux développeurs
- ✅ Lien automatique avec Sprint Backlog de la User Story
- ✅ Ordre des tâches

**Endpoints REST :**
- `POST /api/tasks` - Créer une tâche
- `PUT /api/tasks/{id}` - Modifier une tâche
- `GET /api/tasks/{id}` - Récupérer une tâche
- `GET /api/tasks` - Lister toutes les tâches
- `GET /api/tasks/user-story/{userStoryId}` - Lister par user story (triées par ordre)
- `GET /api/tasks/sprint-backlog/{sprintBacklogId}` - Lister par sprint backlog
- `GET /api/tasks/assigned-to/{userId}` - Lister par utilisateur assigné
- `PATCH /api/tasks/{id}/status?status={status}` - Modifier le statut
- `DELETE /api/tasks/{id}` - Supprimer une tâche

## DTOs Créés

### Product Backlog
- `ProductBacklogDTO` - Représentation complète
- `CreateProductBacklogRequest` - Création avec validation

### Epic
- `EpicDTO` - Représentation complète avec compteur de user stories
- `CreateEpicRequest` - Création avec validation

### User Story
- `UserStoryDTO` - Représentation complète avec toutes les relations
- `CreateUserStoryRequest` - Création avec validation complète des priorités
- `UpdateUserStoryRequest` - Mise à jour partielle

### Task
- `TaskDTO` - Représentation complète avec assignation
- `CreateTaskRequest` - Création avec validation
- `UpdateTaskRequest` - Mise à jour partielle

## Fonctionnalités de Priorisation Implémentées

### Critères de Priorisation
1. **Priorité MoSCoW** - Enum Priority (MUST_HAVE, SHOULD_HAVE, COULD_HAVE, WONT_HAVE)
2. **Ordre de Priorité** - Champ priorityOrder pour tri numérique
3. **Valeur Métier** - businessValue (1-10)
4. **Story Points** - Estimation de complexité
5. **Heures Estimées** - Pour planification détaillée

### Méthodes de Tri
- Tri automatique par `priorityOrder ASC, businessValue DESC`
- Méthode dédiée pour mise à jour de la priorité
- Repository custom pour requêtes optimisées

## Validations Implémentées

- ✅ Validation des champs obligatoires (@NotBlank, @NotNull)
- ✅ Validation des valeurs numériques (@Min, @Max)
- ✅ Validation de la valeur métier (1-10)
- ✅ Validation des heures (positives)
- ✅ Validation de l'unicité (Product Backlog par projet)

## Gestion des Relations

- ✅ User Story → Epic (optionnel)
- ✅ User Story → Product Backlog (obligatoire)
- ✅ User Story → Sprint Backlog (optionnel, par déplacement)
- ✅ Task → User Story (obligatoire)
- ✅ Task → Sprint Backlog (automatique via User Story)
- ✅ Task → User (assignation optionnelle)

## Gestion Transactionnelle

- ✅ @Transactional sur toutes les opérations d'écriture
- ✅ @Transactional(readOnly = true) sur les lectures
- ✅ Cascade approprié sur les relations
- ✅ Orphan removal sur les tâches

## Points Techniques

### Repositories
- Méthodes personnalisées pour filtrage et tri
- Utilisation de @Query pour requêtes complexes
- Méthodes dérivées pour cas simples

### Services
- Conversion DTO ↔ Entity dans les services
- Gestion centralisée des exceptions
- Validation métier (ex: unicité du Product Backlog)

### Contrôleurs
- RESTful API avec codes HTTP appropriés
- Validation automatique avec @Valid
- ResponseEntity pour retours structurés

## Corrections Apportées

1. ✅ Correction de l'encodage UTF-8 dans application.properties
2. ✅ Ajout de spring-boot-starter-validation au pom.xml
3. ✅ Correction des noms de propriétés User (firstname/lastname)
4. ✅ Ajout de méthodes manquantes dans repositories
5. ✅ Nettoyage du code dupliqué

## Prochaines Étapes Suggérées

1. Implémenter SprintBacklogService et Controller
2. Ajouter la gestion des commentaires
3. Implémenter la sécurité avec Spring Security
4. Ajouter des tests unitaires et d'intégration
5. Implémenter les fonctionnalités de reporting (burndown chart)
6. Ajouter des endpoints pour statistiques et métriques

## Compilation

Le projet compile correctement avec Maven :
```bash
./mvnw.cmd compile -DskipTests
```

Résultat : ✅ BUILD SUCCESS

