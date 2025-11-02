# Aperçu du Projet – Student Management Console App

## Structure générale
- **Application type** : console Java (Maven).
- **Point d’entrée** : `src/main/java/hamza/patient/net/Main.java`.
- **Configuration** : `hamza.patient.net.config.AppConfig` (construction de l’application, gestion du chemin fichier).
- **Interface console** : package `hamza.patient.net.console` (menu et vues, stubs).
- **Domaine métier** : package `hamza.patient.net.domain` (`Student`, `CourseNote` sans implémentation détaillée).
- **Services** : package `hamza.patient.net.service` (CRUD étudiants, gestion notes, calculs – signatures uniquement).
- **Persistance fichier** : interface `StudentRepository` + implémentation `repository.file.FileStudentRepository` et `TxtSerializer` (corps non implémentés).
- **Stratégie de calcul** : package `service.strategy` avec `AverageStrategy`, `SimpleAverageStrategy` (stubs).
- **Utilitaires** : package `hamza.patient.net.util` (`Logger` complet, `IdGenerator` et `Validation` vides).
- **Exceptions** : package `hamza.patient.net.exceptions` vide.

## Ressources et builds
- **Gestionnaire de build** : `pom.xml` (définitions Maven par défaut).
- **Tests** : dossier `src/test/java` présent mais sans classes.
- **Ressources** : `src/main/resources` vide (pas de `application.properties` inclus).

## Documentation existante
- `docs/user-stories.md` – cartographie des user stories et tâches.

> Ce document liste uniquement les artefacts présents, sans détail d’implémentation.

