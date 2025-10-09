# Usa a imagem oficial do Eclipse Temurin (OpenJDK) para criar a imagem de BUILD
FROM eclipse-temurin:21-jdk-alpine AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo Maven pom.xml
COPY pom.xml .

# Copia o código fonte do projeto
COPY src ./src

# Executa o build do Maven. 
# O comando 'clean package' compila e cria o JAR. 
# O parâmetro -DskipTests=true é adicionado AQUI para que o build 
# no Docker não rode os testes *novamente* (eles já rodaram na pipeline).
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests=true

# --- FASE DE EXECUÇÃO (Runtime) ---
# Usa uma imagem menor e mais leve, otimizada para rodar o JAR (economia de espaço)
FROM eclipse-temurin:21-jre-alpine

# Define o argumento ARGS, que será o nome do JAR gerado
ARG JAR_FILE=target/*.jar

# Define o diretório de trabalho
WORKDIR /opt/app

# Copia o arquivo JAR da fase de build para a fase de execução
COPY --from=build /app/${JAR_FILE} app.jar

# Expõe a porta que o Spring Boot usa (padrão 8080)
EXPOSE 8080

# Comando para rodar a aplicação quando o container iniciar
ENTRYPOINT ["java","-jar","app.jar"]
