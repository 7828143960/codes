variable "region" {
  description = "The AWS region to deploy resources in"
  default     = "eu-north-1"
}

variable "instance_type" {
  description = "The EC2 instance type"
  default     = "t2.micro"
}

variable "ami_id" {
  description = "The ID of the AMI to use"
  default     = "ami-07c8c1b18ca66bb07"
}

variable "key_name" {
  description = "Name of the SSH key pair"
  type        = string
  default     = "navin-key"
}
