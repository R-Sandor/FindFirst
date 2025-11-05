#syntax=docker/dockerfile:1.7-labs
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY --exclude=build/ . .
RUN ./gradlew assemble

FROM openjdk:26-ea-slim  AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/findfirst-1.0.0.jar findfirst.jar
COPY scripts/ scripts/

ENTRYPOINT ["scripts/entrypoint.sh"]
