#!/usr/bin/env bash
set -euo pipefail

HOST=${POSTGRES_HOST:-db}
PORT=${POSTGRES_PORT:-5432}
USER=${POSTGRES_USER:-postgres}
DB=${POSTGRES_DB:-ecommerce_db}
RETRY=60
SLEEP=1

echo "[wait-for-db] Waiting for postgres at ${HOST}:${PORT} ..."

while ! pg_isready -h "${HOST}" -p "${PORT}" -U "${USER}" -d "${DB}" >/dev/null 2>&1; do
  RETRY=$((RETRY-1))
  if [ "$RETRY" -le 0 ]; then
    echo "[wait-for-db] ERROR: Postgres not available after wait."
    exit 1
  fi
  sleep ${SLEEP}
done

echo "[wait-for-db] Postgres is ready."
# return to caller to continue starting app
