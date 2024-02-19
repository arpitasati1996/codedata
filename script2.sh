#!/bin/bash

# Set your server and API endpoint URLs
SERVER_URL="https://your-auth-server.com"
API_URL="https://your-api-endpoint.com"

# Set your credentials
USERNAME="your_username"
PASSWORD="your_password"

# Set the authentication request body
auth_request_body='{"username":"'"$USERNAME"'", "password":"'"$PASSWORD"'"}'

# Step 1: Get authentication token
token_response=$(curl -s -X POST -H "Content-Type: application/json" -d "$auth_request_body" "$SERVER_URL/authenticate")

# Extract the token from the response
auth_token=$(echo "$token_response" | jq -r '.token')

# Check if authentication was successful
if [ -z "$auth_token" ]; then
  echo "Authentication failed"
  exit 1
fi

echo "Authentication successful. Token: $auth_token"

# Set the API request body
api_request_body='{"param1":"value1", "param2":"value2"}'

# Step 2: Make a POST call to API with the acquired token
api_response=$(curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $auth_token" -d "$api_request_body" "$API_URL/your-endpoint")

# Process the API response as needed
echo "API Response: $api_response"
