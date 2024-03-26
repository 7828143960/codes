# Documentation of Slack Notification

***

| **Author** | **Created On** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **23-01-2024** | **24-01-2024** | **v1** |

***

# Table Of Contents

1. [Introduction](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#introduction)
2. [Pre-requisites](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#pre-requisites)
3. [Architecture](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#Architecture)
4. [Slack Notification Setup](https://github.com/avengersp7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#slacknotificationsetup)
5. [Conclusion](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#conclusion)
6. [Contact Information](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#contactinformation)
7. [Reference](https://github.com/avengers-p7/Documentation/edit/main/Application_CI/Design/Slack_Notification.md#reference)

***

# Introduction

This document provides a detailed guide on implementing Slack notifications, including an introduction to Slack incoming webhooks, step-by-step instructions, and a Proof of Concept (POC) to demonstrate the integration.**Slack notification** integration is a valuable feature that allows you to keep your team informed and engaged by sending notifications directly to your Slack channels. This can be achieved using Slack's incoming webhooks, a simple and effective way to push messages from external sources into Slack.

***

# Pre-requisites

**1.Jenkins Installed**

**2.Slack Notification Plugin**

***

# Architecture

![image](https://github.com/avengers-p7/Documentation/assets/156057205/5bdcfc50-1e15-4829-921f-1df5964cdfb7)

***

# Slack Notification Setup

Setting up Slack notifications in Jenkins involves integrating Jenkins with Slack using a Jenkins plugin and configuring the necessary settings in both Jenkins and Slack.

**Step-1 Workspace Creation**

Different projects or teams may have different Jenkins jobs with unique notification requirements. Using separate workspaces in Slack allows for the isolation and organization of notifications related to specific projects or teams.

Go to the Slack website.Click on "Get started for free" or "Create a new workspace."Follow the prompts to set up your Slack workspace, providing details such as your email address and password.

<img width="865" alt="image" src="https://github.com/avengers-p7/Documentation/assets/156057205/5abb2e1b-f5d1-47a0-ba8b-26a6c7bf7287">

***

**Step-2 Channel Creation**

Creating a dedicated channel for Jenkins notifications helps isolate and organize build-related messages.

Log in to your Slack workspace.In the left sidebar, click on the "+" button next to "Channels."Choose "Create a channel."

<img width="846" alt="Screenshot 2024-01-23 181612" src="https://github.com/avengers-p7/Documentation/assets/156057205/ce1ccb76-4a59-4a4c-89d9-4a0d418238d7">


***

**Step-3 Jenkins CI Setup**

This step includes the steps for "Password" or "Secret Text" creation,which is an important part for Slack Notification.With this,will get the "Team Subdomain" & "Password" which is needed for jenkins slack notification configuration.

<img width="945" alt="Screenshot 2024-01-23 181736" src="https://github.com/avengers-p7/Documentation/assets/156057205/ac515c7a-4501-48b6-b84d-2724d1084572">

***

<img width="940" alt="Screenshot 2024-01-23 181811" src="https://github.com/avengers-p7/Documentation/assets/156057205/a531d89d-8ee9-4c2c-b230-b3f66726201e">

***

<img width="948" alt="Screenshot 2024-01-23 181853" src="https://github.com/avengers-p7/Documentation/assets/156057205/0b59d774-0387-4373-86ab-fdd113031e37">

***

<img width="941" alt="Screenshot 2024-01-23 181923" src="https://github.com/avengers-p7/Documentation/assets/156057205/5041db4a-2457-4f7a-8608-1779d081980e">

***

**Step-4 Jenkins Configuration** 

Setting up Jenkins Global Configuration for Slack notifications allows you to centralize and manage Slack-related settings at a global level. This global configuration provides a convenient way to store and share common information, such as Slack team domain and integration tokens, across multiple Jenkins jobs.

Go to "Manage Jenkins" > "Configure System."Scroll down to the "Slack" section.In the "Team Domain" field, enter the Slack team domain.In the "Integration Token Credential" section, click on "Add" to add your Slack integration token as a Jenkins credential.Enter the necessary information, such as the token itself and an ID to identify the credential.In the "Test Connection" section, enter a Slack channel name and click on "Test Connection" to verify that Jenkins can communicate with Slack.

<img width="862" alt="Screenshot 2024-01-23 181534" src="https://github.com/avengers-p7/Documentation/assets/156057205/ff96bc8e-440e-4b72-946a-9f3757499eea">

***

<img width="940" alt="Screenshot 2024-01-23 181213" src="https://github.com/avengers-p7/Documentation/assets/156057205/b681c571-4705-4524-aebb-aac653379158">

***

**Step-5 Creation of Freestyle Job** 

Creating a freestyle job in Jenkins for Slack notifications allows you to automate the process of notifying your team about build statuses, failures, or other events directly within your Slack workspace. 

Click on "New Item" on the Jenkins dashboard.Enter a name for your project "Doc-Job".

<img width="945" alt="image" src="https://github.com/avengers-p7/Documentation/assets/156057205/40d8c31d-6bc5-46c1-b805-194e531c0392">

***

**Step-6 Job Configuration** 

Configure any other general settings based on your project requirements.In the "Build" section, click on "Add build step" and select "Execute shell".Scroll down to the "Post-build Actions" section.Click on "Add post-build action" and select "Slack Notifications".

<img width="952" alt="Screenshot 2024-01-23 182859" src="https://github.com/avengers-p7/Documentation/assets/156057205/6f1b76f6-aa23-4ae6-853a-7c796cd4acc1">

***

<img width="944" alt="Screenshot 2024-01-23 182929" src="https://github.com/avengers-p7/Documentation/assets/156057205/457942d7-0189-440d-bd1f-5dd70d756f63">

***

<img width="951" alt="Screenshot 2024-01-23 183019" src="https://github.com/avengers-p7/Documentation/assets/156057205/97642ba5-0bec-4752-9363-741f54e47995">

***

**Step-7 Build Status** 

After configuring,build the job and check the console output for the job's status.

<img width="955" alt="Screenshot 2024-01-23 182619" src="https://github.com/avengers-p7/Documentation/assets/156057205/79713aee-4436-4ae0-86f7-80476a2a64f1">

***

**Step-8 Slack Notification** 

Now,go to slack and check for the notification.

<img width="654" alt="Screenshot 2024-01-23 182532" src="https://github.com/avengers-p7/Documentation/assets/156057205/d81b0d91-fd0d-4cf9-b977-14eed33a7ced">


***

# Conclusion

In conclusion, this documentation has provided a comprehensive guide on integrating Jenkins with Slack for notifications through the creation of a freestyle job. By following the outlined steps, users can seamlessly connect Jenkins and Slack, enabling automated notifications about build statuses, failures, and other relevant events directly within the Slack workspace.

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# Reference

| **Source** | **Description** |
| ---------- | --------------- |
| https://plugins.jenkins.io/slack | Link for Jenkins Configuration |
| https://medium.com/appgambit/integrating-jenkins-with-slack-notifications-4f14d1ce9c7a |  Integration Jenkins with slack |
| https://slack.com/apps/A0F7VRFKN-jenkins-ci | Link for Jenkins CI |


