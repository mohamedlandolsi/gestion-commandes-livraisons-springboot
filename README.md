# Système de Gestion des Commandes et Livraisons

Une application web complète développée avec Spring Boot (Back-end) et Angular (Front-end) pour la gestion des commandes et livraisons.

## Description du projet

Cette application permet de gérer l'ensemble du processus de commande et de livraison, incluant:
- Création et validation des bons de commande
- Suivi des commandes et livraisons
- Gestion des paiements en ligne
- Mise à jour automatique des stocks à la réception
- Historique des commandes par fournisseur

## Technologies utilisées

### Back-end
- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Security
- MySQL

### Front-end (à implémenter)
- Angular
- Bootstrap

## Installation et exécution

### Prérequis
- Docker et Docker Compose installés sur votre machine
- Git

### Avec Docker (recommandé)

1. Clonez le dépôt:
```
git clone https://gitlab.com/mohamedlandolsi/gestion-commandes-livraisons.git
cd gestion-commandes-livraisons
```

2. Lancez l'application avec Docker Compose:
```
docker-compose up -d
```

L'application sera accessible à l'adresse: http://localhost:8080

Pour arrêter l'application:
```
docker-compose down
```

Pour voir les logs:
```
docker-compose logs -f app
```

### Sans Docker

1. Assurez-vous d'avoir Java 17+ et Maven installés

2. Clonez le dépôt:
```
git clone https://gitlab.com/mohamedlandolsi/gestion-commandes-livraisons.git
cd gestion-commandes-livraisons
```

3. Configurez votre base de données MySQL dans `src/main/resources/application.properties`

4. Lancez l'application:
```
mvn spring-boot:run
```

## Structure du projet

- `src/main/java/itbs/mohamedlandolsi/gestioncommandeslivraisons/`
  - `model/` - Entités JPA (Commande, Livraison, etc.)
  - `repository/` - Repositories Spring Data JPA
  - `service/` - Logique métier
  - `controller/` - API REST endpoints
  - `exception/` - Gestion d'exceptions
  - `config/` - Configuration de l'application

## API REST

L'API REST est disponible à l'URL de base `/api` avec les endpoints suivants:

### Commandes
- `GET /api/commandes` - Liste toutes les commandes
- `GET /api/commandes/{id}` - Détails d'une commande
- `POST /api/commandes` - Crée une nouvelle commande
- `PATCH /api/commandes/{id}/status` - Met à jour le statut d'une commande

### Livraisons
- `GET /api/livraisons` - Liste toutes les livraisons
- `GET /api/livraisons/{id}` - Détails d'une livraison
- `PATCH /api/livraisons/{id}/status` - Met à jour le statut d'une livraison
- `PATCH /api/livraisons/{id}/transporteur` - Assigne un transporteur

### Fournisseurs
- `GET /api/fournisseurs` - Liste tous les fournisseurs
- `GET /api/fournisseurs/{id}/commandes` - Historique des commandes par fournisseur

### Clients
- `GET /api/clients` - Liste tous les clients
- `GET /api/clients/{id}` - Détails d'un client
- `POST /api/clients` - Crée un nouveau client
- `PUT /api/clients/{id}` - Met à jour un client existant
- `DELETE /api/clients/{id}` - Supprime un client

### Produits
- `GET /api/produits` - Liste tous les produits
- `GET /api/produits/{id}` - Détails d'un produit
- `POST /api/produits` - Crée un nouveau produit
- `PUT /api/produits/{id}` - Met à jour un produit existant
- `DELETE /api/produits/{id}` - Supprime un produit

### Lignes de Commande
- `GET /api/lignecommandes` - Liste toutes les lignes de commande
- `GET /api/lignecommandes/{id}` - Détails d'une ligne de commande
- `POST /api/lignecommandes` - Crée une nouvelle ligne de commande (généralement associée à une commande existante)
- `PUT /api/lignecommandes/{id}` - Met à jour une ligne de commande existante
- `DELETE /api/lignecommandes/{id}` - Supprime une ligne de commande

### Paiements
- `GET /api/paiements` - Liste tous les paiements
- `GET /api/paiements/{id}` - Détails d'un paiement
- `POST /api/paiements` - Enregistre un nouveau paiement (généralement associé à une commande)
- `GET /api/commandes/{commandeId}/paiements` - Liste les paiements pour une commande spécifique

### Transporteurs
- `GET /api/transporteurs` - Liste tous les transporteurs
- `GET /api/transporteurs/{id}` - Détails d'un transporteur
- `POST /api/transporteurs` - Crée un nouveau transporteur
- `PUT /api/transporteurs/{id}` - Met à jour un transporteur existant
- `DELETE /api/transporteurs/{id}` - Supprime un transporteur

## Variables d'environnement

Les variables d'environnement suivantes peuvent être configurées:

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| SPRING_DATASOURCE_URL | URL de connexion à la base de données | jdbc:mysql://localhost:3306/gestion_commandes_livraisons?createDatabaseIfNotExist=true |
| SPRING_DATASOURCE_USERNAME | Nom d'utilisateur MySQL | root |
| SPRING_DATASOURCE_PASSWORD | Mot de passe MySQL | (vide) |
| PORT | Port du serveur | 8080 |
| LOGGING_LEVEL | Niveau de logs | INFO |

## Contributeurs

- Mohamed Landolsi
