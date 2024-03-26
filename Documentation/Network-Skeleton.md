# Network Skeleton Terraform Module Doc

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f95dfc00-7217-45b6-b770-c0f2af907cec">

***

| **Author** | **Created on** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **13 March 2024** | **13 March 2024** | **v1** |

***

# Table of contents
* [Introduction](#Introduction)
* [Why Terraform Module](#Why-Terraform-Module)
* [Flow Diagram](#Flow-Diagram)
* [Pre-requisites](#Pre-requisites)
* [Directory Structure](#Directory-Structure)
* [Configuration Files](#Configuration-Files)
* [Output](#Output)
* [Best Practices](#Best-Practices)
* [Conclusion](#Conclusion)
* [Contact Information](#Contact-Information)
* [References](#References)

***

# Introduction

<img width="400" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/643d0c30-6c1e-4088-bba3-5b1c91be9253">

A Terraform module is a container for organizing and reusing Terraform code to manage related resources. It encapsulates a set of resources and configurations into a single unit that can be easily shared, reused, and versioned. Modules promote code reusability, maintainability, and scalability in Terraform projects.

***

# Why Terraform Module

| Benefit           | Description                                                                                                      |
| ----------------  | ---------------------------------------------------------------------------------------------------------------- |
| **Reusability**   | Define reusable components for different Terraform configurations or projects.                                   |
| **Abstraction**   | Abstract the complexity of infrastructure components, making them easier to manage and understand.               |
| **Consistency**   | Enforce consistency by providing a standardized way to create and manage resources, reducing configuration drift.|
| **Collaboration** | Enable collaboration by allowing teams to share infrastructure components as reusable building blocks.           |
| **Versioning**    | Version modules to track changes and ensure consistent deployments across environments.                          |

***

# Flow Diagram

<img width="950" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/9b5ae612-95d9-4cf4-9612-d9874fcfd843">

***

# Pre-requisites

| **Pre-requisite** | **Description** |
| ----------------- | --------------- |
| **AWS Account**   | Access to the AWS Management Console with credentials configured. |
| **Hashicrop Terraform** | Terraform **(v1.7.4)** for configuration of AWS resources. |

***

# Directory Structure

<img width="400" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f3286afd-84d4-487c-b185-f6bc00787194">

***

# Configuration Files

### main.tf file

**The `main.tf` file contains the configuration for creating various AWS resources such as VPC, subnets, internet gateway, NAT gateway, route tables, network ACLs, security groups, ALB, Route 53 zone, and records.**

<details>
<summary> Click here to see main.tf file</summary>
<br>
  
```shell
/*--------------- VPC ---------------*/

resource "aws_vpc" "dev_vpc" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = var.vpc_enable_dns_support
  enable_dns_hostnames = var.vpc_enable_dns_hostnames
  tags = var.vpc_tags
}

/*--------------- Public Subnets ---------------*/

resource "aws_subnet" "dev_public_subnets" {
  count                   = length(var.public_subnets_cidr)
  vpc_id                  = aws_vpc.dev_vpc.id
  cidr_block              = var.public_subnets_cidr[count.index]
  availability_zone       = var.public_subnets_az[count.index]
  map_public_ip_on_launch = var.enable_map_public_ip_on_launch
  tags                    = var.public_subnets_tags[count.index]
}

/*--------------- Private Subnets ---------------*/

resource "aws_subnet" "dev_private_subnets" {
  count             = length(var.private_subnets_cidr)
  vpc_id            = aws_vpc.dev_vpc.id
  cidr_block        = var.private_subnets_cidr[count.index]
  availability_zone = var.private_subnets_az
  tags              = var.private_subnets_tags[count.index]
}

/*--------------- # Internet Gateway ---------------*/

resource "aws_internet_gateway" "dev_igw" {
  vpc_id = aws_vpc.dev_vpc.id
  tags = var.igw_tags
}

/*--------------- Elastic IP ---------------*/

resource "aws_eip" "dev_elastic_ip" {
  domain = "vpc"
}

/*--------------- NAT Gateway ---------------*/

resource "aws_nat_gateway" "dev_nat" {
  allocation_id = aws_eip.dev_elastic_ip.id
  subnet_id     = aws_subnet.dev_public_subnets[0].id
  tags = var.nat_tags
  depends_on = [aws_eip.dev_elastic_ip]
}

/*--------------- Public Route Table ---------------*/

resource "aws_route_table" "dev_public_rtb" {
  vpc_id = aws_vpc.dev_vpc.id
  route {
    cidr_block = var.vpc_cidr
    gateway_id = "local"
  }
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.dev_igw.id
  }
  tags = var.public_route_table_tags
}

/*--------------- Public RTB Association ---------------*/

resource "aws_route_table_association" "dev_public_route_association01" {
  count = length(aws_subnet.dev_public_subnets.*.id)
  subnet_id      = aws_subnet.dev_public_subnets[count.index].id
  route_table_id = aws_route_table.dev_public_rtb.id
}
/*--------------- Private RTB ---------------*/

resource "aws_route_table" "dev_private_rtb" {
  vpc_id = aws_vpc.dev_vpc.id
  route {
    cidr_block = var.vpc_cidr
    gateway_id = "local"
  }
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.dev_nat.id
  }
  tags = var.private_route_table_tags
  depends_on = [aws_nat_gateway.dev_nat]
}

/*--------------- Private RTB Association ---------------*/

resource "aws_route_table_association" "dev_private_route_association01" {

  count = length(aws_subnet.dev_private_subnets.*.id)
  subnet_id      = aws_subnet.dev_private_subnets[count.index].id
  route_table_id = aws_route_table.dev_private_rtb.id
  depends_on     = [aws_route_table.dev_private_rtb]
}

/*--------------- Frontend NACL ---------------*/

resource "aws_network_acl" "dev_frontend_nacl" {
  vpc_id = aws_vpc.dev_vpc.id

  dynamic "ingress" {
    for_each = var.frontend_nacl_ingress
    content {
      protocol   = ingress.value.protocol
      rule_no    = ingress.value.rule_no
      action     = ingress.value.action
      cidr_block = ingress.value.cidr_block
      from_port  = ingress.value.from_port
      to_port    = ingress.value.to_port
    }
  }
  dynamic "egress" {
    for_each = var.frontend_nacl_egress
    content {
      protocol   = egress.value.protocol
      rule_no    = egress.value.rule_no
      action     = egress.value.action
      cidr_block = egress.value.cidr_block
      from_port  = egress.value.from_port
      to_port    = egress.value.to_port
    }
  }
  tags = var.frontend_nacl_tags
}
/*--------------- Frontend NACL Subnet Association ---------------*/

resource "aws_network_acl_association" "dev_frontend_nacl" {
  network_acl_id = aws_network_acl.dev_frontend_nacl.id
  subnet_id      = aws_subnet.dev_private_subnets[0].id
}

/*--------------- Backend NACL ---------------*/

resource "aws_network_acl" "dev_backend_nacl" {
  vpc_id = aws_vpc.dev_vpc.id

  dynamic "ingress" {
    for_each = var.backend_nacl_ingress
    content {
      protocol   = ingress.value.protocol
      rule_no    = ingress.value.rule_no
      action     = ingress.value.action
      cidr_block = ingress.value.cidr_block
      from_port  = ingress.value.from_port
      to_port    = ingress.value.to_port
    }
  }
  dynamic "egress" {
    for_each = var.backend_nacl_egress
    content {
      protocol   = egress.value.protocol
      rule_no    = egress.value.rule_no
      action     = egress.value.action
      cidr_block = egress.value.cidr_block
      from_port  = egress.value.from_port
      to_port    = egress.value.to_port
    }
  }
  tags = var.backend_nacl_tags
}
/*--------------- Backend NACL Subnet Association ---------------*/

resource "aws_network_acl_association" "dev_backend_nacl_assc" {
  network_acl_id = aws_network_acl.dev_backend_nacl.id
  subnet_id      = aws_subnet.dev_private_subnets[1].id
}

/*--------------- Database NACL ---------------*/

resource "aws_network_acl" "dev_db_nacl" {
  vpc_id = aws_vpc.dev_vpc.id

  dynamic "ingress" {
    for_each = var.db_nacl_ingress
    content {
      protocol   = ingress.value.protocol
      rule_no    = ingress.value.rule_no
      action     = ingress.value.action
      cidr_block = ingress.value.cidr_block
      from_port  = ingress.value.from_port
      to_port    = ingress.value.to_port
    }
  }
  dynamic "egress" {
    for_each = var.db_nacl_egress
    content {
      protocol   = egress.value.protocol
      rule_no    = egress.value.rule_no
      action     = egress.value.action
      cidr_block = egress.value.cidr_block
      from_port  = egress.value.from_port
      to_port    = egress.value.to_port
    }
  }
  tags = var.db_nacl_tags
}
/*--------------- Database NACL Subnet Association ---------------*/

resource "aws_network_acl_association" "dev_db_nacl_assc" {
  network_acl_id = aws_network_acl.dev_db_nacl.id
  subnet_id      = aws_subnet.dev_private_subnets[2].id
}

/*--------------- ALB Security Group ---------------*/


resource "aws_security_group" "dev_alb_sg" {
  name        = var.alb_sg_name
  description = var.alb_sg_description
  vpc_id      = aws_vpc.dev_vpc.id

  dynamic "ingress" {
    for_each = var.alb_sg_inbound_rules
    content {
      from_port   = ingress.value.port
      to_port     = ingress.value.port
      protocol    = ingress.value.protocol
      cidr_blocks = [ingress.value.source]
    }
  }

  dynamic "egress" {
    for_each = var.alb_sg_outbound_rules
    content {
      from_port       = egress.value.port
      to_port         = egress.value.port
      protocol        = egress.value.protocol
      cidr_blocks     = [egress.value.source]
    }
  }
  tags = var.alb_sg_tags
}

/*--------------- ALB ---------------*/

resource "aws_lb" "dev_alb" {
  name               = var.alb_name
  internal           = var.alb_internal
  load_balancer_type = var.alb_type
  security_groups    = [aws_security_group.dev_alb_sg.id]
  subnets            = [for subnet in aws_subnet.dev_public_subnets : subnet.id]

  enable_deletion_protection = var.alb_deletion_protection

  tags = var.alb_tags
}

/*--------------- Route 53   ---------------*/

resource "aws_route53_zone" "dev_route53_zone" {
 name = var.route53_zone_name
 vpc {
   vpc_id = aws_vpc.dev_vpc.id
   vpc_region = var.region
 }
 tags = var.route53_zone_tags
}

resource "aws_route53_record" "dev_route53_record" {
  zone_id = aws_route53_zone.dev_route53_zone.id
  name    = "example.com"
  type    = "A"

  alias {
    name                   = aws_lb.dev_alb.dns_name
    zone_id                = aws_lb.dev_alb.zone_id
    evaluate_target_health = true
  }
}
```
</details>

***

### variables.tf file

**The `variables.tf` file defines input variables used in the `main.tf` file to parameterize the configuration. These variables include VPC settings, subnet details, internet gateway tags, NAT gateway tags, route table tags, network ACLs, ALB security group settings, ALB configuration, Route 53 zone tags, and record settings.**

<details>
<summary> Click here to see variables.tf file</summary>
<br>
  
```shell
variable "region" {
  type    = string
  default = "us-east-2"
}
/*---------------vpc variables---------------*/

variable "vpc_cidr" {
  type    = string
  default = "10.0.1.128/25"
}

variable "vpc_enable_dns_support" {
  type    = bool
  default = true
}

variable "vpc_enable_dns_hostnames" {
  type    = bool
  default = false
}

variable "vpc_tags" {
  type    = map(string)
  default = {
    Name = "QA-vpc"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- Public Subnets ---------------*/

variable "public_subnets_cidr" {
  description = "public subnet cidr block"
  type        = list(string)
  default     = ["10.0.1.128/28", "10.0.1.144/28"]

}

variable "public_subnets_az" {
  description = "public subnet availability zones"
  type        = list(string)
  default     = ["us-east-2a", "us-east-2b"]
}
variable "enable_map_public_ip_on_launch" {
  type    = bool
  default = true
}

variable "public_subnets_tags" {
  description = "public subnet tags"
  type        = list(map(string))
  default = [{
    Name = "public-subnet-01"
    Enviroment = "QA"
    Owner = "shreya"
    }, {
    Name = "public-subnet-02"
    Enviroment = "QA"
    Owner = "shreya"
  }]
}

/*--------------- Private Subnets ---------------*/

variable "private_subnets_cidr" {
  description = "private subnet cidr block"
  type        = list(string)
  default     = ["10.0.1.160/28", "10.0.1.176/28", "10.0.1.192/28"]

}

variable "private_subnets_az" {
  description = "private subnet availability zones"
  type        = string
  default     = "us-east-2a"

}

variable "private_subnets_tags" {
  description = "private subnet tags"
  type        = list(map(string))
  default = [{
    Name = "frontend-subnet"
    Enviroment = "QA"
    Owner = "shreya"
    }, {
    Name = "backend-subnet"
    Enviroment = "QA"
    Owner = "shreya"
  }, {
    Name = "database-subnet"
    Enviroment = "QA"
    Owner = "shreya"
  }]
}

/*--------------- # Internet Gateway ---------------*/

variable "igw_tags" {
  type    = map(string)
  default = {
    Name = "QA-igw-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- # NAT Gateway ---------------*/

variable "nat_tags" {
  type    = map(string)
  default = {
    Name = "QA-nat-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- # Route Table ---------------*/

variable "public_route_table_tags" {
  type    = map(string)
  default = {
    Name = "QA-public-RTB-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

variable "private_route_table_tags" {
  type    = map(string)
  default = {
    Name = "QA-private-RTB-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- Frontend NACL ---------------*/

variable "frontend_nacl_ingress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "frontend_nacl_egress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "frontend_nacl_tags" {
  type    = map(string)
  default = {
    Name = "QA-frontend-nacl-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- Backend NACL ---------------*/

variable "backend_nacl_ingress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "backend_nacl_egress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "backend_nacl_tags" {
  type    = map(string)
  default = {
    Name = "QA-backend-nacl-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- Database NACL ---------------*/

variable "db_nacl_ingress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "db_nacl_egress" {
  type    = list(object({
    rule_no = number
    protocol = string
    cidr_block = string
    from_port = number
    to_port = number
    action = string
  }))
}

variable "db_nacl_tags" {
  type    = map(string)
  default = {
    Name = "QA-db-nacl-01"
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- ALB Security Group ---------------*/

variable "alb_sg_name" {
  description = "security group name"
  type            = string
  default         = "QA-alb-sg"
}

variable "alb_sg_description" {
  description = "security group for Attendance API"
  type            = string
  default         = "Security group for QA-ALB"
}

variable "alb_sg_inbound_rules" {
  description = "List of inbound rules for the security group"
  type = list(object({
    port     = number
    source   = string
    protocol = string
  }))
  default = [{
      port     = 80
      source   = "0.0.0.0/0" 
      protocol = "tcp"  
    }, {
      port     = 443
      source   = "0.0.0.0/0" 
      protocol = "tcp"  
    }]
}

variable "alb_sg_outbound_rules" {
  description = "List of outbound rules for the security group"
  type = list(object({
    port     = number
    source   = string
    protocol = string
  }))
  default = [
    {
      port     = 0 // allow all ports 
      source   = "0.0.0.0/0"
      protocol = "-1"  // all protocols
    }
  ]
}

variable "alb_sg_tags" {
  description = "Tag for Attedance sg"
  type        = map(string)
  default     = {
    Environment = "QA"
    Owner       = "shreya"
  }
}

/*--------------- ALB ---------------*/
variable "alb_name" {
  type = string
  default = "QA-alb"
}
variable "alb_internal" {
  type = bool
  default = false 
}
variable "alb_type" {
  type = string
  default = "application"  
}
variable "alb_deletion_protection" {
  type = bool
  default = true
}
variable "alb_tags" {
  type    = map(string)
  default = {
    Enviroment = "QA"
    Owner = "shreya"
  }
}

/*--------------- Route 53 ---------------*/

variable "route53_zone_tags" {
  type    = map(string)
  default = {
    Enviroment = "QA"
    Owner = "shreya"
  }
}
variable "route53_zone_name" {
  type = string
  default = "example.com"
}
```
</details>

***

### output.tf file

**The `output.tf` file specifies the outputs of the Terraform configuration. It includes the IDs of various resources created such as VPC, subnets, internet gateway, NAT gateway, route tables, network ACLs, ALB security group, ALB DNS name, Route 53 zone name servers, and Route 53 record name.**

<details>
<summary> Click here to see output.tf file</summary>
<br>

```shell
output "vpc-id" {
  value = aws_vpc.dev_vpc.id
}

output "public-subnets-id" {
  value = aws_subnet.dev_public_subnets.*.id
}


output "private-subnets-id" {
  value = aws_subnet.dev_private_subnets.*.id
}

output "dev-igw-id" {
  value = aws_internet_gateway.dev_igw.id
}

output "dev-nat-id" {
  value = aws_nat_gateway.dev_nat.id
}

output "dev-public-RTB-id" {
  value = aws_route_table.dev_public_rtb.id
}

output "dev-private-RTB-id" {
  value = aws_route_table.dev_private_rtb.id
}

output "dev-frontend-nacl-id" {
  value = aws_network_acl.dev_frontend_nacl.id
}

output "dev-backend-nacl-id" {
  value = aws_network_acl.dev_backend_nacl.id
}

output "dev-db-nacl-id" {
  value = aws_network_acl.dev_db_nacl.id
}

output "alb-sg-id" {
  value = aws_security_group.dev_alb_sg.id  
}

output "dev-alb-dns" {
  value = aws_lb.dev_alb.dns_name
}

output "dev-route53-zone" {
  value = aws_route53_zone.dev_route53_zone.name_servers
}
output "dev-route53-record" {
  value = aws_route53_record.dev_route53_record.name
}
```
</details>

***

# Output

## Terminal Output

<img width="524" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/bd9a27e6-97e7-467b-b3ac-ad2cd7df65e6">

***

## Console Output

### VPC & it's resources

![Screenshot 2024-03-13 101153](https://github.com/CodeOps-Hub/Documentation/assets/156057205/742b3b6c-d16a-4189-a168-8d0b85d1f761)

***

### Elastic IP

![Screenshot 2024-03-13 101337](https://github.com/CodeOps-Hub/Documentation/assets/156057205/1befbce1-79cd-4c48-baa1-27e4b481b43c)

***

### NACLs

**Frontend NACL**

![Screenshot 2024-03-13 101312](https://github.com/CodeOps-Hub/Documentation/assets/156057205/e198c4cd-80d3-4624-8bb2-246ec8daba98)

***

**Backend NACL**

![Screenshot 2024-03-13 101257](https://github.com/CodeOps-Hub/Documentation/assets/156057205/29912ca1-bcc6-4301-9413-ca24ed050262)

***

**DB NACL**

![Screenshot 2024-03-13 101244](https://github.com/CodeOps-Hub/Documentation/assets/156057205/c79ed561-5823-4722-b3d1-4def3472b801)

***

### Security Group

![Screenshot 2024-03-13 101512](https://github.com/CodeOps-Hub/Documentation/assets/156057205/84aa070c-8dbf-4707-9b03-8bc6a6e95e6f)

***

### Load Balancer

![Screenshot 2024-03-13 101649](https://github.com/CodeOps-Hub/Documentation/assets/156057205/67cda353-793f-43c7-8b44-2eeace3316c8)

***

### Route53

![Screenshot 2024-03-13 101748](https://github.com/CodeOps-Hub/Documentation/assets/156057205/c21a501b-bc82-40a1-b09a-0ffddf4deea7)

***

# Best Practices

| Practice              | Description                                                                                                        |
|-----------------------|------------------------------------------------------------------------------------------------------------------- |
| **Atomicity**         | Keep modules small and focused on a single concern or resource type to promote reusability and maintainability.    |
| **Input Validation**  | Validate input variables to ensure they meet expected requirements and constraints.                                |
| **Documentation**     | Provide clear documentation, including usage examples, input variables, and outputs.                               |
| **Testing**           | Implement automated tests to validate functionality and prevent regressions.                                       |
| **Versioning**        | Follow semantic versioning to manage changes and dependencies effectively.                                         |
| **Error Handling**    | Implement error handling and logging mechanisms for troubleshooting.                                               |
| **Idempotency**       | Ensure modules are idempotent, allowing multiple applications without unintended changes.                          |
| **State Management**  | Avoid storing sensitive information in Terraform state files and leverage remote state management for better security.|
| **Separation of Concerns**| Separate infrastructure concerns into reusable modules based on functionality or lifecycle.                       |

***

# Conclusion

Terraform modules are a fundamental building block for creating reusable and maintainable infrastructure as code. By following best practices and principles such as modularity, abstraction, and versioning, teams can leverage modules to efficiently manage complex infrastructure deployments at scale. Investing in well-designed modules promotes consistency, collaboration, and agility in Terraform projects, ultimately leading to more reliable and manageable infrastructure.

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# References

| **Source** | **Description** |
| ---------- | --------------- |
| [Link](https://github.com/CodeOps-Hub/Documentation/blob/main/Application_CI/Implementation/GenericDoc/Terraform/terraform.md) | Terraform Generic Doc Link. |
| [Link](https://developer.hashicorp.com/terraform/language/modules) | Terraform Module Concept. |
| [Link](https://medium.com/@selvamraju007/terraform-modules-explanation-726ba4a0b98e) | Reference Link For Terraform Modules. |


