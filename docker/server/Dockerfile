
FROM alpine:3.19.1 as linux
WORKDIR /keys
RUN apk add openssl openjdk17 
COPY ./scripts/createServerKeys.sh  .
RUN chmod +x ./createServerKeys.sh
RUN ./createServerKeys.sh .

FROM openjdk:21-oracle AS builder
WORKDIR /app
COPY . .
COPY --from=linux /keys/app.* src/main/resources/
RUN ./gradlew jar 

FROM linux as runner
WORKDIR /app
COPY --from=builder /app/build/libs/findfirst-1.0.0.jar findfirst.jar
COPY --from=linux /keys keys/
COPY scripts/ scripts/
RUN chmod u+x scripts/*

ENTRYPOINT ["scripts/entrypoint.sh"]