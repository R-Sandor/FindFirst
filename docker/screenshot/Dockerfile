FROM alpine:3.20.2 AS linux
RUN apk add openjdk17 

FROM openjdk:17-slim AS builder
WORKDIR /appHome
COPY . .
RUN ./gradlew assemble

FROM linux AS runner
WORKDIR /app
COPY --from=builder /appHome/app/build/libs/app.jar screenshot.jar
COPY scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]
