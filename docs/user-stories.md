# Analyse des User Stories - Student Management Console App

## 1. Contexte projet
- Objectif : application Java console pour gerer etudiants, cours et calculs, avec persistance fichier.
- Pile technique : Java 17+ (Maven), packages `hamza.patient.net.*`, stockage texte.
- Composants actuels : configuration (`config.AppConfig`), console (`console`), domaine (`domain`), services (`service`), repository fichier (`repository.file`), utilitaires (`util`), entree (`Main`).

## 2. User stories deja couvertes

| US | Description | Packages / Classes | Etat du code |
| --- | --- | --- | --- |
| TASK-SETUP-000 | Projet Maven initialise avec arborescence standard | `pom.xml`, `src/main/java`, packages `hamza.patient.net.*`, `config.AppConfig` | Pret |
| US-BOOT-001 | Lancer l'application console et demarrer le menu | `hamza.patient.net.Main`, `config.AppConfig`, `console.ConsoleApplication`, `console.Menu` | Demarrage fonctionnel, menu boucle sans actions |
| US-CONF-002 | Choisir l'emplacement du fichier `students.txt` via config/ENV/VM args | `config.AppConfig#resolveDataPath`, `config.AppConfig#ensureParentExists` | Resolution hierarchique operationnelle |
| US-LOG-003 | Tracer l'application avec niveaux et couleurs ANSI | `util.Logger` | Implementé complet (configurable) |

## 3. User stories partiellement implementées

| US | Description | Packages / Classes | Elements manquants |
| --- | --- | --- | --- |
| US-MENU-010 | Boucle menu console | `console.Menu`, `console.StudentViews`, `console.CourseViews`, `console.CalculationViews` | Menu affiche rien, switch non branche |
| US-WIRING-011 | Assemblage des composants | `config.AppConfig#wire` | Wiring pret mais depend de services/repos vides |
| US-AVG-020 | Strategie calcul moyenne | `service.strategy.AverageStrategy`, `service.strategy.SimpleAverageStrategy` | Interface presente, calcul non code, pas injecte |

## 4. User stories non implementées (decoupage par package)

### 4.1 Gestion des etudiants
- **US-STU-DOM-101** – Modele Student complet (constructeurs, accessors, equals/hashCode/toString)  
  - Package : `domain` (`Student`).  
  - Etat : uniquement champs declares.
- **US-STU-UTIL-102** – Outils d'identifiant et validations basiques  
  - Package : `util` (`IdGenerator`, `Validation`).  
  - Etat : classes vides.
- **US-STU-SVC-103** – Logique CRUD et recherche des etudiants  
  - Package : `service` (`StudentService`).  
  - Etat : methods retournent `null` ou `List.of()`.
- **US-STU-UI-104** – Vues console pour operations etudiant  
  - Package : `console` (`StudentViews`).  
  - Etat : aucune interaction avec l'utilisateur.

### 4.2 Gestion des cours et notes
- **US-COURSE-DOM-201** – Modele CourseNote (champs, operations equals/hashCode)  
  - Package : `domain` (`CourseNote`, ajustements `Student`).  
  - Etat : getters/setters manquants, pas de gestion de liste.
- **US-COURSE-SVC-202** – Manipuler les notes de cours dans le service  
  - Package : `service` (`CourseNoteService`).  
  - Etat : methods vides.
- **US-COURSE-UI-203** – Vue console pour gerer les cours d'un etudiant  
  - Package : `console` (`CourseViews`).  
  - Etat : aucune saisie ni affichage.

### 4.3 Calculs et reporting
- **US-CALC-SVC-301** – Calculer moyenne / meilleur / echecs  
  - Package : `service` (`CalculationService`).  
  - Etat : retours `0.0`, `Optional.empty()`, `List.of()`.
- **US-CALC-STRAT-302** – Strategie de moyenne simple (utilisee par le service)  
  - Package : `service.strategy` (`SimpleAverageStrategy`).  
  - Etat : retourne `0.0`, non relie au service.
