# Cloud Infra Design QA

<img width="300" length="100" alt="QA Infra" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/a5bd713e-2bea-4257-baed-27798aef3b3b">

***

| **Author** | **Created On** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **27 Feb 2024** | **03 March 2024** | **v1** |

***

# Table of Contents

1. [Introduction](#introduction)
2. [Pre-requisites](#pre-requisites)
3. [Infrastructure Diagram](#infrastructure-diagram)
4. [Description](#description)
5. [Best Practices](#Best-Practices)
6. [Conclusion](#Conclusion)
7. [Contact Information](#contact-information)
8. [References](#References)

***

# Introduction

In the modern software development landscape, microservices architecture has become increasingly popular due to its scalability, flexibility, and resilience. Quality Assurance (QA) plays a crucial role in ensuring that microservices function as intended and meet the expectations of users. Cloud infrastructure further enhances the agility and reliability of microservices by providing scalable and elastic resources.

***

# Pre-requisites

| Tool                  | Description                                  |
|-----------------------|----------------------------------------------|
| **AWS Account**       | Required for provisioning AWS resources.     |

***

# Infrastructure Diagram

![Ideal-QA-Infra - Page 1 (2)](https://github.com/CodeOps-Hub/Documentation/assets/156057205/eef74ae9-bba3-4f93-b0e2-be2172ef3212)

*** 

# Description

| Component                           | Details                                                                                           |
| ----------------------------------- | ------------------------------------------------------------------------------------------------- |
| **User Access**                     | Users connect to the infrastructure through the internet.                                          |
| **Route53**                         | DNS service for routing traffic to various components.                                               |
| **Public Subnets**                  | Public subnets host components requiring direct internet access. Bastion Host facilitates secure access.|
| **Private Subnets**                 | Frontend app, Attendance, Employee, and Salary APIs hosted in separate private subnets. Databases (PostgreSQL, Scylla, Redis) in another private subnet. |
| **Security Groups**                 | - To provide security on instance level |
| **NACLs (Network ACLs)**           | Control inbound and outbound traffic at subnet level for specific private subnets.                  |
| **Internet Gateway & NAT Gateway**  | - Internet Gateway (Igw) for VPC internet access.<br>- NAT Gateway for outbound traffic from private subnets to the internet.|
| **Route Tables**                    | - Public-RT for public subnet internet access.<br>- Private-RT for communication within the VPC.    |
| **ALB (Application Load Balancer)** | Configured to distribute frontend traffic across multiple targets for high availability.            |
| **Auto Scaling Group (ASG)**        | Dynamically adjusts frontend and API service instances based on demand for scalability.              |
| **Region and Availability Zone**    | Deployed in US East region, specifically N.Virginia (us-east-1). Utilizes availability zones (us-east-1a) for redundancy and fault tolerance. |
| **VPC (Virtual Private Cloud)**     | Created for Development environments, organizes components for scalability and availability.        |
            
***

## Security Groups and Inbound Rules

### Frontend Security Group

| Layer    | Security Group Name | Inbound Rule Port | Inbound Rule Source |
|----------|---------------------|-------------------|---------------------|
| Frontend | Frontend-lb-sg      | 80                | 0.0.0.0/0           | 
| Frontend | Frontend-sg         | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28) |      
| Frontend | Frontend-sg         | 3000              | Frontend-lb-sg (sg-04d283934a64707a) | 

***

### Backend Security Group

| Layer    | Security Group Name | Inbound Rule Port | Inbound Rule Source |
|----------|---------------------|-------------------|---------------------|
| Backend  | Attendance-sg       | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28)     |
| Backend  | Attendance-sg       | 8080              | Frontend-lb-sg (sg-04d283934a64707a)                               | 
| Backend  | Salary-sg           | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28)     |
| Backend  | Salary-sg           | 8080              | Frontend-lb-sg (sg-04d283934a64707a)                               |
| Backend  | Employee-sg         | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28)     |
| Backend  | Employee-sg         | 8080              | Frontend-lb-sg (sg-04d283934a64707a)                               |

***

### Database Security Group

| Layer    | Security Group Name | Inbound Rule Port | Inbound Rule Source |
|----------|---------------------|-------------------|---------------------|
| Database | Postgresql-sg       | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28) |      
| Database | Postgresql-sg       | 5432              | Backend-sg (sg-0a4ecb0570e13e3) |       
| Database | Redis-sg            | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28) |
| Database | Redis-sg            | 6379              | Backend-sg (sg-0a4ecb0570e13e3)     |
| Database | Scylla-sg           | 22                | Openvpn-sg (sg-0ced15d988acdb94), Management-vpc (20.0.0.0/28) | 
| Database | Scylla-sg           | 9042              | Backend-sg (sg-0a4ecb0570e13e3)  |

***

### Openvpn Security Group

| Layer    | Security Group Name | Inbound Rule Port | Inbound Rule Source |
|----------|---------------------|-------------------|---------------------|
| OpenVPN  | Openvpn-sg          | 22                | Management-vpc (20.0.0.0/28) |
| OpenVPN  | Openvpn-sg          | 1194              | 0.0.0.0/0 |

***

## NACL Rules

### Frontend NACL Inbound Rules

| Rule number | Type      | Protocol | Port range | Source       | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | SSH       | TCP      | 22         | Management-vpc (20.0.0.0/28), QA-Public-Subnet-1 (10.0.1.0/27)      | Allow |
| 110         | Custom TCP| TCP      | 3000       | QA-Public-Subnet-1 (10.0.1.0/27), QA-Public-Subnet-2 (10.0.1.64/27) | Allow |
| *           | All traffic | All    | All        | 0.0.0.0/0    | Deny       |

### Frontend NACL Outbound Rules

| Rule number | Type      | Protocol | Port range | Destination  | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | Custom TCP| TCP      | 1024-65535 | Management-vpc (20.0.0.0/28)      | Allow      |
| 110         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-1 (10.0.1.0/27)  | Allow      |
| 120         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-2 (10.0.1.64/27) | Allow      |
| *           | All traffic | All    | All        | 0.0.0.0/0    | Deny       |

### Backend NACL Inbound Rules

| Rule number | Type      | Protocol | Port range | Source       | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | SSH       | TCP      | 22         | Management-vpc (20.0.0.0/28), QA-Public-Subnet-1 (10.0.1.0/27)      | Allow      |
| 110         | Custom TCP| TCP      | 8080       | QA-Public-Subnet-1 (10.0.1.0/27), QA-Public-Subnet-2 (10.0.1.64/27) | Allow      |
| 120         | Custom TCP| TCP      | 1024-65535 | QA-DB-Pvt-Subnet (10.0.1.48/27)   | Allow      |
| 130         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-2 (10.0.1.64/27) | Allow      |
| 140         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-1 (10.0.1.0/27)  | Allow      |
| *           | All traffic | All    | All        | 0.0.0.0/0    | Deny       |

### Backend NACL Outbound Rules

| Rule number | Type      | Protocol | Port range | Source       | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | Custom TCP| TCP      | 1024-65535 | Management-vpc (20.0.0.0/28)      | Allow      |
| 110         | Custom TCP| TCP      | 1024-65535 | QA-DB-Pvt-Subnet (10.0.1.48/27)   | Allow      |
| 120         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-2 (10.0.1.64/27) | Allow      |
| 130         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-1 (10.0.1.0/27)  | Allow      |
| *           | All traffic | All    | All        | 0.0.0.0/0    | Deny       |

### Database NACL Inbound Rules

| Rule number | Type      | Protocol | Port range | Source       | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | SSH                     | TCP | 22         | Management-vpc (20.0.0.0/28), QA-Public-Subnet-1 (10.0.1.0/27)  | Allow      |
| 110         | Custom TCP(Redis)       | TCP | 6379       | QA-Backend-Pvt-Subnet (10.0.1.32/27)                            | Allow      |
| 120         | Custom TCP(Scylla)      | TCP | 9042       | QA-Backend-Pvt-Subnet (10.0.1.32/27)                            | Allow      |
| 130         | Custom TCP (PostgreSQL) | TCP | 5432       | QA-Backend-Pvt-Subnet (10.0.1.32/27)                            | Allow      |
| *           | All traffic             | All | All        | 0.0.0.0/0                                                       | Deny       |

### Database NACL Outbound Rules

| Rule number | Type      | Protocol | Port range | Source       | Allow/Deny |
|-------------|-----------|----------|------------|--------------|------------|
| 100         | Custom TCP| TCP      | 1024-65535 | QA-Backend-Pvt-Subnet (10.0.1.32/27) | Allow      |
| 110         | Custom TCP| TCP      | 1024-65535 | Management-vpc (20.0.0.0/28)         | Allow      |
| 120         | Custom TCP| TCP      | 1024-65535 | QA-Public-Subnet-1 (10.0.1.0/27)     | Allow      |
| *           | All traffic | All    | All        | 0.0.0.0/0                            | Deny       |

***

# Best Practices

| Practice | Description |
|------------- | ----------- |
| **Version Control** | Utilize version control systems like Git to manage documentation, enabling collaboration, tracking changes, and ensuring versioning.|
| **Clear Naming Conventions** | Adopt clear and consistent naming conventions for documents, folders, and files to facilitate easy navigation and understanding.|
| **Operational Excellence** | Involves monitoring systems and continuously improving the cloud architecture and internal processes. Implement incremental changes, document procedures, and focus on troubleshooting.|
| **Cost Optimization** | A major concern for businesses moving to the cloud. Incorporate cloud cost optimization strategies to reduce overall spending. Analyze expenses, drop components with high operational costs, and use automation for cost-efficient alternatives.|
| **Performance Efficiency** | Design cloud architecture with the right service provider to improve performance. Focus on structuring resources, selecting appropriate sizes/types, and utilizing automation and AI for optimization.|
| **Reliability** | Ensure workloads perform as expected and recover quickly from outages. Build automated recovery processes, use autoscaling to avoid under/overprovisioning, and establish change management processes for better resource monitoring and demand management.|
| **Security** | Maintain data integrity, detect security breaches, and manage user permissions. Establish secure procedures and continuously monitor for unauthorized access through automated notification.|
| **Sustainability** | Take proactive measures to minimize environmental impact. Maximize resource utilization, decrease costs, and establish responsible models for sustainability. Harden security for all applications and instances while continuously monitoring for breaches.|

***

# Conclusion

Documentation of QA processes for microservices on cloud infrastructure is vital for ensuring the reliability, scalability, and performance of the system. By following best practices outlined in this documentation, teams can streamline their QA processes, improve collaboration, and deliver high-quality microservices to users. Regular updates and revisions to the documentation will ensure that it remains relevant and useful as the system evolves over time.

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# References 

| **Source** | **Description** |
| --------- | --------------- |
| [Link](https://github.com/OT-MICROSERVICES/documentation-template/wiki/Application-Template) | Template Reference Link |
| [Link](https://github.com/avengers-p7/Documentation/blob/main/Application_CI/Design/09-%20Cloud%20Infra%20Design/Cloud-Infra-Design-30K-Feet.md) | Cloud Infra Diagram 30k Feet  |
| [Link](https://www.nops.io/blog/cloud-architecture-best-practices/) | Cloud Infra Best Practices |


