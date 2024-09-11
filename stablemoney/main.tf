module "eventbridge_scheduler" {
  source = "./event-bridge"

  region            = var.region
  eventbridge_rules = var.eventbridge_rules
  tags              = var.tags
}
