FROM gradle:8.10.0-jdk17-alpine AS base
FROM mcr.microsoft.com/playwright/java:jammy-amd64 as playwright

FROM base AS builder
WORKDIR /appHome
COPY . .
RUN gradle assemble

FROM playwright 
WORKDIR /app
COPY --from=builder /appHome/app/build/libs/app.jar screenshot.jar
COPY scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]
