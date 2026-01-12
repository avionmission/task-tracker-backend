#!/bin/sh

echo "=== Railway Deployment Debug Info ==="
echo "PORT: $PORT"
echo "DATABASE_URL: ${DATABASE_URL:0:50}..." # Only show first 50 chars for security
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
echo "JWT_SECRET set: $([ -n "$JWT_SECRET" ] && echo "YES" || echo "NO")"
echo "Java version: $(java -version 2>&1 | head -1)"
echo "====================================="

# Start the application
exec java -Dspring.profiles.active=railway \
     -Djava.security.egd=file:/dev/./urandom \
     -Dserver.port=${PORT:-8080} \
     -jar app.jar