# Network Skeleton Terraform Module Doc

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f95dfc00-7217-45b6-b770-c0f2af907cec">

***

| **Author** | **Created on** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **05 March 2024** | **05 March 2024** | **v1** |

***

# Table of contents
* [Introduction](#Introduction)
* [Terraform Modules](#Terraform-Modules)
* [What is State file](#What-is-State-file)
* [Pre-requisites](#Pre-requisites)
* [Flow Diagram](#Flow-Diagram)
* [Directory Structure](#Directory-Structure)
* [Usage](#Usage)
* [Tags](#Tags)
* [Inputs](#Inputs)
* [Outputs](#Outputs)
* [Result](#Result)
* [Conclusion](#Conclusion)
* [Contact Information](#Contact-Information)
* [References](#References)

***

# Introduction

With the increasing complexity of modern networks and the need for agility in infrastructure management, Terraform has emerged as a powerful tool for automating the provisioning and configuration of network resources.This module is designed to provide a foundational framework for managing network infrastructure as code using Terraform. By leveraging this module, users can quickly establish a standardized and scalable network architecture within their cloud environment.

***

# Terraform Modules

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/b2ec5454-cc39-4212-9dd9-fbc7b9967317">

Terraform modules serve as fundamental building blocks within the Terraform infrastructure as code (IaC) framework, enabling users to encapsulate and reuse configuration logic across different projects and environments. Essentially, a Terraform module is a collection of resources and associated configuration files that define a specific piece of infrastructure, such as a virtual machine, network, or application. By organizing infrastructure components into modular units, users can achieve greater abstraction, maintainability, and scalability in their infrastructure provisioning workflows.Additionally, Terraform modules support parameterization, allowing users to customize module behavior through input variables and outputs, further enhancing flexibility and adaptability.

***

# What is State file?

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/40ac0ac5-1a6a-4221-b2a8-f97467608f01">

The state file is an artifact that you’re left with once an Infrastructure as Code framework finishes a deployment. It is the “single source of truth” about what was deployed, where it was deployed, and all the configuration needed to deploy it. 

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

<img width="242" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/13a20fce-0900-4467-ab59-a24772f89a56">

***

# Usage

```shell
module "network" {
    source                         = "./networking"
    vpc_id                         = var.vpc-id
    subnet_id                      = var.subnet-id
    sec_grp_name                   = var.sec-grp-name
    sec_grp_description            = var.sec-grp-description
    sg_inbound_ports               = var.inbound-ports
    sg_outbound_ports              = var.outbound-ports
    Sec_grp_tags                   = var.sec-grp-tags
    key_name                       = var.key-name
    server_type                    = var.server-type
    server_name                    = var.server-name
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
| **vpc_id** | ID of the VPC | `string` | `172.31.0.0/16` |
| **sec_grp_name** | Nmae of the Security Group | `string` | `dev_sg` | 
| **sec_grp_description** | Description for the Security Group | `string` | `Security group for Dev Env` |  
| **inbound-ports** | Inbound Rules for Security Group | `object` | `allowed ports` |  
| **outbound-ports** | Outbound Rule for Security Group | `object` | `defined port` |  
| **Sec_grp_tags** | Tag for Security Group | `string` | `dev-sg` | 
| **key_name**  | SSH Key name | `string` | `VM-key.pem` |
| **server-type**  | EC2 instance type | `string` | `t2.micro` |  
| **server-name**  | Name of the EC2 instance | `string` | `Standalone-server` |

***

# Outputs 

| Name | Description |
|------|-------------|
| **Security_Group_id** | Security Group ID |
| **server_id** | EC2 instance ID |

***

# Result

<img width="562" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/2e0a08e1-df25-409f-a0b8-b20fd841a091">

***
<img width="959" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/2ca7ea10-00bc-4630-9b14-819314273221">

***

<img width="959" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/346f2522-33a9-4f3a-b71f-7a55f1de8a23">

***

<img width="959" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/a7b65630-b957-468c-95b8-5066aaab9b9f">

***

# Conclusion

In conclusion, the standalone VM module presents a compelling solution for organizations seeking flexibility, portability, cost-efficiency, scalability, interoperability, and security in their virtualized environments. By decoupling virtualization from specific hypervisors or cloud platforms, this module offers users the freedom to deploy virtual machines across diverse hardware configurations without vendor lock-in. Its portability enables seamless migration of virtualized workloads across different environments, promoting agility and adaptability.

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
