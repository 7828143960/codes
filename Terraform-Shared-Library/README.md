# Documentation of Terraform CI/CD Using Shared Library

|   Author        |  Created on   |  Version   | Last updated by  | Last edited on |
| --------------- | --------------| -----------|----------------- | -------------- |
| Shreya jaiswal  | 21 August 2024 |  Version 1 | Shreya Jaiswal  | 21 August 2024  |

<img width="894" alt="image" src="https://github.com/user-attachments/assets/73b5cc7b-6d97-4400-ac04-e93d63e0b8ce">


## Table of Contents
- [What is Shared Library?](#What-is-Shared-Library?)
- [What is Terraform?](#What-is-Terraform?)
- [what is CI/CD?](#what-is-CI/CD?)
- [Terraform CI](#Terraform-CI)
- [Terraform CD](#Terraform-CD)
- [Pre-requisites](#Pre-requisites)
- [Terraform Code](#Terraform-Code)
- [Shared Library Code](#Shared-Library-Code)
- [Implementation](#Implementation)
- [Output](#Output)
- [Terraform best-practices when working with terraform](#Terraform-best-practices-when-working-with-terraform)
- [Conclusion](#Conclusion)
- [Contact Information](#Contact-Information)
- [References](#References)
***

## What is Shared Library?
A Shared Library in the context of Jenkins (and specifically with Jenkins Pipelines) is a way to share and reuse code across multiple Jenkins pipelines. Shared Libraries are used to encapsulate common pipeline code or functions that can be used by different Jenkins jobs, reducing duplication and promoting consistency across your CI/CD processes.

***

## What is Terraform?
Terraform is an open-source Infrastructure as Code (IaC) tool developed by HashiCorp. It allows you to define and manage infrastructure resources (such as virtual machines, networks, storage, etc.) using a high-level configuration language called HashiCorp Configuration Language (HCL) or JSON. Terraform enables you to automate the provisioning, management, and orchestration of cloud infrastructure across various cloud providers, including AWS, Azure, Google Cloud Platform, and others.

## what is CI/CD?
**Continuous Integration (CI)** is a software development practice that automates the integration of code changes from multiple contributors into a shared software project repository. 
Automation and Integration: CI automates the process of integrating code changes frequently into a central repository, facilitating faster merging of code changes and ensuring their correctness through automated testing.

**Continuous deployment and delivery (CD)** is a software development practice that aims to automate the integration, testing, and delivery of code changes. It is a two-part process that involves continuous delivery and continuous deployment.

| Aspect                  | Continuous Delivery (CD)                                                                                                                                                                  | Continuous Deployment (CD)                                                                                                                              |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| Definition              | An extension of continuous integration. It automates the release of validated code to a repository following the automation of builds and unit and integration tests.                       | A logical continuation of the practice of automating build and test steps. If a build passes all previous stages in the pipeline successfully, it is automatically released to production.                  |
| Deployment Process      | Each successful build is automatically deployed to pre-production environments (e.g., testing and acceptance testing environments) for QA and product professionals to verify changes. | As soon as any change to the software has passed all tests, it is automatically delivered to users, shortening the feedback loop from code change to use in production.                                 |
| Goal                    | Helps teams release software faster, shorten the feedback loop, and automate repetitive tasks.                                                                                             | Shortens the feedback loop from code change to use in production, providing timely insight into how changes perform in the real world without compromising quality.                                         |
| Automation              | Automates the release of validated code following successful builds and tests.                                                                                                             | Automates the release of code to production as soon as all tests have passed, eliminating manual intervention in the deployment process.                                                                              |
| Usage                   | Suitable for scenarios where a manual approval step is needed before deploying changes to production.                                                                                      | Suitable for scenarios where rapid deployment of changes to production is desired and where automated testing ensures the quality of changes before release.                                                   |


***

## Terraform CI/CD: Automating Code Deployment
In the context of Terraform, combining Continuous Integration/Continuous Deployment (CI/CD) practices allows for the automated testing, building, and deployment of Terraform codes. 
**CI/CD for Terraform Code:** Integrating CI/CD practices with Terraform code involves automating the process of uploading modules to the official Terraform registry. This automation streamlines the deployment of infrastructure configurations and ensures consistency in code publishing.

## Terraform CI

For Terraform CI, I am utilizing a comprehensive set of tools to ensure code quality, consistency, and security throughout the development process. These tools include:

| Tool             | Purpose                                                                                                                                                                  |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Terraform fmt    | Automatically formats Terraform configuration files (.tf files) to ensure consistent styling and layout.                                                                   |
| Terraform validate | Verifies whether the configuration files are syntactically correct and internally consistent. It checks for errors in attribute names, argument types, and required inputs. |

***

## Terraform CD

For Terraform CD, I'm employing a streamlined approach to deployment using the terraform plan and terraform apply commands. These commands facilitate the automated deployment of infrastructure changes to the target environment. Here's a breakdown:

| Aspect           | Terraform CD                                                                                                                                              |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| terraform plan   | Generates an execution plan outlining the changes Terraform will apply to the infrastructure, including new resources, updated attributes, and deletions. |
| terraform apply  | Executes the planned changes in the Terraform configuration files, applying them to the target environment and automating the deployment process.        |

***

## Pre-requisites

| **Pre-requisite** |
| ---------------- |
| **Jenkins**   |
| **Terraform** |
| **AWS CLI** |  
| **Blue Ocean Plugin** | 
| **AWS Credentials Plugin** | 
| **Pipeline Stage View Plugin** |  
| **Default Plugins** | 

***

## Terraform Code

<details>
<summary>terraform files</summary>
<br>

```shell
resource "aws_instance" "example" {
  ami           = "ami-07c8c1b18ca66bb07"  # Change to your preferred AMI ID
  instance_type = "t3.micro"               # Instance type

  tags = {
    Name = "example-instance"
  }

  # Ensure you have an existing VPC and subnet
  subnet_id                   = "subnet-0134f5449c7d97a39"  # Replace with your subnet ID
  associate_public_ip_address = true               # To associate a public IP address

  # Optional - Add security group to allow SSH and HTTP access
  security_groups = [aws_security_group.example.id]
}

resource "aws_security_group" "example" {
  name        = "example-security-group"
  description = "Allow SSH and HTTP traffic"
  vpc_id      = "vpc-01833dda82803ed0b"  # Replace with your VPC ID

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

/*------------Generate SSH Key--------------*/
resource "tls_private_key" "rsa_4096" {
  algorithm = "RSA"
  rsa_bits  = 4096
 }
/*----------Create PEM Key----------------------*/
resource "aws_key_pair" "key_pair" {
  key_name   = var.key_name
  public_key = tls_private_key.rsa_4096.public_key_openssh
 }
/*----------Download PEM Key-------------------*/
resource "local_file" "private_key" {
  content  = tls_private_key.rsa_4096.private_key_pem
  filename = var.key_name
 }
```
output.tf

```shell
output "instance_public_ip" {
  description = "The public IP of the EC2 instance"
  value       = aws_instance.example.public_ip
}

output "instance_id" {
  description = "The ID of the EC2 instance"
  value       = aws_instance.example.id
}

```

variables.tf

```shell

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

```

provider.tf

```shell
provider "aws" {
  region = "eu-north-1"  # Change to your preferred region
}
```
</details>

***

## Shared Library Code

**terraform.groovy**

<details>
<summary> terraform.groovy </summary>
<br>

```shell
package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath){

    gitcheckout = new checkout()
    tfinit = new init()
    tfvalidate = new validate()
    tffmt = new fmt()
    tfapply = new apply()
    
    
  
    gitcheckout.call(url, creds, branch)
    tfinit.call(rootPath, childPath)
    tfvalidate.call(rootPath, childPath)
    tffmt.call(rootPath, childPath)
    tfapply.call(rootPath, childPath)
    
}

```
</details>

**src files**

<details>
<summary>scr files</summary>
<br>
checkout.grovvy

```shell
package org.terraform

def call(String url, String creds, String branch) {
    stage('Clone') {
        script {
            git branch: "${branch}", credentialsId: "${creds}", url: "${url}"
        }
    }
}
```
init.groovy

```shell
package org.terraform

def call(String rootPath, String childPath) {
    stage('Terraform init') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform init"
        }
    }
}

```

validate.groovy

```shell

package org.terraform

def call(String rootPath, String childPath) {
    stage('Terraform Validate') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform validate"
        }
    }
}

```

fmt.groovy

```shell
package org.terraform

def call(String rootPath, String childPath) {
    stage('Terraform fmt') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform fmt"
        }
    }
}

```

apply.groovy

```shell
package org.terraform

def call(String rootPath, String childPath) {
    stage("Terraform Plan") {
        script {
            sh "cd ${rootPath}/${childPath} && terraform plan"
        }
    }

    stage('Approval For Apply') {
        script {
            // Prompt for approval before applying changes
            input "Do you want to apply Terraform changes?"
        }
    }

    stage('Terraform Apply') {
        script {
            // Run Terraform apply
            sh "cd ${rootPath}/${childPath} && terraform apply -auto-approve"
        }
    }
}

```
</details>

**Shared Library Jenkinsfile**

<details>
<summary>Jenkinsfile</summary>
<br>

```shell
@Library('my-shared-library') _
def terraformCI = new org.template.terraform()
pipeline {
    agent any
    environment {
        AWS_ACCESS_KEY_ID = credentials('vishal-aws-creds')
        AWS_SECRET_ACCESS_KEY = credentials('vishal-aws-creds')
        // TF_CLI_ARGS = '-input=false'
    }
    stages {
        stage('Terraform') {
            steps {
                script {
                    properties([
                        parameters([
                            string(name: 'branch', defaultValue: 'snaatak-Jenkinsfile', description: 'Enter the branch name'),
                            string(name: 'rootPath', defaultValue: 'Jenkinsfile', description: 'Enter the root directory path'),
                            string(name: 'childPath', defaultValue: 'terraform', description: 'Enter the child directory path')
                        ])
                    ])
                node {
                    def url = 'https://github.com/7828143960/shreya_private.git'
                    def creds = 'shreya-github-id'
                    terraformCI.call(url, creds, params.branch, params.rootPath, params.childPath)
                     }
                }
            }
        }
    }
}

```

</details>

***

## Implementation

**Pipeline Job**

![Screenshot 2024-08-21 141545](https://github.com/user-attachments/assets/49e1f46d-c4e2-4d32-bd02-77597b94662b)

**Pipeline With SCM**

![Screenshot 2024-08-21 141605](https://github.com/user-attachments/assets/495371a1-2e10-49a4-b0cb-1e5dc8c2b622)

**Jenkinsfile Path**

![Screenshot 2024-08-21 141620](https://github.com/user-attachments/assets/5b1120e5-fa87-40d7-8734-9e4f1dd144e3)

***

## Output

 **Pipeline Stage View**

 ![Screenshot 2024-08-21 134814](https://github.com/user-attachments/assets/0fe37403-e598-4398-96f1-361a9a515737)


 **Pipeline Console Output**

![Screenshot 2024-08-21 134727](https://github.com/user-attachments/assets/5b26ea29-b8e5-4db8-8e6f-cf9f29fd9655)


 **AWS Console Output**

![Screenshot 2024-08-21 135114](https://github.com/user-attachments/assets/645b60b2-58fd-4a37-9e7e-d2a3230f736a)

***

## Terraform best-practices when working with terraform

| Best Practice                                              | Description                                                                                                                                              |
|------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Input and Output Variables                                | Use input variables to parameterize codes and output variables to expose relevant information for customization based on different use cases.         |
| Documentation                                             | Provide comprehensive documentation, including usage examples, input variable descriptions, and output variable details, to promote module adoption.    |
| Versioning                                                | Manage Terraform using version control (e.g., Git) and version them appropriately with tools like version tags, following semantic versioning.   |
| Formatting                                                | Integrate formatting (e.g., terraform fmt) into the development workflow to enforce code style conventions and detect errors. |
| Continuous Integration/Continuous Deployment (CI/CD)      | Incorporate CI/CD pipelines to automate testing, validation, and deployment, ensuring thorough testing and controlled deployment of code changes.        |

## Conclusion
In conclusion, implementing robust CI/CD checks on Terraform is crucial for guaranteeing the reliability, security, and efficiency of your infrastructure as code (IaC). By following best practices and leveraging automation tools like Jenkins or Terraform Cloud, organizations can streamline development and deployment processes, demonstrably reducing deployment failures  and minimizing downtime.

## Contact Information
| Name            | Email Address                        |
|-----------------|--------------------------------------|
| Shreya Jaiswal | shreya.jaiswal@mygurukulam.co |

## References

| Description                                   | References  
| --------------------------------------------  | -------------------------------------------------|
| Terraform Module | https://spacelift.io/blog/what-are-terraform-modules-and-how-do-they-work |
| What is CI | https://www.atlassian.com/continuous-delivery/continuous-integration |
| What is CD | https://www.atlassian.com/continuous-delivery/principles/continuous-integration-vs-delivery-vs-deployment |
| Terraform Module CI/CD | https://www.reddit.com/r/Terraform/comments/17ldr9i/cicd_for_creating_terraform_modules/ |
