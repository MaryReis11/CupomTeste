# Usando a imagem do Maven para construir o projeto
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Definindo o diretório de trabalho dentro do container
WORKDIR /app

# Copiando os arquivos do repositório para dentro do container
COPY . .

# Compilando o projeto e criando o JAR
RUN mvn clean package -DskipTests

# Usando a imagem do OpenJDK para rodar a aplicação
FROM eclipse-temurin:17-jdk

# Definindo o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR para o diretório de trabalho do container
COPY target/cupom-api-0.0.1-SNAPSHOT.jar /app/cupom-api.jar

# Expõe a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "cupom-api.jar"]