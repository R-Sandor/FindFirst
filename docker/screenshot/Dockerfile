#syntax=docker/dockerfile:1.7-labs
FROM gradle:8.14.3-jdk21-noble AS base
FROM mcr.microsoft.com/playwright/java:jammy-amd64 AS playwright

FROM base AS builder
WORKDIR /appHome
COPY --exclude=app/build/* --exclude=data . . 
RUN gradle assemble

FROM playwright 
WORKDIR /app
COPY --from=builder /appHome/app/build/libs/app.jar screenshot.jar
COPY screenshots/scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]
