#! /bin/sh
cd ../server/src/main/resources
openssl genrsa -out keypair.pem 
openssl pkcs8 -in keypair.pem -topk8 -out app.key -nocrypt 
openssl rsa -in keypair.pem -pubout > app.pub
rm keypair.pem