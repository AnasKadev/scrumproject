# 🚀 Application de Gestion de Projets Agile Scrum

Une application complète de gestion de projets selon la méthode Agile Scrum, développée avec Spring Boot.

## 📋 Table des Matières

- [Aperçu](#aperçu)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [API Endpoints](#api-endpoints)
- [Modèle de Données](#modèle-de-données)
- [Documentation Complète](#documentation-complète)

## 🎯 Aperçu

Cette application facilite la gestion de projets selon la méthode Agile en permettant de :
- Suivre le développement de nouvelles fonctionnalités via User Stories
- Organiser les User Stories en Epics dans un Product Backlog
- Gérer les sprints via des Sprint Backlogs
- Assigner et suivre les tâches
- Générer des rapports et statistiques (burndown chart, vélocité, etc.)

## ✨ Fonctionnalités

### 🎯 Gestion Complète du Backlog
- **Product Backlog** : Gestion centralisée de toutes les fonctionnalités
- **Epics** : Regroupement logique des User Stories
- **User Stories** : Histoires utilisateur avec critères d'acceptation
- **Priorisation** : MoSCoW + ordre numérique + valeur métier

### 🏃 Gestion des Sprints
- Création et gestion de sprints (PLANNED → ACTIVE → COMPLETED/CANCELLED)
- Sélection des User Stories depuis le Product Backlog
- Suivi en temps réel de l'avancement
- Gestion des Tasks par User Story
- Statistiques détaillées par sprint

### 📊 Reporting et Statistiques
- **Burndown Chart** : Visualisation de l'avancement du sprint
- **Vélocité** : Calcul automatique par sprint et moyenne projet
- **Taux de complétion** : User Stories et Tasks
- **Statistiques détaillées** : Story points, heures, progression

### 👥 Gestion d'Équipe
- Gestion des utilisateurs (Product Owner, Scrum Master, Developer)
- Assignation aux projets
- Assignation des tâches aux développeurs
- Commentaires collaboratifs

## 🚀 Installation

### Prérequis
- Java 17 ou supérieur
- MySQL 8.0+ ou PostgreSQL 13+
- Maven 3.6+ (ou utiliser le wrapper fourni)

### Étapes d'Installation

1. **Cloner le repository**
```bash
git clone <repository-url>
cd scrum
```

2. **Configurer la base de données**
```sql
CREATE DATABASE scrum;
CREATE SCHEMA scrum;
```

3. **Configurer application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/scrum
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

4. **Lancer l'application**
```bash
./mvnw spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 📚 Documentation Complète

Pour une documentation détaillée, consultez :
- [IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md) - Documentation technique complète
- [cahierdecharge.md](cahierdecharge.md) - Cahier des charges original

## 🔌 API Endpoints Principaux

### Projets
- `POST /api/projects` - Créer un projet
- `GET /api/projects` - Liste des projets

### User Stories
- `POST /api/user-stories` - Créer une user story
- `GET /api/user-stories` - Liste des user stories
- `PATCH /api/user-stories/{id}/move-to-sprint/{sprintId}` - Déplacer vers un sprint

### Sprints
- `POST /api/sprint-backlogs` - Créer un sprint
- `PATCH /api/sprint-backlogs/{id}/start` - Démarrer un sprint
- `GET /api/sprint-backlogs/{id}/statistics` - Statistiques du sprint

### Reporting
- `GET /api/reports/sprints/{id}` - Rapport complet du sprint (burndown chart)
- `GET /api/reports/projects/{id}` - Rapport complet du projet

---

**Version** : 1.0.0  
**Date** : Janvier 2026  
**Statut** : ✅ Production Ready

