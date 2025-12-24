  #!/bin/sh
  set -e

  DB_FILE="data.db"

  rm -f "$DB_FILE"

  sqlite3 "$DB_FILE" < scripts/init.sql
  sqlite3 "$DB_FILE" < scripts/fill.sql

  echo "DB initialized: $DB_FILE"
