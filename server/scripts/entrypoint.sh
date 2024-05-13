#! /bin/sh
./scripts/createServerKeys.sh /app/keys
exec java -Djwt.private.key=file:/app/keys/app.key  -Djwt.public.key=file:/app/keys/app.pub -jar findfirst.jar