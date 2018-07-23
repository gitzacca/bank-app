#!/bin/bash


set -e

cd `dirname $0`
r=`pwd`
echo $r

# Accounts
cd $r/accounts
chmod +x gradlew
echo "Starting accounts service..."
./gradlew bootRun &

# Transactions
echo "Starting Tranasctions Service..."
cd $r/transactions
chmod +x gradlew
./gradlew bootRun &
