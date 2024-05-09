#! /bin/sh
outpath=../src/main/resources

if [ $# -neq 0 ]; then
 outpath="$1"
fi

openssl genrsa -out keypair.pem 
openssl pkcs8 -in keypair.pem -topk8 -out app.key -nocrypt 
openssl rsa -in keypair.pem -pubout > app.pub
rm keypair.pem