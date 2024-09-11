# variables.tf

variable "region" {
  description = "AWS region to deploy resources"
  type        = string
}

variable "eventbridge_rules" {
  description = "List of EventBridge rules with Lambda ARNs"
  type = list(object({
    name                = string
    schedule_expression = string
    lambda_arn          = string
    description         = string
  }))
}

variable "tags" {
  description = "Tags to assign to the resources"
  type        = map(string)
}

variable "lambda_permission_statement_id_prefix" {
  description = "Prefix for the Lambda permission statement IDs"
  type        = string
}

variable "lambda_target_id_prefix" {
  description = "Prefix for the Lambda target IDs"
  type        = string
}

variable "input_transformation" {
  description = "Optional input transformation to the Lambda function"
  type        = map(string)
  default     = {}
}

