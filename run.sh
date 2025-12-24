#!/usr/bin/env bash
set -euo pipefail

mvn -B -q -DskipTests=true package

APP_JAR=$(ls target/*-SNAPSHOT.jar | head -n 1)

if [ -z "$APP_JAR" ]; then
  echo "JAR not found in target/"
  exit 1
fi

java -jar "$APP_JAR" "$@"
