#! /bin/sh
docker exec -i findfirst-db-1 /usr/local/bin/pg_dump -Fc -U postgres findfirst > postgres-backup.sql