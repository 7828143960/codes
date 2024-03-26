# Documentation of AMI Introduction

| **Author** | **Created On** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **24-01-2024** | **29-01-2024** | **v1** |

***

# Table of Contents

+ [Introduction](#introduction)
+ [What is AMI?](#what-is-AMI)
+ [Why AMI](#why-AMI)
+ [Types of AMI](#types-of-AMI)
+ [EBS-Backed AMI vs Instance Store-Backed AMI](#EBS-Backed-AMI-vs-Instance-Store-Backed-AMI)
+ [Advantages of AMI](#Advantages-of-ami)
+ [Limitations of AMI](#limitations-of-AMI)
+ [Best Practices](#Best-Practices)
+ [Different Tools for AMI Management](#Different-Tools-for-AMI-Management)
+ [Conclusion](#conclusion)
+ [Contact Information](#contact-information)
+ [Reference](#reference)

***

# Introduction

![image](https://github.com/avengers-p7/Documentation/assets/156057205/8d2a875e-1999-472c-8636-ece4884f419d)


This documentation serves as a comprehensive guide, offering insights into the creation, maintenance, security considerations, and compliance governance surrounding AMIs within the AWS ecosystem.Whether embarking on new cloud projects or optimizing existing infrastructures, the insights provided herein will serve as a valuable resource for making informed decisions and ensuring the robustness of AWS deployments.

***

# What is AMI?

An Amazon Machine Image (AMI) is a supported and maintained image provided by AWS that provides the information required to launch an instance. You must specify an AMI when you launch an instance. You can launch multiple instances from a single AMI when you require multiple instances with the same configuration. You can use different AMIs to launch instances when you require instances with different configurations.

***

# Why AMI?

| **Reason** | **Description** |
| ---------- | --------------- |
| **Rapid Deployment** | AMIs enable quick and consistent instance provisioning, reducing deployment time. |
| **Version Control** | Facilitates versioning of configurations, supporting easy rollback and updates. |
| **Scalability** | Supports auto-scaling and dynamic provisioning for varying workloads. |
| **Pay-as-You-Go Model** | AMIs align with AWS's pay-as-you-go pricing model, optimizing costs. |

***

# Types of AMI

| **Types** | **Description** |
| --------- | --------------- |
| **EBS-Backed AMI** | An AMI with the root volume created using an Amazon Elastic Block Store (Amazon EBS) volume. This ensures that the data is persisted, even after the EC2 instance is shut down.|
| **Instance-store (S3-backed) AMI** | An AMI that uses an Instance Store volume created from a template stored in Amazon S3. When using an Instance Stored-backed AMI, the data in the EC2 instance is not persisted after shut down since the instance gets terminated.|

![image](https://github.com/avengers-p7/Documentation/assets/156057205/950075ec-1096-4a5d-9e44-60a46c2546b5)


***

# EBS-Backed AMI vs Instance Store-Backed AMI

| **Characteristic** | **Amazon EBS-backed AMI** | **Amazon instance store-backed AMI** |
| ------------------ | ------------------------- | ------------------------------------ |
| **Boot time for an instance** | Usually less than 1 minute	| Usually less than 5 minutes |
| **Root device volume** | EBS volume | Instance store volume |
| **Data persistence** | By default, the root volume is deleted when the instance terminates. But the data on any other EBS volume persists. | Data on any instance store volumes is deleted when the instance terminates |
| **Charges** | Charged for instance usage, EBS volume usage, and storing the AMI as an EBS snapshot.| Charged for instance usage and storing your AMI in Amazon S3 |
| **Stopped state** | Can be in a stopped state. Even when the instance is stopped and not running, the root volume is persisted in Amazon EBS.| Cannot be in a stopped state, instances are running or terminated.|

***

# Advantages of AMI

| **Advantages** | **Description** |
| -------------- | --------------- |
| **Fast and Repeatable Deployments** | Enables rapid and consistent deployments, reducing the time and effort needed to set up new instances. |
| **Scaling and Elasticity** | AMIs play a crucial role in auto-scaling and load balancing strategies. |
| **Backup and Disaster Recovery** | AMIs serve as a basis for creating backups and implementing disaster recovery strategies. |
| **Versioning and Rollback** | AMIs can be versioned, allowing for easy rollbacks to previous configurations. |

***

# Limitations Of AMI

| **Limitation** | **Description** |
| -------------- | --------------- |
| **Region Specific** | AMIs are region-specific, and you need to create or copy them for use in different AWS regions. |
| **Instance Type Compatibility** | AMIs are often specific to certain instance types, and you need to ensure compatibility when launching instances. |
| **Time Taken** | There is a limit on the size of an AMI. If your AMI is large, it might take longer to launch instances from it. |
| **Network Configuration** | AMIs may have specific network configurations tied to the region in which they were created.When deploying an AMI in a different region, you may need to adjust network settings.|

***

# Best Practices

- Include only necessary software and configurations in the AMI to keep it lightweight.
- Restrict access to AMIs based on the principle of least privilege.
- Implement tagging for AMIs to categorize and manage them efficiently.
- Regularly review and clean up unused or outdated AMIs to reduce security risks.

***

# Different Tools for AMI Management

| **Tools** | **Description** |
| --------- | --------------- | 
| **AWS Management Console** | The AWS Management Console provides a web-based interface for creating and managing AMIs. You can use the EC2 service to launch an instance, customize it, and then create an AMI from it. |
| **Packer** | Packer is an open-source tool for creating machine images from a single source configuration. It supports multiple builders, including the Amazon EC2 builder, allowing you to automate the creation of AMIs. |
| **AWS Command Line Interface (CLI)** | The AWS CLI allows you to perform AWS operations from the command line. You can use commands like create-image to create an AMI based on a running or stopped instance. |
| **Terraform** | Terraform is an infrastructure as code tool that can be used to define and provision infrastructure. You can use Terraform to create and manage EC2 instances and then create AMIs from those instances. |
| **Ansible** | Ansible is an open-source automation tool. You can use Ansible playbooks to configure instances and then create AMIs. The ec2_ami module can be helpful in this context. |
| **AWS SDKs** | Software Development Kits (SDKs) are available for various programming languages (e.g., Python, Java, Ruby). You can use SDKs to programmatically interact with AWS services, including creating AMIs. | 


***

# Conclusion

In conclusion, Amazon Machine Images (AMIs) play a pivotal role in the realm of cloud computing, offering unparalleled advantages in terms of efficiency, scalability, and resource optimization within the AWS ecosystem. This documentation has provided a comprehensive exploration of AMIs, covering their definition, components, and purpose. 

And at last,I choose **"Packer"** as a tool for **AMI Creation**.Packer is often preferred for creating Amazon Machine Images (AMIs) due to its multi-platform support, infrastructure as code approach, and template-based configuration. Its extensibility, supporting various builders and provisioners, allows seamless integration into existing workflows.  The strong community support and integration with other HashiCorp tools further contribute to its popularity for efficient and scalable AMI creation.

> [!NOTE]
> POC for AMI Creation is prepared in different doc,if you want to see the AMI Creation Process use this link [AMI POC](https://github.com/avengers-p7/Documentation/blob/main/Application_CI/Design/02-%20Generic%20CI%20operation/AMI/AMI%20via%20Packer.md) 

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# Reference

| **Source** | **Description** |
| ---------- | --------------- |
| https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html | Documentation Link |                 
| https://www.techtarget.com/searchaws/definition/Amazon-Machine-Image-AMI | Concept of AMI |
| https://www.scaler.com/topics/aws/amazon-machine-image/ | AMI reference Link |
| https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ComponentsAMIs.html | Link for AMI Types |
| https://awstip.com/create-an-ami-with-ansible-9ad446aa44e1 | Link for AMI Creation using Ansible |


