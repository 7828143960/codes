variable "region" {
  description = "The AWS region to deploy resources in"
  default     = "us-east-1"
}

variable "instance_type" {
  description = "The EC2 instance type"
  default     = "t2.micro"
}

variable "ami_id" {
  description = "The ID of the AMI to use"
  default     = "ami-0e86e20dae9224db8"
}

variable "key_name" {
  description = "Name of the SSH key pair"
  type        = string
  default     = "navin"
}
