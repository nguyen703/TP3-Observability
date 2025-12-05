# Rapport TP3
## NGO Hoai Nguyen - ICo

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

