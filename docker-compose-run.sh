#!/usr/bin/env bash
set -e # Exit immediately if a command exits with a non-zero status
ENV_FILE="${ENV_FILE:-.env}"
echo "📦 Packaging application..."
mvn package -DskipTests
echo "🚀 Starting Docker containers..."
docker compose --env-file "${ENV_FILE}" up --build -d