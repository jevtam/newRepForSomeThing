#!/usr/bin/env bash
set -euo pipefail

KOTLIN_VERSION=1.9.24
CLI_VER=0.3.5

SRC_DIR="src"
OUT_DIR="build"
JAR="$OUT_DIR/app.jar"
CLI_JAR="$OUT_DIR/kotlinx-cli-jvm-$CLI_VER.jar"

mkdir -p "$OUT_DIR"

if [ ! -f "$CLI_JAR" ]; then
  curl -L -o "$CLI_JAR" \
    "https://repo1.maven.org/maven2/org/jetbrains/kotlinx/kotlinx-cli-jvm/$CLI_VER/kotlinx-cli-jvm-$CLI_VER.jar"
fi

kotlinc "$SRC_DIR" -include-runtime -cp "$CLI_JAR" -d "$JAR"

echo "Built: $JAR"
