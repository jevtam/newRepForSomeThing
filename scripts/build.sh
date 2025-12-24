#!/usr/bin/env bash
set -euo pipefail

./mvnw -q -DskipTests package
echo "Built: target/*.jar"
