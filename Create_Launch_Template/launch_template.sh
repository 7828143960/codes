#!/bin/bash

# Function to load variables from config.properties if it exists
load_config() {
    [ -f config.properties ] && . config.properties
}

# Function to handle errors
handle_error() {
    echo "Error occurred: $1"
    exit 1
}

# Function to fetch the latest image by creation timestamp with a specific name pattern
fetch_latest_image() {
    LATEST_IMAGE=$(gcloud compute images list \
        --project="$IMAGE_PROJECT" \
        --filter="name~$IMAGE_NAME_PATTERN AND status=READY" \
        --format="value(name)" \
        --sort-by="~creationTimestamp" \
        | head -n 1)

    # Check if a valid image was found
    [ -z "$LATEST_IMAGE" ] && handle_error "No matching image found"
}

# Function to generate timestamp for instance template name
generate_timestamp() {
    INSTANCE_TEMPLATE_TIMESTAMP=$(date +"%Y%m%d%H%M%S")
}

# Function to validate instance template scope and region
validate_scope_and_region() {
    [[ ! "$INSTANCE_TEMPLATE_SCOPE" =~ ^(regional|global)$ ]] && handle_error "Invalid instance template scope"
    [[ "$INSTANCE_TEMPLATE_SCOPE" == "regional" && -z "$REGION" ]] && handle_error "Region cannot be empty for a regional instance template"
}

# Function to set default values for VPC_NETWORK and SUBNET
set_defaults() {
    VPC_NETWORK=${VPC_NETWORK:-default}
    SUBNET=${SUBNET:-default}
}

# Function to check if instance template name is provided
check_instance_template_name() {
    [ -z "$INSTANCE_TEMPLATE_NAME" ] && handle_error "Instance Template Name cannot be empty"
}

# Function to create instance template command
create_instance_template_command() {
    CMD="gcloud compute instance-templates create \"$INSTANCE_TEMPLATE_NAME-$INSTANCE_TEMPLATE_TIMESTAMP\" \
        --project=\"$PROJECT_ID\" \
        --machine-type=\"$INSTANCE_TYPE\" \
        --image=\"$LATEST_IMAGE\" \
        --image-project=\"$IMAGE_PROJECT\" \
        --network=\"$VPC_NETWORK\" \
        --subnet=\"$SUBNET\""

    # Add region if regional scope
    [ "$INSTANCE_TEMPLATE_SCOPE" == "regional" ] && CMD+=" --region=\"$REGION\""

    # Add network tags for private instance if PRIVATE_INSTANCE is set to "true"
    [ "$PRIVATE_INSTANCE" == "true" ] && CMD+=" --no-address"

    # Allow HTTP port 80
    CMD+=" --tags=http-server"
}

# Function to execute the command
execute_command() {
    echo "Executing command: $CMD"
    eval $CMD || handle_error "Failed to create $INSTANCE_TEMPLATE_SCOPE instance template $INSTANCE_TEMPLATE_NAME"
}

# Function to save instance template name with timestamp to file
save_instance_template() {
    echo "$INSTANCE_TEMPLATE_TIMESTAMP: $INSTANCE_TEMPLATE_NAME-$INSTANCE_TEMPLATE_TIMESTAMP" > latest_instance_template.txt
    echo "Instance template $INSTANCE_TEMPLATE_NAME-$INSTANCE_TEMPLATE_TIMESTAMP created with the latest image $LATEST_IMAGE."
}

# Main script logic
main() {
    load_config
    fetch_latest_image
    generate_timestamp
    validate_scope_and_region
    set_defaults
    check_instance_template_name
    create_instance_template_command
    execute_command
    save_instance_template
    echo "Script execution completed."
}

# Execute the main function
main
