module "eventbridge_scheduler" {
  source = "./event-bridge"

  region                                = var.region
  eventbridge_rules                     = var.eventbridge_rules
  tags                                  = var.tags
  lambda_permission_statement_id_prefix = var.lambda_permission_statement_id_prefix
  lambda_target_id_prefix               = var.lambda_target_id_prefix
  input_transformation                  = var.input_transformation
}
