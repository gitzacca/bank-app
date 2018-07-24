#!/bin/bash

set -e

cd `dirname $0`
r=`pwd`
echo $r

# Accounts
cd $r/accounts
chmod +x gradlew
./gradlew build &

# Transactions
cd $r/transactions
chmod +x gradlew
./gradlew build &

cd $r

docker-compose up
