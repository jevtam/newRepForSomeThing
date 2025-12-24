FROM eclipse-temurin:17-jre
WORKDIR /app

COPY build/app.jar /app/app.jar
COPY libs/ /app/libs/

ENTRYPOINT ["java", "-cp", "/app/app.jar:/app/libs/*", "app.MainKt"]
