## Guide d'Installation et d'Exécution

Ce guide détaille les étapes nécessaires pour lancer l'application complète (Base de données, Backend API, Frontend et Outils d'observabilité).

### 1\. Prérequis

Assurez-vous d'avoir installé les outils suivants sur votre machine :

* **Java JDK 17** (ou version supérieure).
* **Docker** et **Docker Compose**.
* **Node.js** (version 16 ou supérieure) et **npm**.

### 2\. Démarrage de l'Infrastructure (Docker)

Nous utilisons Docker pour héberger la base de données MongoDB et l'outil de tracing Jaeger.

1.  Ouvrez un terminal à la racine du projet.
2.  Lancez les conteneurs :
    ```bash
    docker-compose up -d
    ```
3.  Vérifiez que les services sont actifs :
    * **Jaeger UI :** Accessible sur [http://localhost:16686](https://www.google.com/search?q=http://localhost:16686).
    * **MongoDB :** Accessible sur le port `27017`.

### 3\. Lancement du Backend (Spring Boot)

Le backend expose l'API REST et génère les logs pour le profilage utilisateur.

1.  Ouvrez un terminal dans le dossier du projet backend (là où se trouve `build.gradle`).

2.  Assurez-vous que l'instrumentation Spoon a été exécutée (si nécessaire) via la classe `SpoonRunner`.

3.  Lancez l'application :

    ```bash
    ./gradlew bootRun
    ```

    *(Sous Windows : `gradlew.bat bootRun`)*

4.  L'application démarrera sur le port `8080`.

    * Le script de scénario (`ScenarioExecutor`) s'exécutera automatiquement au démarrage pour générer des données de test.

### 4\. Lancement du Frontend

Le frontend est une application web légère servant d'interface utilisateur.

1.  Ouvrez un terminal dans le dossier `frontend`.
2.  Installez les dépendances (première fois uniquement) :
    ```bash
    npm install
    ```
3.  Lancez le serveur de développement :
    ```bash
    npm run dev
    ```
4.  Ouvrez l'URL indiquée dans le terminal (généralement [http://localhost:5173](https://www.google.com/search?q=http://localhost:5173)) dans votre navigateur.

### 5\. Vérification de l'Observabilité

Une fois les trois composantes lancées :

1.  **Profilage (Exercice 1) :**

    * Les logs sont générés dans le fichier `application.json` à la racine du backend.
    * Pour voir les profils utilisateurs, exécutez la classe Java `LogAnalyzer`.

2.  **Tracing (Exercice 2) :**

    * Utilisez l'interface web pour cliquer sur les boutons "Get All Products" ou "Add Product".
    * Allez sur **Jaeger** ([http://localhost:16686](https://www.google.com/search?q=http://localhost:16686)).
    * Sélectionnez le service `tp3-backend` (ou `tp3-observability`) pour voir les traces des opérations API et Base de données.
    * *(Note : La connexion Frontend-\>Backend n'est pas visible suite aux limitations techniques rencontrées).*