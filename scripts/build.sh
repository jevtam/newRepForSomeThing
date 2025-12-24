#!/usr/bin/env bash
set -euo pipefail

SRC_DIR="src/main/kotlin"
OUT_DIR="build"
JAR="$OUT_DIR/app.jar"
LIB_DIR="libs"

KOTLIN_VERSION="1.9.24"
KOTLIN_HOME="$OUT_DIR/kotlin-$KOTLIN_VERSION"

mkdir -p "$OUT_DIR"

if ! command -v kotlinc >/dev/null 2>&1; then
  if [ ! -x "$KOTLIN_HOME/bin/kotlinc" ]; then
    echo "Downloading Kotlin compiler $KOTLIN_VERSION..."
    curl -sSL -o "$OUT_DIR/kotlin.zip" "https://github.com/JetBrains/kotlin/releases/download/v$KOTLIN_VERSION/kotlin-compiler-$KOTLIN_VERSION.zip"
    unzip -q "$OUT_DIR/kotlin.zip" -d "$OUT_DIR"
  fi
  export PATH="$KOTLIN_HOME/bin:$PATH"
fi

CP="$(echo "$LIB_DIR"/*.jar | tr ' ' ':')"

kotlinc "$SRC_DIR" -include-runtime -cp "$CP" -d "$JAR"

echo "Built: $JAR"
