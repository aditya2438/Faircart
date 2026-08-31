#!/usr/bin/env bash
set -euo pipefail

echo "=== FairCart Setup ==="

command -v java >/dev/null || { echo "ERROR: Java not found"; exit 1; }
command -v mvn  >/dev/null || { echo "ERROR: Maven not found"; exit 1; }

echo ""
echo "[1/3] Starting MySQL via Docker Compose..."
docker compose up -d || echo "WARNING: Docker Compose failed."

echo ""
echo "[2/3] Building backend..."
(cd backend && mvn -q clean package -DskipTests)

echo ""
echo "[3/3] Setup complete!"
echo ""
echo "  Backend:  cd backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev"
echo "  Frontend: open frontend/index.html"
echo "  Health:   http://localhost:8080/api/v1/health"
