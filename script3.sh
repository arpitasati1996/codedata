#!/bin/bash

# Set your server and API endpoint URLs
SERVER_URL="https://your-auth-server.com"
API_URL="https://your-api-endpoint.com"

# Set your credentials
read -p "Enter your username: " USERNAME
read -s -p "Enter your password: " PASSWORD
echo

# Set the authentication request body dynamically
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

# Set the API request body dynamically
read -p "Enter param1 value: " PARAM1
read -p "Enter param2 value: " PARAM2

api_request_body='{"param1":"'"$PARAM1"'", "param2":"'"$PARAM2"'"}'

# Step 2: Make a POST call to API with the acquired token
api_response=$(curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $auth_token" -d "$api_request_body" "$API_URL/your-endpoint")

# Check if the API response indicates success
if [ "$(echo "$api_response" | jq -r '.status')" != "success" ]; then
  echo "API request failed. Terminating script."
  # Log the error using a logger of your choice, e.g., syslog
  logger -p user.err "API request failed: $api_response"
  exit 1
fi

# Process the API response as needed for a successful request
echo "API Response: $api_response"
