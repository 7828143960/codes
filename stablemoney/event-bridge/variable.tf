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
  default = []
}

variable "tags" {
  description = "Tags to assign to the resources"
  type        = map(string)
}

