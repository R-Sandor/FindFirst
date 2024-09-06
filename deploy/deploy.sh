#!/bin/sh
cd ../
git pull
docker compose -f docker-compose-prod.yml build
docker compose down
docker compose --env-file .prod.env -f docker-compose-prod.yml up -d
