FROM ubuntu:20.04 as linux
RUN apt update -y && apt install -y openssl openjdk-17-jdk

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
