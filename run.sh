#!/bin/sh
set -e

LIBS="libs/*"
OUT_DIR="out"
MAIN_CLASS="app.MainKt"

java -cp "$OUT_DIR:$LIBS" "$MAIN_CLASS" "$@"

