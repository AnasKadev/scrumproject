# 📚 Documentation - MapStruct et Swagger

## Vue d'ensemble

Ce document explique comment utiliser **MapStruct** pour les conversions DTO et **Swagger (OpenAPI)** pour la documentation interactive de l'API.

---

## 🔄 MapStruct - Conversion Automatique des DTOs

### Qu'est-ce que MapStruct ?

MapStruct est un générateur de code qui simplifie les conversions entre objets Java (Entités ↔ DTOs). Il génère automatiquement le code de mapping au moment de la compilation, ce qui est plus performant et plus sûr que la réflexion.

### Mappers Implémentés

Tous les mappers se trouvent dans le package `org.example.scrum.mapper` :

1. **ProjectMapper** - Conversion Project ↔ ProjectDTO
2. **ProductBacklogMapper** - Conversion ProductBacklog ↔ ProductBacklogDTO
3. **EpicMapper** - Conversion Epic ↔ EpicDTO
4. **UserStoryMapper** - Conversion UserStory ↔ UserStoryDTO
5. **SprintBacklogMapper** - Conversion SprintBacklog ↔ SprintBacklogDTO
6. **TaskMapper** - Conversion Task ↔ TaskDTO
7. **CommentMapper** - Conversion Comment ↔ CommentDTO
8. **UserMapper** - Conversion User ↔ UserDTO

### Exemple d'utilisation

#### Avant (Conversion manuelle)
```java
private ProjectDTO convertToDTO(Project project) {
    ProjectDTO dto = new ProjectDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setDescription(project.getDescription());
    dto.setActive(project.isActive());
    // ... beaucoup plus de code
    return dto;
}
```

#### Après (Avec MapStruct)
```java
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper; // Injection automatique
    
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("..."));
        return projectMapper.toDTO(project); // Conversion automatique !
    }
    
    public List<ProjectDTO> getAllProjects() {
        return projectMapper.toDTOList(projectRepository.findAll()); // Liste complète !
    }
}
```

### Fonctionnalités des Mappers

Chaque mapper fournit :
- **`toDTO(Entity)`** : Convertit une entité en DTO
- **`toDTOList(List<Entity>)`** : Convertit une liste d'entités en liste de DTOs
- **`toEntity(DTO)`** : Convertit un DTO en entité (utilisé rarement)

### Mappings Personnalisés

Les mappers gèrent automatiquement :
- Les relations (exemple : `productBacklog.nom` → `productBacklogName`)
- Les calculs (exemple : `getUserStories().size()` → `userStoriesCount`)
- Les expressions complexes (exemple : concatenation nom + prénom)

#### Exemple de mapping personnalisé dans TaskMapper :
```java
@Mapping(target = "assignedToName", 
    expression = "java(task.getAssignedTo() != null ? " +
        "task.getAssignedTo().getFirstname() + \" \" + " +
        "task.getAssignedTo().getLastname() : null)")
```

### Comment ajouter un nouveau mapper ?

1. **Créer l'interface du mapper** :
```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonEntityMapper {
    
    @Mapping(target = "champDTO", source = "champEntity")
    MonEntityDTO toDTO(MonEntity entity);
    
    List<MonEntityDTO> toDTOList(List<MonEntity> entities);
}
```

2. **Compiler le projet** :
```bash
./mvnw clean compile
```

3. **Utiliser le mapper dans votre service** :
```java
@Service
@RequiredArgsConstructor
public class MonService {
    private final MonEntityMapper mapper;
    
    public MonEntityDTO getById(Long id) {
        MonEntity entity = repository.findById(id).orElseThrow();
        return mapper.toDTO(entity);
    }
}
```

### Avantages de MapStruct

✅ **Performance** : Code généré à la compilation (pas de réflexion)  
✅ **Type-safe** : Erreurs de compilation si le mapping est incorrect  
✅ **Maintenabilité** : Moins de code boilerplate à maintenir  
✅ **Lisibilité** : Mapping déclaratif et clair  
✅ **Integration Spring** : Injection automatique avec `componentModel = "spring"`

---

## 📖 Swagger (OpenAPI) - Documentation Interactive de l'API

### Qu'est-ce que Swagger ?

Swagger (OpenAPI) est un standard pour documenter les APIs REST. SpringDoc génère automatiquement une interface web interactive permettant de :
- Visualiser tous les endpoints
- Tester l'API directement depuis le navigateur
- Générer des clients API dans différents langages

### Accès à la Documentation

Une fois l'application lancée, accédez à :

#### 🌐 Interface Swagger UI (Interactive)
```
http://localhost:8080/swagger-ui.html
```

#### 📄 Spécification OpenAPI (JSON)
```
http://localhost:8080/api-docs
```

### Annotations Swagger Utilisées

#### Au niveau du contrôleur

```java
@Tag(name = "Projects", description = "API de gestion des projets Scrum")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    // ...
}
```

#### Au niveau des méthodes

```java
@Operation(
    summary = "Créer un projet",
    description = "Crée un nouveau projet Scrum avec les informations fournies"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Projet créé avec succès",
        content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
    @ApiResponse(responseCode = "400", description = "Données invalides"),
    @ApiResponse(responseCode = "500", description = "Erreur serveur")
})
@PostMapping
public ResponseEntity<ProjectDTO> createProject(
    @Parameter(description = "Informations du projet à créer", required = true)
    @Valid @RequestBody CreateProjectRequest request) {
    // ...
}
```

### Groupes d'API Documentés

