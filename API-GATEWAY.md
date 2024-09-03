# Documentation of Terraform Module for API-Gateway

<img width="360" length="100" alt="Terraform" src="https://github.com/user-attachments/assets/3845da7d-0f74-4280-ad2c-8de088143297">

***

| **Author** | **Created on** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **03 september 2024** | **03 september 2024** | **v1** |

***

# Table of contents
* [Introduction](#Introduction)
* [Why Terraform Module](#Why-Terraform-Module)
* [Flow Diagram](#Flow-Diagram)
* [Pre-requisites](#Pre-requisites)
* [Configuration Files](#Configuration-Files)
* [Output](#Output)
* [Best Practices](#Best-Practices)
* [Conclusion](#Conclusion)
* [Contact Information](#Contact-Information)
* [References](#References)

***

# Introduction

An API Gateway Terraform module is a reusable and configurable infrastructure-as-code solution for deploying and managing API Gateway resources in a cloud environment. It simplifies the creation and management of RESTful APIs, integrating seamlessly with backend services while providing essential features like request validation, throttling, and security. 

***

# Why Terraform Module

| Benefit           | Description                                                                                                      |
| ----------------  | ---------------------------------------------------------------------------------------------------------------- |
| **Reusability**   | Define reusable components for different Terraform configurations or projects.                                   |
| **Abstraction**   | Abstract the complexity of infrastructure components, making them easier to manage and understand.               |
| **Consistency**   | Enforce consistency by providing a standardized way to create and manage resources, reducing configuration drift.|
| **Collaboration** | Enable collaboration by allowing teams to share infrastructure components as reusable building blocks.           |
| **Versioning**    | Version modules to track changes and ensure consistent deployments across environments.                          |

***

# Flow Diagram

<img width="950" alt="image" src="https://github.com/user-attachments/assets/7c6d4425-c1dc-44dc-8c3c-83c0cee6472a">

***

# Pre-requisites

| **Pre-requisite** | **Description** |
| ----------------- | --------------- |
| **AWS Account**   | Access to the AWS Management Console with credentials configured. |
| **Hashicrop Terraform** | Terraform **(v1.7.4)** for configuration of AWS resources. |

***

# Configuration Files

### main.tf file

**The `main.tf` file contains the configuration for creating various AWS resources such as security groups, key pair and EC2 Instance.**

<details>
<summary> Click here to see main.tf file</summary>
<br>
  
```shell


resource "aws_api_gateway_rest_api" "this" {
  name        = var.api_name
  description = var.api_description
}

# Use the root resource directly for the API Gateway
resource "aws_api_gateway_method" "this" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_rest_api.this.root_resource_id
  http_method   = var.method_type
  authorization = "NONE"
}

# Define the API Gateway integration
resource "aws_api_gateway_integration" "this" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_rest_api.this.root_resource_id
  http_method             = aws_api_gateway_method.this.http_method
  type                    = "AWS"
  integration_http_method = "POST"
  uri                     = "arn:aws:apigateway:${var.aws_region}:sns:action/${var.sns_action_name}"
  passthrough_behavior    = "WHEN_NO_MATCH"
  timeout_milliseconds    = 29000
  credentials             = var.role_arn
  request_templates       = var.request_templates
  request_parameters      = {
    "integration.request.header.Content-Type" = "'application/x-www-form-urlencoded'"
  }
}


# Define the API Gateway response model
resource "aws_api_gateway_model" "response_model" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  name        = "ResponseModel"
  description = "Model for API Gateway response"
  schema      = jsonencode({
    "$schema" = "http://json-schema.org/draft-04/schema#"
    type      = "object"
    properties = {
      code = {
        type = "integer"
      }
    }
  })
  content_type = "application/json"
}

# Define the method response
resource "aws_api_gateway_method_response" "this" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_rest_api.this.root_resource_id
  http_method = aws_api_gateway_method.this.http_method
  status_code = "200"

  # Specify the response model
  response_models = {
    "application/json" = aws_api_gateway_model.response_model.name
  }

  # Define the response parameters
  response_parameters = {
    "method.response.header.Content-Type" = true
  }
}

resource "aws_api_gateway_integration_response" "this" {
   depends_on = [aws_api_gateway_integration.this]
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_rest_api.this.root_resource_id
  http_method = aws_api_gateway_method.this.http_method
  status_code = aws_api_gateway_method_response.this.status_code
  response_templates = var.integration_response_templates
}



# Define the API Gateway deployment
resource "aws_api_gateway_deployment" "this" {
  depends_on  = [aws_api_gateway_integration.this]
  rest_api_id = aws_api_gateway_rest_api.this.id
  stage_name  = var.api_stage_name
}


# Define the custom domain name and ACM certificate


# resource "aws_api_gateway_base_path_mapping" "this" {
#   api_id      = aws_api_gateway_rest_api.this.id
#   domain_name = var.domain_name
#   base_path   = var.base_path  
#   stage_name  = var.api_stage_name
# }

resource "aws_apigatewayv2_api_mapping" "this" {
  api_id      = aws_api_gateway_rest_api.this.id
  domain_name = var.domain_name
  stage       = var.api_stage_name
  api_mapping_key = var.base_path
}
```
</details>

***

### variables.tf file

**The `variables.tf` file defines input variables used in the `main.tf` file to parameterize the configuration. These variables include vpc, subnet, security group, private key and instance related configurations .**

<details>
<summary> Click here to see variables.tf file</summary>
<br>
  
```shell
variable "api_name" {
  description = "The name of the API Gateway"
  type        = string
}

variable "api_description" {
  description = "The description of the API Gateway"
  type        = string
}


variable "api_stage_name" {
  description = "The deployment stage name for the API"
  type        = string
}

variable "api_resources" {
  description = "The API Gateway resources path"
  type        = string
}

variable "method_type" {
  description = "The HTTP method type (GET, POST, etc.)"
  type        = string
}

variable "integration_type" {
  description = "The integration type for the API Gateway"
  type        = string
}


variable "role_arn" {
  description = "ARN of the IAM role"
  type        = string
}

variable "endpoint_type" {
  description = "The endpoint type (EDGE, REGIONAL, PRIVATE)"
  type        = string
}

variable "aws_region" {
  description = "The AWS region where the API Gateway and SNS are hosted"
  type        = string
}

variable "sns_action_name" {
  description = "The action name for the SNS integration"
  type        = string
  default     = "Publish"  
}


variable "sns_topic_name" {
  description = "The SNS topic name to be used"
  type        = string
}


variable "request_templates" {
  description = "Map of request templates for API Gateway integration"
  type        = map(string)
}

variable "domain_name" {
  description = "Custom domain name for API Gateway"
  type        = string
}



variable "base_path" {
  description = "The base path for the API mapping"
  type        = string
}

variable "integration_response_templates" {
  description = "The mapping template for integration responses"
  type        = map(string)
}
```
</details>

***

### output.tf file

**The `output.tf` file specifies the outputs of the Terraform configuration. It includes the IDs of Securioty Group and EC2 Instance.**

<details>
<summary> Click here to see output.tf file</summary>
<br>

```shell
output "Security_Group_ID" {
  value = [aws_security_group.sec_grp.id]
}
output "server_id" {
  value = [aws_instance.standalone_server.id]
}

```
</details>

***

### data.tf file

<details>
<summary> Click here to see data.tf file</summary>
<br>

```shell
data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

```
</details>


# Output

## Terminal Output

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/1c3b58fa-2136-4ad6-92de-24790585fbc2">

***

## Console Output

### Security Group

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/60879fc6-f6c5-4794-8147-0476470d4a03">

***

### Private Key

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/cd56aa74-2a0f-4fd1-a2e4-848805eb627b">

***

### EC2 Instance

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/0e1d3c60-eada-4a04-92a6-bb4fff18b064">

***

# Best Practices

| Practice              | Description                                                                                                        |
|-----------------------|------------------------------------------------------------------------------------------------------------------- |
| **Atomicity**         | Keep modules small and focused on a single concern or resource type to promote reusability and maintainability.    |
| **Input Validation**  | Validate input variables to ensure they meet expected requirements and constraints.                                |
| **Documentation**     | Provide clear documentation, including usage examples, input variables, and outputs.                               |
| **Testing**           | Implement automated tests to validate functionality and prevent regressions.                                       |
| **Error Handling**    | Implement error handling and logging mechanisms for troubleshooting.                                               |
| **State Management**  | Avoid storing sensitive information in Terraform state files and leverage remote state management for better security.|

***

# Conclusion

Terraform modules are a fundamental building block for creating reusable and maintainable infrastructure as code. By following best practices and principles such as modularity, abstraction, and versioning, teams can leverage modules to efficiently manage complex infrastructure deployments at scale.By leveraging this module, teams can ensure consistent, scalable, and secure API infrastructure, reducing the complexity of manual setups and enabling faster development cycles. 

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal@mygurukulam.co |

***

# References

| **Source** | **Description** |
| ---------- | --------------- |
| [Link](https://medium.com/onfido-tech/aws-api-gateway-with-terraform-7a2bebe8b68f) | API Gateway Module. |
| [Link](https://developer.hashicorp.com/terraform/language/modules) | Terraform Module Concept. |