- **US-CALC-UI-303** – Interface console pour afficher resultats  
  - Package : `console` (`CalculationViews`).  
  - Etat : aucune logique d'affichage.

### 4.4 Persistance fichier
- **US-DATA-SER-401** – Serialisation TXT (parse/format)  
  - Package : `repository.file` (`TxtSerializer`).  
  - Etat : renvoie toujours `List.of()`.
- **US-DATA-REPO-402** – Chargement initial et sauvegarde sur disque  
  - Package : `repository.file` (`FileStudentRepository#loadAll`, `#saveAll`).  
  - Etat : corps vides.
- **US-DATA-REPO-403** – CRUD en memoire et persistence automatique  
  - Package : `repository.file` (`FileStudentRepository#findAll/#findById/#create/#update/#delete`).  
  - Etat : stubs retournent valeurs neutres.

### 4.5 Validation et erreurs
- **US-UTIL-501** – Generation d'identifiants uniques  
  - Package : `util` (`IdGenerator`).  
  - Etat : aucune implementation.
- **US-UTIL-502** – Validation des entrees (emails, chaines, bornes)  
  - Package : `util` (`Validation`).  
  - Etat : aucune methode.
- **US-ERR-503** – Exceptions metier dediees  
  - Package : `exceptions`.  
  - Etat : dossier vide.

### 4.6 Bonus optionnels
- **US-BONUS-601** – Recherche avancee ou import/export CSV  
  - Packages potentiels : `service`, `console`, `repository.file`.  
  - Etat : aucun debut.
- **US-BONUS-602** – Interface console enrichie (formatage, validation interactive)  
  - Package : `console`.  
  - Etat : a concevoir.
- **US-BONUS-603** – Strategies supplementaires ou autres design patterns  
  - Package : `service.strategy`.  
  - Etat : interface prete mais pas d'alternatives.

### 4.7 Traçabilite projet
- **US-JIRA-701** – Support JIRA (lien board ou captures)  
  - Livrables attendus : doc README, captures.  
  - Etat : rien dans le depot.

## 5. Dependances par package / classe

| Package | Role | Classes clefs | Etat |
| --- | --- | --- | --- |
| `hamza.patient.net` | Entree application | `Main` | Fonctionnel |
| `config` | Wiring et configuration | `AppConfig` | Operationnel |
| `console` | Interface utilisateur texte | `ConsoleApplication`, `Menu`, `StudentViews`, `CourseViews`, `CalculationViews` | Menu partiel, vues vides |
| `domain` | Modele metier | `Student`, `CourseNote` | Champs declares, aucune methode |
| `repository` | Abstraction persistance | `StudentRepository` | Interface definie |
| `repository.file` | Persistance disque | `FileStudentRepository`, `TxtSerializer` | Stubs |
| `service` | Logique metier | `StudentService`, `CourseNoteService`, `CalculationService` | Stubs |
| `service.strategy` | Calculs extensibles | `AverageStrategy`, `SimpleAverageStrategy` | Non utilises |
| `util` | Utilitaires | `Logger`, `IdGenerator`, `Validation` | Logger OK, autres vides |
| `exceptions` | Gestion d'erreurs | *(vide)* | Aucun fichier |

## 6. Prochaines etapes recommandees
1. Modeles et utilitaires : finaliser `Student`, `CourseNote`, `IdGenerator`, `Validation`, definir exceptions.
2. Persistance TXT : implementer `TxtSerializer` et `FileStudentRepository` (charge, sauvegarde, cache).
3. Services metier : coder CRUD, gestion des notes, calculs de moyenne/indicateurs.
4. Interface console : developper les vues et le menu pour orchestrer les services.
5. Tests et documentation : ajouter tests unitaires, README d'execution, references JIRA.
6. Bonus : rechercher/import CSV, strategies alternatives, ameliorations UX console.

---

Ce document sert de reference pour convertir chaque user story en ticket JIRA cible, avec le package principal et l'etat actuel identifie.
