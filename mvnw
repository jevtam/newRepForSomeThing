#!/bin/sh

MVNW_REPOURL="https://repo.maven.apache.org/maven2"
WRAPPER_DIR="$(cd "$(dirname "$0")" && pwd)/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
PROPS="$WRAPPER_DIR/maven-wrapper.properties"

DIST_URL=$(grep -E '^distributionUrl=' "$PROPS" | cut -d'=' -f2- | tr -d '\r')
if [ -z "$DIST_URL" ]; then
  echo "distributionUrl is missing in $PROPS"
  exit 1
fi

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$WRAPPER_DIR"
  echo "Downloading maven-wrapper.jar..."
  curl -fsSL -o "$WRAPPER_JAR" "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
fi

JAVA_CMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"
exec "$JAVA_CMD" -Dmaven.multiModuleProjectDirectory="$(cd "$(dirname "$0")" && pwd)" \
  -classpath "$WRAPPER_JAR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
