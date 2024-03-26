#  Terraform Wrapper Code For QA-OpenVPN 

<img width="360" length="100" alt="Terraform" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f95dfc00-7217-45b6-b770-c0f2af907cec">

***

| **Author** | **Created on** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **15 March 2024** | **15 March 2024** | **v1** |

***

# Table of contents
* [Introduction](#Introduction)
* [Why Terraform Wrapper Code](#Why-Terraform-Wrapper-Code)
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

In this document it will guide through the steps to achieve the setup of wrapper code for Openvpn server which involves the configuration files.According to the value passed through the root module/wrapper code we can manage/build the infra structure setup accordingly. Which involves the managing of different terraform state files for managing respective environments.

***

# Why Terraform Wrapper Code

| Feature                | Description                    |
|------------------------|---------------------------------------------------------------------------------------------------------------------|
| **Automation**      | Automates repetitive tasks like initialization, applying changes, and destroying resources, saving time and effort.|
| **Enhanced Functionality** | Extends Terraform's capabilities by integrating with other tools, implementing custom logic, or enforcing specific workflows.|
| **Standardization**        | Enforces standardized practices across Terraform projects, ensuring consistency and reducing the risk of errors.|
| **Integration**            | Facilitates integration with CI/CD pipelines, version control systems, and infrastructure management platforms for seamless deployment. |

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

<details>
<summary> <b> Click here for main.tf file </b> </summary>
<br>
    
```shell
module "openvpn" {
    source                         = "git@github.com:CodeOps-Hub/Terraform-modules.git//Modules/VM-Module?ref=main"  
    vpc_id                         = var.vpc-id 
    subnet_id                      = var.subnet-id         
    sec_grp_name                   = var.sec-grp-name
    Sec_grp_description            = var.Sec-grp-description
    inbound_ports                  = var.inbound-ports
    outbound_ports                 = var.outbound-ports
    Sec_grp_tags                   = var.Sec-grp-tags
    key_name                       = var.key-name
    server_type                    = var.server-type
    server_name                    = var.server-name
}    
```
</details>

***

### variables.tf file

<details>
<summary> <b> Click here for variables.tf file </b> </summary>
<br>

```shell
variable "sec-grp-name" {
  description     = "Name of the security group"
  type            = string
  default         = "qa-openvpn-sg"
}
variable "Sec-grp-description" {
  description     = "Description for the security group"
  type            = string
  default         = "Security group for Open Vpn in QA Env"
}
variable "vpc-id" {
  description     = "ID of the VPC for instances"
  type            = string
}

variable "inbound-ports" {
  description     = "List of inbound ports, protocols and cidr block"
  type            = list(map(any))
  default         = [
    { port = 22, protocol = "TCP",cidr_blocks = "20.0.0.0/28" },  
    { port = 1194, protocol = "UDP",cidr_blocks = "0.0.0.0/0"},
  ]
}

variable "outbound-ports" {
  description     = "List of outbound ports, protocols and Cidr block "
  type            = list(map(any))
  default         = [
    { port = 0, protocol = "-1", cidr_blocks = "0.0.0.0/0", },
  ]
}
variable "Sec-grp-tags" {
  type            = map(string)
  default         = {
    Name          = "qa-openvpn-sg"
    Enviroment    = "qa"
    Owner         = "shreya"
  }
}

variable "key-name" {
  description = "Name of the SSH key pair"
  type        = string
}

variable "server-type" {
  description = "Instance type for qa-openvpn server"
  type        = string
}

variable "subnet-id" {
  description = "ID of the subnet for the qa-openvpn server"
  type        = string
}

variable "server-name" {
  description = "Name tag for the openvpn qa-server"
  type        = string
}
```
</details>

***

### output.tf file

<details>
<summary> <b> Click here for output.tf file </b> </summary>
<br>

```shell
output "Security_Group_ID" {
  value = module.openvpn.Security_Group_ID
}
output "server_id" {
  value = module.openvpn.server_id
}
```
</details>

***

### provider.tf file

<details>
<summary> <b> Click here for provider.tf file </b> </summary>
<br>
    
```shell
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.38.0"  # Using a minimum version constraint
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "us-east-1"  
} 
```
</details>

***

### terraform.tfvars

<details>
<summary> Click here to see terraform.tfvars file</summary>
<br>
    
```shell
vpc-id              = "vpc-0a744f2efc4febb2a"
key-name            = "snaatak.pem"
server-type         = "t2.micro"
subnet-id           = "subnet-009e342da70d1d460"
server-name         = "qa-openvpn-Server"

```
</details>

***

# Output

## Terminal Output

<img width="524" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/cd8fbfc7-52ef-4b35-b8dd-76398cd5d9fa">

***

## Console Output

### Private Key (snaatak.pem)

<img width="800" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/919b9aaf-ece5-4ee6-ac69-e7f81f2093a2">

***

### Security Group (qa-openvpn-sg)

<img width="800" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/aa25f60a-7257-4a60-86ba-908c65a91c96">

***

### EC2 Instance (qa-Openvpn-server)

<img width="800" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/7657c7d7-11d5-4079-9b30-b0eaede59d91">

***

# Best Practices

| Practice           | Description                                                                                                                                   |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| **Modularity**         | Design wrapper scripts in a modular way, with separate functions for each Terraform command or task.                                          |
| **Error Handling**     | Implement robust error handling mechanisms to gracefully handle failures and provide informative error messages.                             |
| **Logging**            | Include logging functionality to capture relevant information and debug issues during Terraform execution.                                    |
| **Version Control**    | Store wrapper scripts alongside Terraform configurations in version control repositories for versioning, collaboration, and auditability.  |
| **Documentation**      | Provide comprehensive documentation for wrapper scripts, including usage instructions, dependencies, and troubleshooting tips.               |
| **Security**           | Follow security best practices to protect sensitive information such as credentials and API keys used by wrapper scripts.                   |


***

# Conclusion

Terraform wrapper code enhances the capabilities of Terraform by automating tasks, providing additional functionality, and enforcing best practices. By following best practices such as modularity, error handling, and documentation, wrapper scripts can streamline Terraform workflows, improve productivity, and ensure the reliability and security of infrastructure deployments.The OpenVPN wrapper code makes it easier to set up and manage OpenVPN by simplifying complex tasks into easy steps. It helps improve security and makes it simpler to expand and maintain the VPN. With this wrapper, you can easily connect OpenVPN with your existing systems and automate tasks, making it faster to deploy and monitor your VPN across different setups.

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
| [Link](https://medium.com/@brandon.wagner/a-wrapper-for-terraform-61a125b27ffc) | Terraform Wrapper Code Concept. |
| [Link](https://medium.com/@selvamraju007/terraform-modules-explanation-726ba4a0b98e) | Reference Link For Terraform Modules. |
