#! /bin/sh
PURPLE='\033[0;35m'
NOCOLOR='\033[0m'

echo "${PURPLE}====================================================="
echo "Getting DB container's ID:"
echo "=====================================================$NOCOLOR"
cont_ID=$(docker ps | grep db | cut -d " " -f1)
echo "$PURPLE$cont_ID$NOCOLOR"
inspect=$(docker inspect -f '{{ json .Mounts }}' $cont_ID | python3 -m json.tool)
echo "$inspect"
echo "\n"
dest=$(echo "$inspect" | grep Destination | grep data | cut -d ":" -f2 | cut -d \" -f2)
echo $dest
docker cp ./postgres-backup.sql $cont_ID:$dest 

docker exec -i $cont_ID /usr/local/bin/pg_restore -U postgres -d findfirst $dest/postgres-backup.sql