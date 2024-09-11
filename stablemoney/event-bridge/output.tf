output "eventbridge_rule_arns" {
  description = "List of ARNs of the EventBridge rules"
  value       = { for rule in aws_cloudwatch_event_rule.schedule_rule : rule.name => rule.arn }
}

output "eventbridge_rule_ids" {
  description = "List of IDs of the EventBridge rules"
  value       = { for rule in aws_cloudwatch_event_rule.schedule_rule : rule.name => rule.id }
}

output "lambda_permissions" {
  description = "Lambda permissions allowing EventBridge to invoke Lambda functions"
  value = { for perm in aws_lambda_permission.allow_eventbridge : perm.statement_id => {
    lambda_arn = perm.function_name
    rule_arn   = perm.source_arn
  } }
}
