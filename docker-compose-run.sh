#!/usr/bin/env bash
set -Eeuo pipefail # Exit immediately if a command exits with a non-zero status
ENV_FILE="${ENV_FILE:-.env}"

MVN_ARGS="${MVN_ARGS:--DskipTests}"

echo "📦 Packaging application..."

./mvnw clean package "${MVN_ARGS}"

echo "🚀 Starting Docker containers..."

docker compose --env-file "${ENV_FILE}" up --build -d