# Utiliser une image de base Maven
FROM maven:3.8.4-openjdk-17-slim AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml et le répertoire src dans le conteneur
COPY pom.xml .
COPY src ./src

# Télécharger toutes les dépendances et construire le projet
RUN mvn clean package -DskipTests

# Utiliser une image de base Java pour l'exécution
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR construit dans le conteneur
COPY --from=build /app/target/Projet-Final-Spring_v1.0-0.0.1-SNAPSHOT.jar /app/app.jar

# Exposer le port sur lequel l'application sera accessible
EXPOSE 9798

# Commande pour exécuter l'application
ENTRYPOINT ["java","-jar","/app/app.jar"]


