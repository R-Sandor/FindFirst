FROM openjdk:21-oracle as base

FROM alpine:3.19.1 as keyGen
WORKDIR /keys
RUN apk add openssl
COPY ./scripts/createServerKeys.sh  .
RUN chmod +x ./createServerKeys.sh
RUN ./createServerKeys.sh .

FROM base AS builder
WORKDIR /app
COPY . .
COPY --from=keyGen /keys/app.* src/main/resources/
RUN ./gradlew jar 

FROM base as runner
WORKDIR /app
COPY --from=builder /app/build/libs/findfirst-1.0.0.jar findfirst.jar

ENTRYPOINT ["java", "-jar", "findfirst.jar"]