# Usando imagem do OpenJDK 17
FROM openjdk:17-jdk-slim

# Diretório dentro do container
WORKDIR /app

# Copia o jar gerado para dentro do container
COPY target/cupom-api-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]