# Documentation of Terraform Module for API-Gateway

<img width="360" length="100" alt="Terraform" src="https://github.com/user-attachments/assets/c28e21be-03f8-4662-9369-ae6f799479a7">

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

## modules/api-gateway/main.tf

This Terraform configuration defines an AWS API Gateway REST API with methods, integrations, responses, and deployments, allowing seamless integration with AWS services such as SNS. It also sets up API mappings for custom domain names, enhancing API management and accessibility.

### main.tf file

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

## modules/api-gateway/variables.tf

These Terraform variables define configurable parameters for setting up an AWS API Gateway, such as API name, description, stage, methods, integration details, and custom domain settings. 

### variables.tf file

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

## modules/api-gateway/provider.tf

Terraform provider configuration specifies the AWS region as `ap-south-1 (Mumbai)` for deploying resources. It enables Terraform to interact with AWS services in the specified region.

### provider.tf file

<details>
<summary> Click here to see provider.tf file</summary>
<br>

```shell
provider "aws" {
  region = "ap-south-1"  
}

```
</details>

***

## api-gateway/backend.tf

This Terraform configuration sets up remote state storage in an S3 bucket named broking-staging-tf-state with state locking managed by a DynamoDB table. It ensures secure and consistent state management for deployments in the `ap-south-1` region.

### backend.tf file

<details>
<summary> Click here to see data.tf file</summary>
<br>

```shell
terraform {
  backend "s3" {
    bucket         = "broking-staging-tf-state"
    key            = "broking-staging/backend/backend-api-gateway/api-gateway.tfstate"
    region         = "ap-south-1"
    dynamodb_table = "terraform-state-locking"
    encrypt        = true
  }
}

```
</details>
***

## api-gateway/custom_domain.tf

This resource configures a custom domain for API Gateway using AWS API Gateway V2, specifying the domain name and associated ACM certificate for secure HTTPS communication. It uses a regional endpoint with TLS 1.2 security for enhanced security compliance.

### custom_domain.tf file

<details>
<summary> Click here to see custom_domain.tf file</summary>
<br>

```shell
resource "aws_apigatewayv2_domain_name" "custom_domain" {
  domain_name = var.nse_callback_api.domain_name
  domain_name_configuration {
    certificate_arn = var.nse_callback_api.acm_certificate_arn
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"  # Required attribute specifying the TLS version
  }
}

```
</details>
***

## api-gateway/iam.tf

This Terraform configuration creates an IAM role with policies to allow API Gateway to publish messages to SNS and send messages to SQS. It attaches necessary permissions, including a managed policy for API Gateway logging to CloudWatch, ensuring secure and traceable API operations.

### iam.tf file

<details>
<summary> Click here to see iam.tf file</summary>
<br>

```shell
resource "aws_iam_role" "sns_to_sqs_role" {
  name = "sns-to-sqs-role"

  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "",
        "Effect": "Allow",
        "Principal": {
          "Service": "apigateway.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_policy" "sns_publish_and_sqs_send_policy" {
  name        = "sns-publish-and-sqs-send-policy"
  description = "Policy to allow SNS publish and SQS send actions"

  policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "VisualEditor0",
        "Effect": "Allow",
        "Action": [
          "sns:DeleteTopic",
          "sns:ListTopics",
          "sns:Unsubscribe",
          "sns:CreatePlatformEndpoint",
          "sns:OptInPhoneNumber",
          "sns:CheckIfPhoneNumberIsOptedOut",
          "sns:GetDataProtectionPolicy",
          "sns:ListEndpointsByPlatformApplication",
          "sns:SetEndpointAttributes",
          "sns:Publish",
          "sns:DeletePlatformApplication",
          "sns:SetPlatformApplicationAttributes",
          "sns:VerifySMSSandboxPhoneNumber",
          "sns:Subscribe",
          "sns:ConfirmSubscription",
          "sns:*",
          "sns:ListTagsForResource",
          "sns:DeleteSMSSandboxPhoneNumber",
          "sns:PutDataProtectionPolicy",
          "sns:ListSubscriptionsByTopic",
          "sns:GetTopicAttributes",
          "sns:ListSMSSandboxPhoneNumbers",
          "sns:CreatePlatformApplication",
          "sns:SetSMSAttributes",
          "sns:CreateTopic",
          "sns:GetPlatformApplicationAttributes",
          "sns:GetSubscriptionAttributes",
          "sns:ListSubscriptions",
          "sns:ListOriginationNumbers",
          "sns:DeleteEndpoint",
          "sns:ListPhoneNumbersOptedOut",
          "sns:GetEndpointAttributes",
          "sns:SetSubscriptionAttributes",
          "sns:GetSMSSandboxAccountStatus",
          "sns:CreateSMSSandboxPhoneNumber",
          "sns:ListPlatformApplications",
          "sns:GetSMSAttributes"
        ],
        "Resource": "*"
      },
      {
        "Sid": "VisualEditor1",
        "Effect": "Allow",
        "Action": "sqs:SendMessage",
        "Resource": "arn:aws:sqs:ap-south-1:*:*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "sns_to_sqs_role_policy_attach" {
  role       = aws_iam_role.sns_to_sqs_role.name
  policy_arn = aws_iam_policy.sns_publish_and_sqs_send_policy.arn
}

resource "aws_iam_role_policy_attachment" "attach_apigw_cloudwatch_policy" {
  role       = aws_iam_role.sns_to_sqs_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs"
}


```
</details>

