#!/usr/bin/env bash
set -euo pipefail

JAR="$(ls target/*.jar | head -n 1)"
java -jar "$JAR" "$@"
