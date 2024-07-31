# Use an official Python runtime as a parent image
FROM python:3.8-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY app.py /app

RUN pip install flask

EXPOSE 8000

ENV FLASK_APP=app.py

# Run the Python script when the container launches
CMD ["python", "app.py"]
CMD ["flask", "run", "--host=0.0.0.0", "--port=8000"]