***

## api-gateway/main.tf

These Terraform module configurations create two API Gateway setups (nse_callback_api and cashfree_callback_api) using a reusable module for managing API resources, methods, integrations, and custom domains. They specify API details, integration settings, and IAM roles for secure API operations and SNS-SQS integration.

### main.tf file

<details>
<summary> Click here to see main.tf file</summary>
<br>

```shell
module "nse_callback_api" {
  source                      = "../../global/modules/api-gateway"
  api_name                    = var.nse_callback_api.api_name
  api_description             = var.nse_callback_api.api_description
  api_stage_name              = var.nse_callback_api.api_stage_name
  api_resources               = var.nse_callback_api.api_resources
  method_type                 = var.nse_callback_api.method_type
  integration_type            = var.nse_callback_api.integration_type
  sns_action_name             = var.nse_callback_api.sns_action_name
  role_arn                    = aws_iam_role.sns_to_sqs_role.arn
  endpoint_type               = var.nse_callback_api.endpoint_type
  aws_region                  = var.region
  sns_topic_name              = var.nse_callback_api.sns_topic_name
  request_templates           = var.nse_callback_api.request_templates
  integration_response_templates = var.nse_callback_api.integration_response_templates
  base_path                   = var.nse_callback_api.base_path
  domain_name                 = aws_apigatewayv2_domain_name.custom_domain.domain_name
}

module "cashfree_callback_api" {
  source                      = "../../global/modules/api-gateway"
  api_name                    = var.cashfree_callback_api.api_name
  api_description             = var.cashfree_callback_api.api_description
  api_stage_name              = var.cashfree_callback_api.api_stage_name
  api_resources               = var.cashfree_callback_api.api_resources
  method_type                 = var.cashfree_callback_api.method_type
  integration_type            = var.cashfree_callback_api.integration_type
  sns_action_name             = var.cashfree_callback_api.sns_action_name
  role_arn                    = aws_iam_role.sns_to_sqs_role.arn
  endpoint_type               = var.cashfree_callback_api.endpoint_type
  aws_region                  = var.region
  sns_topic_name              = var.cashfree_callback_api.sns_topic_name
  request_templates           = var.cashfree_callback_api.request_templates
  integration_response_templates = var.cashfree_callback_api.integration_response_templates
  base_path                   = var.cashfree_callback_api.base_path
  domain_name                 = aws_apigatewayv2_domain_name.custom_domain.domain_name
}
```
</details>

***
## api-gateway/variables.tf

These Terraform variables define configurations for two API Gateway callback APIs (nse_callback_api and cashfree_callback_api), encapsulating API properties such as name, stage, resources, methods, integration settings, and domain details.

### variables.tf file

<details>
<summary> Click here to see variables.tf file</summary>
<br>

