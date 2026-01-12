#!/bin/sh

echo "=== Railway Deployment Debug Info ==="
echo "PORT: $PORT"
echo "DATABASE_URL length: ${#DATABASE_URL}"
echo "DATABASE_URL starts with: $(echo $DATABASE_URL | cut -c1-15)"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
echo "JWT_SECRET set: $([ -n "$JWT_SECRET" ] && echo "YES" || echo "NO")"
echo "Java version: $(java -version 2>&1 | head -1)"

# Check if DATABASE_URL is actually set
if [ -z "$DATABASE_URL" ]; then
    echo "ERROR: DATABASE_URL is not set!"
    echo "Available environment variables:"
    env | grep -E "(DATABASE|POSTGRES|PG)" | sort
else
    echo "DATABASE_URL is set correctly"
fi
echo "====================================="

# Set default profile if not set
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-railway}

# Start the application
exec java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
     -Djava.security.egd=file:/dev/./urandom \
     -Dserver.port=${PORT:-8080} \
     -jar app.jar