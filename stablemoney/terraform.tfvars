region = "ap-south-1"

eventbridge_rules = [
  {
    name                = "rule_1"
    schedule_expression = "cron(* * * * ? *)"
    lambda_arn          = "arn:aws:lambda:ap-south-1:686255949108:function:otp-based-scrappers"
    description         = "EventBridge Rule to trigger Lambda 1"
  },
  {
    name                = "rule_2"
    schedule_expression = "cron(* * * * ? *)"
    lambda_arn          = "arn:aws:lambda:ap-south-1:686255949108:function:wint-scraper"
    description         = "EventBridge Rule to trigger Lambda 2"
  }
]

tags = {
  "Owner" = "devops"
  "Env"   = "dev"
}

input_transformation = {
  key1 = "value1"
}
