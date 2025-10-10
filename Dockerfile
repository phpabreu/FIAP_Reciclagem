# Build
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /opt/app

# Copia apenas o pom.xml e baixa as dependências para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte do projeto
COPY src ./src

# Executa o build do projeto, gerando o arquivo JAR
RUN mvn clean package

# Runtime
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho no container final
WORKDIR /opt/app

# Copia o JAR gerado na etapa de build para a imagem final
COPY --from=build /opt/app/target/reciclagem-0.0.1-SNAPSHOT.jar /opt/app/app.jar

# Define a variável de ambiente do profile (pode ser dev, prd, etc.)
ENV PROFILE=prd

# Expõe a porta 8080 (porta padrão da aplicação Spring Boot)
EXPOSE 8080

# Comando de inicialização, interpolando a variável PROFILE corretamente
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-jar","app.jar"]
