# Use an official Python runtime as a parent image
FROM python:3.8-slim

# Install AWS CLI
RUN apt-get update && \
    apt-get install -y unzip && \
    curl "https://d1uj6qtbmh3dt5.cloudfront.net/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    sudo ./aws/install && \
    rm -rf awscliv2.zip

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY Jenkinsfile/Python/app.py /app

RUN pip install flask

EXPOSE 8000

ENV FLASK_APP=app.py

# Run the Python script when the container launches
CMD ["python", "app.py"]
CMD ["flask", "run", "--host=0.0.0.0", "--port=8000"]
