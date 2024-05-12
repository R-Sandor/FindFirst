#! /bin/bash
outpath=../src/main/resources
if [[ $# -eq 0 ]] ; then
  cd "$(dirname "$0")"
  outpath=../src/main/resources
else 
  outpath="$1"
fi

openssl genrsa -out keypair.pem 
openssl pkcs8 -in keypair.pem -topk8 -out app.key -nocrypt 
openssl rsa -in keypair.pem -pubout > app.pub
rm keypair.pem
mv app.* $outpath