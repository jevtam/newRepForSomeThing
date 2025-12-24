#!/usr/bin/env bash
set -euo pipefail

JAR="build/app.jar"
LIB_DIR="libs"
CP="$JAR:$(echo "$LIB_DIR"/*.jar | tr ' ' ':')"

java -cp "$CP" app.MainKt "$@"

