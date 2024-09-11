output "eventbridge_rule_arns" {
  description = "Map of ARNs of the EventBridge rules"
  value       = module.eventbridge_scheduler.eventbridge_rule_arns
}

output "eventbridge_rule_ids" {
  description = "Map of IDs of the EventBridge rules"
  value       = module.eventbridge_scheduler.eventbridge_rule_ids
}

output "lambda_permissions" {
  description = "Lambda permissions allowing EventBridge to invoke Lambda functions"
  value       = module.eventbridge_scheduler.lambda_permissions
}
