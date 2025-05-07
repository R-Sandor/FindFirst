#!/bin/bash
ORIGIN_PATH=$(pwd)
# Navigate to the script directory
cd $(dirname "$0")
if [ $# -eq 0 ]; then
  echo "No Env file provided, using default."
  . ../.env
else
  . $ORIGIN_PATH/$1
fi

mkdir -p ../data/backups

# Variables
BACKUP_DIR=../data/backups
DB_NAME=$POSTGRES_DB
DB_USER=$POSTGRES_USER
DB_PASSWORD=$POSTGRES_PASSWORD
CONTAINER_NAME=findfirst-db-1

# Get current date and time for backup file
TIMESTAMP=$(date +"%F_%T")
BACKUP_FILE=$BACKUP_DIR/backup_$DB_NAME_$TIMESTAMP.sql

oldBackup=$(ls $BACKUP_DIR/*.sql)

# Run pg_dump inside the PostgreSQL container
docker exec -t $CONTAINER_NAME pg_dump -U $DB_USER $DB_NAME >$BACKUP_FILE

rm $oldBackup

echo "Backup completed: $BACKUP_FILE"
