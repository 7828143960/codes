#!/bin/bash

# Load variables from config.properties if it exists
load_config() {
    if [ -f config.properties ]; then
        . config.properties
    fi
}

# Fetch the latest image by creation timestamp with a specific name pattern
fetch_latest_image() {
    LATEST_IMAGE=$(gcloud compute images list \
        --project="$IMAGE_PROJECT" \
        --filter="name~$IMAGE_NAME_PATTERN AND status=READY" \
        --format="value(name, creationTimestamp)" \
        | sort -t " " -k2 -r \
        | head -n 1 \
        | awk '{print $1}')

    if [ -z "$LATEST_IMAGE" ]; then
        echo "No matching image found"
        exit 1
    fi
}

# Generate timestamp
generate_timestamp() {
    TIMESTAMP=$(date +"%Y%m%d%H%M%S")
}

# Save the AMI ID with timestamp to file
save_latest_ami() {
    echo "$TIMESTAMP: $LATEST_IMAGE" > latest_ami.txt
    echo "Latest AMI: $LATEST_IMAGE saved with timestamp $TIMESTAMP"
}

# Main script logic
main() {
    load_config
    fetch_latest_image
    generate_timestamp
    save_latest_ami
}

# Execute the main function
main
