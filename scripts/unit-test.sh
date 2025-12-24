#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="build"
TEST_CLASSES="$OUT_DIR/test-classes"
MAIN_JAR="$OUT_DIR/app.jar"
LIB_DIR="libs"

KOTLIN_VERSION="1.9.24"
KOTLIN_HOME="$OUT_DIR/kotlin-$KOTLIN_VERSION"

mkdir -p "$TEST_CLASSES"

if ! command -v kotlinc >/dev/null 2>&1; then
  if [ ! -x "$KOTLIN_HOME/bin/kotlinc" ]; then
    echo "Downloading Kotlin compiler $KOTLIN_VERSION..."
    curl -sSL -o "$OUT_DIR/kotlin.zip" "https://github.com/JetBrains/kotlin/releases/download/v$KOTLIN_VERSION/kotlin-compiler-$KOTLIN_VERSION.zip"
    unzip -q "$OUT_DIR/kotlin.zip" -d "$OUT_DIR"
  fi
  export PATH="$KOTLIN_HOME/bin:$PATH"
fi

CP="$(echo "$LIB_DIR"/*.jar | tr ' ' ':')"
if compgen -G "src/test/kotlin/**/*.kt" > /dev/null; then
  kotlinc src/test/kotlin -cp "$MAIN_JAR:$CP" -d "$TEST_CLASSES"
else
  echo "No test sources found in src/test/kotlin"
  exit 0
fi

JUNIT_JAR="$(ls "$LIB_DIR"/junit-platform-console-standalone-*.jar | head -n 1)"
java -jar "$JUNIT_JAR" \
  --class-path "$TEST_CLASSES:$MAIN_JAR:$CP" \
  --scan-class-path
