# Cahier des Charges

## Application de Gestion de Projets Agile

---

## 1. Introduction

L'objectif de cette application est de faciliter la gestion de projets selon la méthode **Agile**, en permettant de suivre le développement de nouvelles fonctionnalités à travers des **User Stories**, organisées en **Epics** et stockées dans un **Product Backlog**.

Les User Stories seront ensuite sélectionnées et traitées dans le cadre de **Sprints** à travers un **Sprint Backlog**.

L'application devra permettre au **Product Owner**, aux **Développeurs** et aux **Scrum Masters** de suivre l'évolution des tâches tout au long du projet.

---

## 2. Objectifs

* Suivre le **Product Backlog** et les **Sprint Backlogs** d'un projet Agile
* Organiser les **User Stories** en **Epics**
* Prioriser les User Stories en fonction de leur importance
* Gérer les **Tasks** associées à chaque User Story
* Affecter les tâches aux développeurs selon leur disponibilité et leurs compétences
* Assurer le suivi de l’état d’avancement des User Stories et des Tasks
  *(To Do, In Progress, Done)*
* Permettre la gestion et le suivi des **Sprints**

---

## 3. Public Cible

* **Product Owner**

  * Gestion du Product Backlog
  * Priorisation des User Stories
  * Définition des Epics

* **Scrum Master**

  * Suivi du déroulement des Sprints
  * Gestion de l’avancement des tâches

* **Développeurs**

  * Mise à jour des User Stories
  * Gestion des tâches selon l’avancement

---

## 4. Fonctionnalités

### 4.1 Gestion du Product Backlog

* Création et gestion des Product Backlogs
* Ajout, modification et suppression des User Stories
* Priorisation des User Stories
* Association des User Stories à des Epics

---

### 4.2 Critères pour Prioriser un Backlog

#### Facteurs de Priorisation

1. **Valeur Métier** : Impact sur les utilisateurs ou l’entreprise
2. **Urgence** : Nécessité pour une prochaine livraison ou dépendance
3. **Complexité / Coût** : Difficulté d’implémentation (Story Points)
4. **Risques** : Réduction des incertitudes techniques ou fonctionnelles
5. **Dépendances** : Déblocage d’autres fonctionnalités

#### Techniques de Priorisation

* **MoSCoW**

  * Must Have
  * Should Have
  * Could Have
  * Won’t Have

* **Valeur vs Effort**

* **WSJF (Weighted Shortest Job First)**
  *(Utilisé dans SAFe – basé sur la valeur business et le temps d’attente)*

---

### 4.3 Gestion des Epics

* Création et gestion des Epics
* Association des User Stories aux Epics *(optionnelle mais recommandée)*
* Visualisation des User Stories par Epic

---

### 4.4 Gestion du Sprint Backlog

* Création et gestion des Sprints
* Sélection des User Stories depuis le Product Backlog
* Gestion des Tasks associées aux User Stories
* Suivi des statuts :

  * To Do
  * In Progress
  * Done

---

### 4.5 Gestion des User Stories

* Ajout, modification et suppression des User Stories
* Association avec des Epics
* Définition des critères d’acceptation
* Suivi du statut des User Stories
* Priorisation dans le Product Backlog

---

### 4.6 Gestion des Tasks

* Création et gestion des Tasks
* Suivi du statut des Tasks :

  * To Do
  * In Progress
  * Done

---

## 5. Architecture Technique

### 5.1 Backend

* **Framework** : Spring Boot
* **Langage** : Java
* **Base de données** : MySQL / PostgreSQL
* **Gestion des transactions** : Spring Transaction Management
* **Sécurité** : Spring Security (JWT, rôles utilisateurs)
* **API** : RESTful API (Spring MVC)
* **Tests** :

  * JUnit
  * Mockito

---

## 6. Modèle de Données

### 6.1 Entités Principales

* **UserStory**

  * titre
  * description
  * priorité
  * statut
  * epic lié
  * ProductBacklog
  * SprintBacklog

* **Epic**

  * titre
  * description
  * liste des User Stories

* **ProductBacklog**

  * nom
  * liste des Epics
  * liste des User Stories

* **SprintBacklog**

  * nom
  * liste des User Stories
  * liste des Tasks

* **Task**

  * titre
  * description
  * statut
  * UserStory associée

---

### 6.2 Relations entre Entités

* Une **UserStory** peut être associée à un **Epic**
* Une **UserStory** appartient à un **ProductBacklog**
* Une **UserStory** peut être déplacée vers un **SprintBacklog**
* Un **SprintBacklog** contient plusieurs **UserStories** et **Tasks**

---

### 6.3 Product Backlog

* Liste des User Stories
* Filtres, tri et priorisation
* Gestion des User Stories et des Epics

---

### 6.4 Sprint Backlog

* Liste des User Stories du Sprint courant
* Visualisation de l’état des Tasks associées

---

### 6.5 User Stories & Tasks

* Interface CRUD pour User Stories
* Vue détaillée des Tasks associées

---

## 7. Gestion des Utilisateurs

* **Authentification**

  * Inscription
  * Connexion

* **Rôles**

  * Product Owner
  * Scrum Master
  * Développeur

* **Permissions**

  * Product Owner : User Stories, Epics
  * Scrum Master : Sprints
  * Développeurs : Tasks

---

## 8. Suivi et Reporting

* **Suivi des progrès**

  * Burndown Chart
  * Avancement des Sprints

* **Historique des Sprints**

  * User Stories terminées
  * En cours
  * À venir

* **Reporting personnalisé**

  * Tableau de bord par utilisateur

---
