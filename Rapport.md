# Rapport TP3
## NGO Hoai Nguyen - ICo
### Dépôt Github:
* Backend : https://github.com/nguyen703/TP3-Observability
* Frontend : https://github.com/nguyen703/TP3-Observability-frontend

## Exercice 1 : Profilage des utilisateurs via les logs

### 1. Architecture

* **Base de données :** MongoDB (script `docker-compose` dans le repo).
* **Logging :** SLF4J avec Logback (dans spring-boot-starter-web).
* **Instrumentation :** Spoon.
* **Format des logs :** JSON.

### 2. Implémentation

#### A. Modèles et Services (Backend)

J'ai créé deux modèles principaux : `User` et `Product`.
L'API permet les opérations suivantes :

* Récupérer un produit (READ).
* Ajouter, modifier ou supprimer un produit (WRITE). Des exceptions sont levées si les IDs existent déjà ou sont introuvables.

#### B. Instrumentation automatique (Spoon)

Pour la Question 3, je n'ai pas écrit les logs manuellement. J'ai utilisé la librairie **Spoon** pour modifier le code automatiquement.

* Le script `LoggingProcessor` détecte si une méthode est de type "READ" (`getProduct`) ou "WRITE" (`addProduct`).
* Il injecte une ligne de log au début de la méthode avec l'ID de l'utilisateur.
* **Format du log :** `{"user": "...", "type": "READ/WRITE", "method": "..."}`.

#### C. Simulation de scénarios

Pour la Question 4, j'ai créé un `ScenarioExecutor`. Ce script se lance au démarrage et simule 10 utilisateurs :

* **Readers :** 90% de chance de lire des données.
* **Writers :** Majorité d'opérations d'écriture.
* **Random :** Comportement mixte.

### 3. Analyse et Résultats

#### Méthode d'analyse

Un programme `LogAnalyzer` (Question 5) lit le fichier `application.json`. Il utilise le *Builder Pattern* pour compter les actions de chaque utilisateur et déterminer son profil.

#### Résultats des Logs

Voici la sortie console obtenue après l'exécution des scénarios et de l'analyseur :

```text
--- ANALYZING USER PROFILES ---
User: User_Random_1   | Reads: 14  | Writes: 6   | Profile: HEAVY READER
User: User_Random_3   | Reads: 12  | Writes: 8   | Profile: REGULAR / BALANCED
User: User_Reader_4   | Reads: 14  | Writes: 6   | Profile: HEAVY READER
User: User_Random_2   | Reads: 9   | Writes: 11  | Profile: REGULAR / BALANCED
User: User_Reader_3   | Reads: 14  | Writes: 6   | Profile: HEAVY READER
User: User_Writer_2   | Reads: 3   | Writes: 17  | Profile: HEAVY WRITER
User: User_Reader_2   | Reads: 18  | Writes: 2   | Profile: HEAVY READER
User: User_Writer_3   | Reads: 2   | Writes: 18  | Profile: HEAVY WRITER
User: User_Reader_1   | Reads: 19  | Writes: 1   | Profile: HEAVY READER
User: User_Writer_1   | Reads: 4   | Writes: 16  | Profile: HEAVY WRITER
User: System_Seeder   | Reads: 20  | Writes: 20  | Profile: REGULAR / BALANCED
```

Voici la version mise à jour du rapport pour l'Exercice 2, reflétant honnêtement les difficultés rencontrées avec la partie Frontend tout en valorisant la réussite de la partie Backend.

***

## Exercice 2 : Tracing d'une application web (OpenTelemetry)

### 1. Architecture

* **Frontend :** JavaScript Vanilla avec Vite.
* **Visualisation :** Jaeger (ajouté au `docker-compose`).
* **Protocole :** OpenTelemetry (OTLP via HTTP sur le port 4318).
* **Communication :** REST API (Frontend vers Backend).

### 2. Implémentation Réalisée

#### A. Interface Web (Frontend)

J'ai développé une interface minimaliste (`index.html`) exposant deux fonctionnalités pour consommer l'API du backend :
* Un bouton **"Get All Products"** (Requête GET).
* Un bouton **"Add Random Product"** (Requête POST).
  L'application fonctionne correctement et communique avec le Backend.

#### B. Instrumentation Backend (Bonus)

Pour la Question 4 (Bonus), j'ai instrumenté le Backend Spring Boot avec succès.
* Ajout des dépendances `micrometer-tracing` et `opentelemetry-exporter-otlp`.
* Configuration de `application.properties` pour envoyer les traces vers Jaeger.
* **Résultat :** Les traces des opérations API et MongoDB apparaissent bien dans Jaeger.

### 3. Difficultés sur le Tracing E2E (Frontend)

Malgré mes efforts pour mettre en place le traçage distribué complet ("End-to-End"), je n'ai pas réussi à faire remonter les traces du Frontend dans Jaeger.

Voici les tentatives et corrections effectuées pour tenter de résoudre le problème :
* **Configuration CORS :** Ajout de la variable `COLLECTOR_OTLP_HTTP_CORS_ALLOWED_ORIGINS=*` dans Docker pour autoriser le navigateur.
* **Code JavaScript :** Mise à jour des imports (`Resource`, `SimpleSpanProcessor`) et utilisation de `FetchInstrumentation` pour propager les en-têtes.
* **Débogage Réseau :** Vérification du port 4318 et des requêtes réseaux dans le navigateur.

Bien que le code semble correct et que le serveur Jaeger soit actif (prouvé par les traces Backend), la connexion entre le navigateur et Jaeger ne s'est pas établie.

### 4. Conclusion des résultats

En conséquence, je ne peux visualiser que la partie **Backend** des traces :
1.  **Frontend :** Aucune trace visible.
2.  **Backend & DB :** Les traces sont bien capturées et visibles (Controller -> Service -> Repository).

La visualisation complète de la cascade (Frontend -> Backend) n'a donc pas pu être validée.
