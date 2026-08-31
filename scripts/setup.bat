@echo off
REM FairCart — Windows setup script
echo === FairCart Setup ===

where java >nul 2>&1 || (echo ERROR: Java not found & exit /b 1)
where mvn >nul 2>&1 || (echo ERROR: Maven not found & exit /b 1)

echo.
echo [1/3] Starting MySQL via Docker Compose...
docker compose up -d
if errorlevel 1 (
    echo WARNING: Docker Compose failed. Ensure Docker Desktop is running.
)

echo.
echo [2/3] Building backend...
cd backend
call mvn -q clean package -DskipTests
if errorlevel 1 exit /b 1

echo.
echo [3/3] Setup complete!
echo.
echo   Backend:  cd backend ^&^& mvn spring-boot:run -Dspring-boot.run.profiles=dev
echo   Frontend: open frontend\index.html
echo   Health:   http://localhost:8080/api/v1/health
cd ..