| Groupe | Description | Endpoints |
|--------|-------------|-----------|
| **Projects** | Gestion des projets | 8 endpoints |
| **Product Backlogs** | Gestion des backlogs produit | 6 endpoints |
| **Epics** | Gestion des epics | 6 endpoints |
| **User Stories** | Gestion des user stories | 10 endpoints |
| **Sprint Backlogs** | Gestion des sprints | 15 endpoints |
| **Tasks** | Gestion des tâches | 8 endpoints |
| **Users** | Gestion des utilisateurs | 5 endpoints |
| **Comments** | Gestion des commentaires | 7 endpoints |
| **Reports** | Génération de rapports | 3 endpoints |

### Tester l'API avec Swagger UI

1. **Ouvrir Swagger UI** : `http://localhost:8080/swagger-ui.html`

2. **Sélectionner un endpoint** : Cliquer sur un endpoint (ex: `POST /api/projects`)

3. **Essayer l'endpoint** : Cliquer sur "Try it out"

4. **Remplir les paramètres** : 
```json
{
  "name": "Mon Projet Scrum",
  "description": "Description du projet",
  "isActive": true
}
```

5. **Exécuter** : Cliquer sur "Execute"

6. **Voir la réponse** : La réponse s'affiche avec le code HTTP et le body

### Fonctionnalités Swagger UI

✅ **Filtrage** : Rechercher des endpoints par nom  
✅ **Tri** : Trier par tag ou par méthode HTTP  
✅ **Authentification** : Support JWT (à configurer)  
✅ **Téléchargement** : Exporter la spécification OpenAPI  
✅ **Génération de code** : Générer des clients API

### Configuration Personnalisée

Dans `application.properties` :
```properties
# Configuration SpringDoc OpenAPI (Swagger)
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.swagger-ui.filter=true
```

### Comment documenter un nouveau endpoint ?

1. **Ajouter @Tag sur le contrôleur** :
```java
@Tag(name = "Mon API", description = "Description de mon API")
@RestController
public class MonController {
```

2. **Ajouter @Operation sur la méthode** :
```java
@Operation(summary = "Créer une ressource", 
           description = "Description détaillée")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Créé"),
    @ApiResponse(responseCode = "400", description = "Erreur")
})
@PostMapping
public ResponseEntity<DTO> create(
    @Parameter(description = "Description du paramètre")
    @RequestBody Request request) {
    // ...
}
```

3. **Compiler et relancer** :
```bash
./mvnw spring-boot:run
```

4. **Vérifier dans Swagger UI** : `http://localhost:8080/swagger-ui.html`

---

## 🚀 Démarrage Rapide

### 1. Compiler le projet (génération MapStruct)
```bash
./mvnw clean compile
```

### 2. Lancer l'application
```bash
./mvnw spring-boot:run
```

### 3. Accéder à Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 4. Tester un endpoint
1. Ouvrir le groupe "Projects"
2. Cliquer sur `POST /api/projects`
3. Cliquer sur "Try it out"
4. Entrer les données JSON
5. Cliquer sur "Execute"
6. Voir la réponse

---

## 📊 Avantages de l'Approche

### MapStruct
- ⚡ **Performance** : ~10x plus rapide que les alternatives basées sur la réflexion
- 🛡️ **Type Safety** : Erreurs détectées à la compilation
- 🧹 **Code propre** : Moins de code boilerplate
- 🔧 **Maintenabilité** : Facile à modifier et à étendre

### Swagger
- 📚 **Documentation automatique** : Toujours à jour avec le code
- 🧪 **Tests interactifs** : Tester l'API sans Postman
- 🤝 **Collaboration** : Partager facilement avec l'équipe frontend
- 🌍 **Standard industriel** : Format OpenAPI reconnu mondialement

---

## 🔍 Vérification

### Vérifier que MapStruct fonctionne

Chercher les fichiers générés :
```
target/generated-sources/annotations/org/example/scrum/mapper/
├── ProjectMapperImpl.java
├── EpicMapperImpl.java
├── UserStoryMapperImpl.java
└── ...
```

### Vérifier que Swagger fonctionne

1. Démarrer l'application
2. Ouvrir `http://localhost:8080/swagger-ui.html`
3. Vérifier que tous les groupes d'API sont visibles
4. Tester un endpoint simple (ex: GET /api/projects)

---

## 🎓 Ressources

### MapStruct
- Documentation officielle : https://mapstruct.org/
- Guide de référence : https://mapstruct.org/documentation/stable/reference/html/

### SpringDoc OpenAPI
- Documentation officielle : https://springdoc.org/
- GitHub : https://github.com/springdoc/springdoc-openapi

### OpenAPI Specification
- Spécification : https://swagger.io/specification/
- Éditeur en ligne : https://editor.swagger.io/

---

## ✅ Checklist d'Intégration

- [x] Dépendances MapStruct ajoutées au pom.xml
- [x] Dépendance SpringDoc OpenAPI ajoutée
- [x] Configuration maven-compiler-plugin avec MapStruct
- [x] 8 interfaces Mapper créées
- [x] Configuration OpenAPIConfig créée
- [x] Annotations Swagger ajoutées aux contrôleurs principaux
- [x] Configuration Swagger dans application.properties
- [x] Compilation réussie avec génération des implémentations MapStruct
- [x] Documentation testée et fonctionnelle

---

**Date de mise à jour** : 22 Janvier 2026  
**Version** : 1.0.0  
**Statut** : ✅ Opérationnel

