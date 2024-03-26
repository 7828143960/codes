# Documentation of GitHub Authorization setup

***

| **Author** | **Created On** | **Last Updated** | **Documentation Version** |
| ---------- | -------------- | ---------------- | ------------------------- |
| Shreya Jaiswal | 18-01-2024 | 18-01-2024 | V1 |

*** 

# Table Of Contents

+ [Introduction](#Introduction)

+ [Step-by-step setup](#Step-by-step-setup)

+ [Best Practices](#Best-Practices)

+ [Conclusion](#Conclusion)

+ [Contact Information](#Contact-Information)

+ [References](#References)

***

# Introduction

 Authorization plays a pivotal role in safeguarding your resources, ensuring that only authorized individuals can interact with and contribute to your codebase. This guide will seamlessly navigate you through the steps, empowering you to tailor access permissions, bolster security, and maintain the integrity of your development environment. By the end of this walkthrough, you'll be equipped with the knowledge to fine-tune authorization settings, fostering a secure and collaborative space for your coding endeavors.

 ***

 # Step-by-Step Setup
 
**1.Creation of Organization of name "GitHub-Authorization"**

To create a new organization on GitHub, navigate to your GitHub account, click on the "+" icon in the upper right corner, and select "New organization." Follow the prompts to set up the organization's details, teams, and repository permissions.

<img width="948" alt="Screenshot 2024-01-18 144117" src="https://github.com/avengers-p7/Documentation/assets/156057205/f2066cd0-e276-417d-a1e5-979bb0622ce0">

***

**2.Adding Team of name "Batch_P7"**

To create a team in GitHub, navigate to your organization, click on the "Teams" tab, and select "New team." Enter the team name, description, and set member permissions to collaborate seamlessly on projects.

<img width="948" alt="Screenshot 2024-01-18 144538" src="https://github.com/avengers-p7/Documentation/assets/156057205/19d5adc5-a588-4268-a252-8f2f35f2b6f2">

***

**3.Privileges of the members of Batch_P7**

Managing Privileges is very important in GitHub for maintaining the security and boosting the features.

**a.All members of the team (Batch_P7)**

This includes all the members of the team with different roles.

<img width="947" alt="Screenshot 2024-01-18 135242" src="https://github.com/avengers-p7/Documentation/assets/156057205/e802b7e6-aed0-4c61-9aa5-d749245eff0a">

***

**b."shreya-snaatak" as a "Maintainer"**

As a maintainer in a GitHub organization, you hold elevated privileges, allowing you to manage teams, repositories, and member access. This role empowers you to oversee and coordinate the development workflow with administrative control and responsibilities.

<img width="957" alt="Screenshot 2024-01-18 135208" src="https://github.com/avengers-p7/Documentation/assets/156057205/8dee55a8-bdf4-4a61-aedd-521d1dad17f1">

***

**c."aakashtripathi-snaatak" and "Vishalkk1998" as "Members"**

Member Privileges: Control default permissions for organization members, managing their access to repositories and organizational settings.

<img width="953" alt="Screenshot 2024-01-18 135228" src="https://github.com/avengers-p7/Documentation/assets/156057205/f8ebbfa3-7ba6-4d23-b9ed-fa846dae0aed">

***

**4.Members Privileges**

GitHub members have essential privileges, allowing them to contribute to repositories, participate in discussions, and collaborate on projects within an organization, fostering a productive and inclusive development environment.In this,i've assigned "Read" access to members.

<img width="948" alt="Screenshot 2024-01-18 145720" src="https://github.com/avengers-p7/Documentation/assets/156057205/09c7bce5-65d9-41ab-bc43-5f495cf2ac91">

***

**5.Generated "Personal Access Token"**

**A personal access token in GitHub serves as a secure and authenticated way for users to interact with the GitHub API or perform Git operations.It is needed for severeal reasons which includes,authentication,security,API access,avoiding Passwords in Scripts,etc**

To generate a token on GitHub, go to "Settings," click on "Developer settings," select "Personal access tokens," then click "Generate token." Choose scopes and duration, and click "Generate token" to obtain an authentication token for API access.

**a.Creation Process**

Process of creation of a "Personal Access Token"

<img width="942" alt="Screenshot 2024-01-18 140505" src="https://github.com/avengers-p7/Documentation/assets/156057205/238e848f-b530-4935-9bae-c5cf0ad4e987">

***

**b.Created Token**

Successfully created a "Personal Access Token" with 30 days of "Expiration Date"

<img width="957" alt="Screenshot 2024-01-18 140519" src="https://github.com/avengers-p7/Documentation/assets/156057205/ea8525d0-3f4a-44d3-a10f-682b82e2d0b1">

***

**6.Creation of "Repository"**

 Navigate to your GitHub profile, click on the "New" button, provide a repository name and optional settings, then hit "Create repository" to initialize your new project 
 space.

**a.Creation Process**

Followed the steps to create a new repository,whether private or public depends on the usecase

<img width="944" alt="Screenshot 2024-01-18 140909" src="https://github.com/avengers-p7/Documentation/assets/156057205/2cd65e1e-d0ab-421f-8648-cfdd72b3c194">

***

**b.Created Repository of name "Repo_01"**

Successfully created a "Public Repository"

<img width="959" alt="image" src="https://github.com/avengers-p7/Documentation/assets/156057205/1a440661-cb19-4f28-af5e-0ee80222ad24">

***

**7.Added Collaborators and Teams with different roles**

Adding collaborators on GitHub involves inviting users to contribute to a repository, specifying their access levels. Additionally, creating teams allows you to group collaborators for streamlined access management and collective permissions.

<img width="952" alt="Screenshot 2024-01-18 142232" src="https://github.com/avengers-p7/Documentation/assets/156057205/b55a5d62-150a-489a-9ae2-8facbb1ae070">

***

# Best Practices
| **Practices** | **Description** |
| ------------- | --------------- |
| **Secure Token Handling** | Keep access tokens and credentials secure. Avoid hardcoding them directly in your code. |
| **Least Privilege Principle** | Grant the minimum necessary permissions (scopes) to your access tokens. |
| **Token Expiry and Refresh** | Implement token expiry and refresh mechanisms for enhanced security. |

***

# Conclusion

The careful consideration of organization-level and repository-level authorization ensures a well-structured and controlled collaborative environment.The implementation of these authorization measures ensures not only the protection of your codebase but also facilitates a well-organized and collaborative development environment. Regular reviews of access permissions and adherence to the principles of least privilege will contribute to the ongoing security and efficiency of your GitHub workflow.

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# Reference

| **Link** | **Description** |
| -------- | --------------- |
| https://docs.deepsource.com/docs/access-control-managing-access-to-your-teams-repositories | Link for Access setup |








 


