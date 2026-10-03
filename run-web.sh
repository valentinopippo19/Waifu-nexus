#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mvn -q -DskipTests compile
java -cp target/classes com.waifu.WebAppServer
