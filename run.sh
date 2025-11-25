#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="build"
JAR="$OUT_DIR/app.jar"
CLI_JAR="$OUT_DIR/kotlinx-cli-jvm-0.3.5.jar"

java -cp "$JAR:$CLI_JAR" app.MainKt "$@"