```shell
variable "region" {
  description = "AWS Region"
  type        = string
  default     = "ap-south-1"
}
variable "nse_callback_api" {
  type = object({
    api_name                    = string
    api_description             = string
    api_stage_name              = string
    api_resources               = string
    method_type                 = string
    integration_type            = string
    endpoint_type               = string
    sns_action_name             = string
    integration_uri             = string
    acm_certificate_arn         = string
    domain_name                 = string
    base_path                   = string
    integration_response_templates = map(string)
    request_templates           = map(string)
    sns_topic_name              = string
  })
}

variable "cashfree_callback_api" {
  type = object({
    api_name                    = string
    api_description             = string
    api_stage_name              = string
    api_resources               = string
    method_type                 = string
    integration_type            = string
    endpoint_type               = string
    sns_action_name             = string
    integration_uri             = string
    acm_certificate_arn         = string
    domain_name                 = string
    base_path                   = string
    integration_response_templates = map(string)
    request_templates           = map(string)
    sns_topic_name              = string
  })
}
```
</details>

***

## api-gateway/terraform.tfvars

These variable definitions configure two API Gateway callback APIs (nse_callback_api and cashfree_callback_api), specifying details such as API names, stages, resource paths, HTTP methods, and integration settings.

### terraform.tfvars file

<details>
<summary> Click here to see terraform.tfvars file</summary>
<br>

```shell
nse_callback_api = {
  api_name         = "NSECallbackAPI"
  api_description  = "API for NSE callbacks"
  api_stage_name   = "nse-callbacks"
  api_resources    = "nse-callbacks"
  method_type      = "POST"  # Specify the method type here
  integration_type = "AWS"
  endpoint_type    = "EDGE"
  sns_action_name  = "Publish"
  base_path        = "v1/notification"
  domain_name      = "devops-ml.online"
  sns_topic_name   = "NSECallbackAPI"
  acm_certificate_arn = "arn:aws:acm:ap-south-1:686255949108:certificate/5221358b-87b0-4150-89aa-282fa0eb50df"
  integration_uri  = "arn:aws:apigateway:ap-south-1:sns:path//"  # Provide the correct integration URI
  request_templates = {
    "application/x-www-form-urlencoded" = <<EOF
Action=Publish&TopicArn=$util.urlEncode('arn:aws:sns:ap-south-1:730288278641:NSECallbackAPI')&TopicName='NSECallbackAPI'
&Message=$util.urlEncode($util.urlEncode($input.body))&Subject=test
EOF
  }
  integration_response_templates = {
    "application/json" = <<EOF
#set($inputRoot = $input.path('$'))
#if($inputRoot.body-json.PublishResponse.PublishResult.MessageId)
    {
        "code":200
    }
#else
    {
        "code":200
    }
#end
EOF
  }
}

cashfree_callback_api = {
  api_name         = "CashfreeCallbackAPI"
  api_description  = "API for Cashfree callbacks"
  api_stage_name   = "cashfree_callbacks"
  api_resources    = "cashfree-callbacks"
  method_type      = "POST"  # Specify the method type here
  integration_type = "AWS"
  endpoint_type    = "EDGE"
  sns_action_name  = "Publish"
  base_path        = "v1/cashfree-callback"
  domain_name      = "devops-ml.online"
  sns_topic_name   = "CashfreeCallbackAPI"
  acm_certificate_arn = "arn:aws:acm:ap-south-1:686255949108:certificate/5221358b-87b0-4150-89aa-282fa0eb50df"
  integration_uri  = "arn:aws:apigateway:ap-south-1:sns:path//"  # Provide the correct integration URI
  request_templates = {
    "application/x-www-form-urlencoded" = <<EOF
Action=Publish&TopicArn=$util.urlEncode('arn:aws:sns:ap-south-1:730288278641:CashfreeCallbackAPI')&TopicName='CashfreeCallbackAPI'
&Message=$util.urlEncode($util.urlEncode($input.body))&Subject=test
EOF
  }
  integration_response_templates = {
    "application/json" = <<EOF
#set($inputRoot = $input.path('$'))
#if($inputRoot.body-json.PublishResponse.PublishResult.MessageId)
    {
        "code":200
    }
#else
    {
        "code":200
    }
#end
EOF
  }
}
```
</details>

***

# Output

## Terminal Output

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/1c3b58fa-2136-4ad6-92de-24790585fbc2">

***

## Console Output

### Security Group

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/60879fc6-f6c5-4794-8147-0476470d4a03">

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
