#!/bin/bash

# Colors for better output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== FlexStream Stack Verification ===${NC}"

# 1. Check Redis
echo -n "Checking Redis Metadata Cache... "
REDIS_COUNT=$(docker exec flex-redis redis-cli HLEN movies:title 2>/dev/null)
if [ -z "$REDIS_COUNT" ] || [ "$REDIS_COUNT" -eq 0 ]; then
    echo "❌ Redis is empty or not running."
else
    echo -e "${GREEN}OK${NC} ($REDIS_COUNT movie titles cached)"
fi

# 2. Check Cassandra
echo -n "Checking Cassandra Activity Stream... "
# Note: We use -e to run the query and tail/awk to get the number
CASS_COUNT=$(docker exec flex-cassandra cqlsh -e "SELECT count(*) FROM flexstream.ratings_by_user;" 2>/dev/null | grep -o '[0-9]\+' | head -1)

if [ -z "$CASS_COUNT" ] || [ "$CASS_COUNT" -eq 0 ]; then
    echo "❌ Cassandra is empty or not running."
else
    echo -e "${GREEN}OK${NC} (~$CASS_COUNT activity records stored)"
fi

echo -e "${BLUE}=====================================${NC}"
