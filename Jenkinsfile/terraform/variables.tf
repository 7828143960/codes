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
  default     = "ami-0c55b159cbfafe1f0"
}

variable "subnet_id" {
  description = "The subnet ID where the EC2 instance will be deployed"
}

variable "vpc_id" {
  description = "The VPC ID where the security group will be created"
}
