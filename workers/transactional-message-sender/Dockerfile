FROM eclipse-temurin:26.0.1_8-jre-alpine

LABEL authors="emmanuelogoma"

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --chown=appuser:appgroup target/*.jar app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]