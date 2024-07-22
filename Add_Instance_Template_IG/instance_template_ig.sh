#!/bin/bash

# Exit script immediately on any error
set -e

# Function to load variables from config.properties if it exists
load_config() {
    if [ -f config.properties ]; then
        echo "Loading configuration from config.properties..."
        . config.properties
    else
        echo "No config.properties file found. Using default configuration."
    fi
}

# Function to fetch the latest instance template by creation timestamp with a specific name pattern
fetch_latest_template() {
    echo "Fetching latest instance template..."
    LATEST_TEMPLATE=$(gcloud compute instance-templates list \
        --project="$PROJECT_ID" \
        --filter="name~$TEMPLATE_NAME_PATTERN" \
        --format="value(name, creationTimestamp)" \
        | sort -t " " -k2 -r \
        | head -n 1 \
        | awk '{print $1}')

    if [ -z "$LATEST_TEMPLATE" ]; then
        echo "Error occurred: No matching instance template found"
        exit 1
    fi

    echo "The latest instance template found: $LATEST_TEMPLATE."
}

# Function to fetch the current instance template and autoscaling settings
fetch_current_state() {
    echo "Fetching current instance template and autoscaling settings..."
    CURRENT_TEMPLATE=$(gcloud compute instance-groups managed describe "$INSTANCE_GROUP_NAME" \
        --region="$REGION" \
        --project="$PROJECT_ID" \
        --format="value(instanceTemplate)")

    if [ -z "$CURRENT_TEMPLATE" ]; then
        echo "Error occurred: No current instance template found"
        exit 1
    fi

    # Fetching autoscaling info in JSON format
    AUTOSCALING_INFO=$(gcloud compute instance-groups managed describe "$INSTANCE_GROUP_NAME" \
        --region="$REGION" \
        --project="$PROJECT_ID" \
        --format=json)

    # Parsing min and max replicas from JSON using basic bash commands
    ORIGINAL_MIN_REPLICAS=$(echo "$AUTOSCALING_INFO" | grep -o '"minNumReplicas": *[^,]*' | awk -F':' '{print $2}' | tr -d '[:space:]' | tr -d '"')
    ORIGINAL_MAX_REPLICAS=$(echo "$AUTOSCALING_INFO" | grep -o '"maxNumReplicas": *[^,]*' | awk -F':' '{print $2}' | tr -d '[:space:]' | tr -d '"')

    if [ -z "$ORIGINAL_MIN_REPLICAS" ] || [ -z "$ORIGINAL_MAX_REPLICAS" ]; then
        echo "Error occurred: Failed to fetch autoscaling info"
        exit 1
    fi

    echo "Current instance template: $CURRENT_TEMPLATE"
    echo "Current autoscaling min replicas: $ORIGINAL_MIN_REPLICAS, max replicas: $ORIGINAL_MAX_REPLICAS"
}

# Function to update the instance group to use the latest template
update_instance_group() {
    echo "Updating instance group $INSTANCE_GROUP_NAME to use template $LATEST_TEMPLATE..."
    gcloud compute instance-groups managed set-instance-template "$INSTANCE_GROUP_NAME" \
        --template="$LATEST_TEMPLATE" \
        --region="$REGION" \
        --project="$PROJECT_ID"
    echo "Instance group $INSTANCE_GROUP_NAME updated."
    sleep 30  # Wait for changes to propagate
}

# Function to set up autoscaling for the instance group
setup_autoscaling() {
    local min_replicas=$1
    local max_replicas=$2
    echo "Setting up autoscaling for $min_replicas to $max_replicas instances..."
    gcloud compute instance-groups managed set-autoscaling "$INSTANCE_GROUP_NAME" \
        --region="$REGION" \
        --project="$PROJECT_ID" \
        --min-num-replicas="$min_replicas" \
        --max-num-replicas="$max_replicas"
    echo "Autoscaling set for instance group $INSTANCE_GROUP_NAME to min $min_replicas and max $max_replicas instances."
    sleep 30  # Wait for autoscaling settings to take effect
}

# Function to check the health status of the instance group
check_health_status() {
    echo "Checking health status of instances in $INSTANCE_GROUP_NAME..."
    sleep 30  # Wait for instances to initialize and report health status
    HEALTH_STATUS=$(gcloud compute instance-groups managed list-instances "$INSTANCE_GROUP_NAME" \
        --region="$REGION" \
        --project="$PROJECT_ID" \
        --format="value(instance, status)")

    echo "Health status of instances in $INSTANCE_GROUP_NAME:"
    echo "$HEALTH_STATUS"

    # Determine if any instance is unhealthy
    if echo "$HEALTH_STATUS" | grep -q "UNHEALTHY"; then
        echo "Error occurred: One or more instances are unhealthy"
        exit 1
    else
        echo "All instances are healthy"
    fi
}

# Main script logic
main() {
    echo "Starting script execution..."
    load_config
    fetch_current_state
    fetch_latest_template
    update_instance_group
    setup_autoscaling 2 4
    echo "Instance group $INSTANCE_GROUP_NAME updated to use template $LATEST_TEMPLATE and set to 2 instances."
    check_health_status
    setup_autoscaling 1 2
    echo "Instance group $INSTANCE_GROUP_NAME set to 1 instance."
    echo "Script execution completed."
}

# Execute the main function
main
