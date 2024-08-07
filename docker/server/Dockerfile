FROM alpine:3.20.2 AS linux
RUN apk add openssl openjdk17 

FROM openjdk:17-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew assemble

FROM linux AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/findfirst-1.0.0.jar findfirst.jar
COPY scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]