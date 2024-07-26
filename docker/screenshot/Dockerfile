FROM alpine:3.19.1 as linux
RUN apk add openssl openjdk17 

FROM openjdk:21-oracle AS builder
WORKDIR /app
COPY . .
RUN ./gradlew assemble

FROM linux as runner
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar screenshot.jar
COPY scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]
