resource "aws_cloudwatch_event_rule" "schedule_rule" {
  for_each = { for rule in var.eventbridge_rules : rule.name => rule }

  name                = each.value.name
  description         = each.value.description
  schedule_expression = each.value.schedule_expression
  tags                = var.tags
}

resource "aws_lambda_permission" "allow_eventbridge" {
  for_each = { for rule in var.eventbridge_rules : rule.name => rule }

  statement_id  = "${var.lambda_permission_statement_id_prefix}_${each.key}"
  action        = "lambda:InvokeFunction"
  function_name = each.value.lambda_arn
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.schedule_rule[each.key].arn
}

resource "aws_cloudwatch_event_target" "lambda_target" {
  for_each = { for rule in var.eventbridge_rules : rule.name => rule }

  rule      = aws_cloudwatch_event_rule.schedule_rule[each.key].name
  target_id = "${var.lambda_target_id_prefix}_${each.key}"
  arn       = each.value.lambda_arn

  # Optional input transformation to the Lambda function
  input = jsonencode(var.input_transformation)
}

