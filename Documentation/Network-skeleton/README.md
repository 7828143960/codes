# Network Skeleton Terraform Module Doc

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f95dfc00-7217-45b6-b770-c0f2af907cec">

***

| **Author** | **Created on** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **01 March 2024** | **02 March 2024** | **v1** |

***

# Table of contents
* [Introduction](#Introduction)
* [Flow Diagram](#Flow-Diagram)
* [Directory Structure](#Directory-Structure)
* [Pre-requisites](#Pre-requisites)
* [Usage](#Usage)
* [Tags](#Tags)
* [Inputs](#Inputs)
* [Outputs](#Outputs)
* [Related Works](#Related-Works)
* [Conclusion](#Conclusion)
* [Contact Information](#Contact-Information)
* [References](#References)

***

# Introduction

With the increasing complexity of modern networks and the need for agility in infrastructure management, Terraform has emerged as a powerful tool for automating the provisioning and configuration of network resources.This module is designed to provide a foundational framework for managing network infrastructure as code using Terraform. By leveraging this module, users can quickly establish a standardized and scalable network architecture within their cloud environment.In this document it will guide through the steps to achieve the setup of terraform module for infrastructure setup which involves the modules:

**VPC**

**Subnet**

**Route Table**

**IGW**

**NAT**

**Elastic IP**

**NACL**

**Loab Balancer**

**Target Group**

***

# Pre-requisites

| **Pre-requisite** | **Description** |
| ----------------- | --------------- |
| **AWS Account**   | Access to the AWS Management Console with credentials configured. |
| **Hashicrop Terraform** | Terraform **(v1.7.4)** for configuration of AWS resources. |

***

# Flow Diagram

![image](https://github.com/CodeOps-Hub/Documentation/assets/156057205/9b5ae612-95d9-4cf4-9612-d9874fcfd843)

***

# Directory Structure

<img width="300" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/1e3a5974-f001-4284-9292-a937b4d3669d">

***

# Usage

```shell
module "network" {
    source                         = "./networking"
    vpc_cidr                       = var.vpc_cidr
    vpc_name                       = var.vpc_name
    vpc_tenancy                    = var.vpc_tenancy
    vpc_enable_dns_support         = var.enable_dns_support
    vpc_enable_dns_hostnames       = var.enable_dns_hostnames
    pub_sub_names                  = var.public_subnet_names
    pub_sub_cidr                   = var.public_subnet_cidr
    pub_sub_az                     = var.public_subnet_az
    enable_map_public_ip_on_launch = var.enable_map_public_ip_on_launch
    pvt_sub_names                  = var.private_subnet_names
    pvt_sub_cidr                   = var.private_subnet_cidr
    pvt_sub_az                     = var.private_subnet_az 
    igw_name                       = var.igw
    nat_gtw                        = var.nat_gtw
    dev_pub_rt                     = var.public_route_table_names
    dev_pvt_rt                     = var.private_route_table_names
    sec_grp_name                   = var.Sec_grp_name
    sec_grp_description            = var.sec_grp_creation
    sg_inbound_ports               = var.sec_grp_inbound_ports
    sg_outbound_ports              = var.sec_grp_outbound_ports
    Sec_grp_tags                   = var.sec_grp_tags
    nacl_tag                       = var.nacl-tag
    nacl_inbound_ports             = var.nacl-inbound-ports
    nacl_outbound_ports            = var.nacl-outbound-ports
    lb_name                        = var.lb-name
    lb_type                        = var.lb-type
    lb_listener_port               = var.lb-listener-port
    target_group_name              = var.target-group-name
    target_group_port              = var.target-group-port
}
```

***

# Tags
* Tags are assigned to resources with name variable as prefix.
* Additial tags can be assigned by tags variables as defined above.
  
***

# Inputs

| Name | Description | Type | Default | 
| ---- | ----------- | ---- | ------- | 
| **vpc_cidr** | CIDR for the VPC | `string` | `10.0.1.0/25` |
| **vpc_name** | Name of the VPC | `string` | `dev_vpc` |
| **vpc_tenancy** | Tenancy of the VPC | `string` | `default` |  
| **vpc_enable_dns_support** | A dns support for instances launched into the VPC | `boolean` | `true` | 
| **vpc_enable_dns_hostnames** | A dns hostname for instances launched into the VPC |`boolean` | `true` | 
| **pub_sub_names** | Names of Public Subnets | `string` | `dev-public-subnet-01`,`dev-public-subnet-02` |   
| **pub_sub_cidr** | CIDRs for Public Subnets | `string` | `10.0.1.0/28`, `10.0.1.64/28` |  
| **pub_sub_az** | Availability Zone for Public Subnets | `string` | `ap-south-1a`, `ap-south-1b` |  
| **enable_map_public_ip_on_launch** | Enabling map for public ip  | `boolean` | `true` |  
| **pvt_sub_names** | Names of Private Subnets | `string` | `dev-frontend-subnet`, `dev-backend-subnet`, `dev-database-subnet` | 
| **pvt_sub_cidr** | CIDRs for Private Subnets | `string` | `10.0.1.16/28`, `10.0.1.32/28`, `10.0.1.48/28` |  
| **pvt_sub_az** | Availability Zone for Private Subnets | `string` | `ap-south-1a`, `ap-south-1b`, `ap-south-1c` |  
| **igw_name** | Internet Gateway's name | `string` | `dev-igw` |  
| **nat_gtw** | NAT Gateway's name | `string` | `dev-nat-gtw` |   
| **dev_pub_rt** | Public Route Table | `string` | `dev_pub_rt` |  
| **dev-pvt-rt** | Private Route Table | `string` | `dev_pvt_rt` |  
| **sec_grp_name** | Nmae of the Security Group | `string` | `dev_sg` | 
| **sec_grp_description** | Description for the Security Group | `string` | `Security group for Dev Env` |  
| **sg_inbound_ports** | Inbound Rules for Security Group | `object` | `allowed ports` |  
| **sg_outbound_ports** | Outbound Rule for Security Group | `object` | `defined port` |  
| **Sec_grp_tags** | Tag for Security Group | `string` | `dev-sg` | 
| **nacl_tag** | Tag for Network ACL | `string` | `dev-nacl` |  
| **nacl_inbound_ports** | Inbound rules for NACL | `object` | `allowed ports` |  
| **nacl_outbound_ports** | Outbound rules for NACL | `object` | `allowed ports` |  
| **lb_name** | Load Balancer Name | `string` | `dev-LB` |  
| **lb_type** | Load Balancer Type | `string` | `application` |  
| **lb_listener_port** | Listner Rule for Load Balancer | `number`| `80` |  
| **target_group_name** | Name of the Target Group | `string` | `dev-TG` |  
| **target_group_port** | Port of the Target Group | `number` | `80` |  

***

# Outputs 

| Name | Description |
|------|-------------|
| **vpc_id** | The ID of the VPC |
| **dev-pub-rt-id** | Public route table ID |
| **dev-pvt-rt-id** | Private Route table ID |
| **dev-igw-id** | Internet Gateway ID |
| **pvt_subnet_id** | Private Subnet IDs |
| **pub_subnet_id** | Public Subnet IDs |
| **Security_Group_id** | Security Group ID |
| **dev-nat-id** | NAT Gateway ID |
| **nacl_id** | Network ACL ID |
| **lb_dns_name** | The DNS name of the load balancer |
| **lb_arn** | The ARN of the load balancer |
| **target_group_arn** | The ARN of the target group |

***

# Related Works

| **Reference** | 
| ------------- |
| [VPC](https://github.com/CodeOps-Hub/Terraform/tree/harshit/vpc/Dev_Infra/Static_Tf/network) |
| [Subnet](https://github.com/CodeOps-Hub/Terraform/blob/harshit/subnets/Dev_Infra/Static_Tf/network/main.tf) |
| [Route Table](https://github.com/CodeOps-Hub/Terraform/blob/harshit/routeTable/Dev_Infra/Static_Tf/network/main.tf) |
| [Internet Gateway](https://github.com/CodeOps-Hub/Terraform/blob/harshit/igw/Dev_Infra/Static_Tf/network/main.tf) |
| [NAT Gateway](https://github.com/CodeOps-Hub/Terraform/blob/harshit/nat/Dev_Infra/Static_Tf/network/main.tf) |

***

# Conclusion

In conclusion, the Network Skeleton Terraform module offers a flexible and efficient solution for managing network infrastructure within a Terraform-based environment. By adopting this module, users can benefit from a standardized approach to defining and provisioning network resources, resulting in improved scalability, reliability, and maintainability of their infrastructure.

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
| [Link](https://github.com/CodeOps-Hub/Documentation/blob/main/Terraform/Design/Design-tf-modules/Network-skeleton/AWS-resources.md)  |  AWS Resources Image Link |
