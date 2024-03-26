# Manual Setup of OpenVPN

<img width="300" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/962c6922-0507-4f62-a686-509622c886ab">

***

| **Author** | **Created On** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **19 Feb 2024** | **19 Feb 2024** | **v1** |

***

# Table of Contents
+ [Introduction](#Introduction)
+ [Pre-requisites](#Pre-requisites)
+ [Flow Diagram](#Flow-Diagram)
+ [OpenVPN Setup](#OpenVPN-Setup)
+ [Result](#Result)
+ [Conclusion](#Conclusion)
+ [Contact Information](#Contact-Information)
+ [References](#References)

***

> [!Note]
> Documentation for introduction of OpenVPN is created in separate doc, click this [Link](https://github.com/CodeOps-Hub/Documentation/blob/main/Infra/Manual/OpenVpn/Open-VPN-Doc.md)

***

# Introduction

This Proof of Concept (PoC) documentation aims to provide a comprehensive guide for setting up and testing OpenVPN on an Ubuntu Server environment. OpenVPN is a widely-used open-source VPN (Virtual Private Network) solution known for its robust security features and flexibility. 

***

# Pre-requisites

| **Pre-requisite** | **Description** |
| ----------------- | --------------- |
| **Root Access or Administrative Privileges** | You'll need administrative access to the server to install packages, modify system settings, and configure firewall rules. |
| **Network Connectivity** | Verify that the server has stable internet connectivity with sufficient bandwidth for VPN traffic. Consider factors like upstream and downstream speeds, latency, and any bandwidth limitations. |
| **Firewall Configuration** | Ensure that firewall rules allow traffic on the necessary ports for OpenVPN. By default, OpenVPN uses UDP port 1194. |

***

# Flow Diagram

<img width="551" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/0192d580-9696-4407-8a12-09f30d35fd59">

***

<img width="800" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/bdc48239-0f9a-40f5-b24c-bb95e6659514">

| **Resource** | **Description** |
| ------------ | --------------- |
| **AWS Cloud** | Choosed AWS Cloud for setup |
| **Region** | Selected Mumbai Region(ap-south-1) |
| **VPC** | Selected Dev VPC of CIDR `10.0.1.0/25` |
| **Public Subnet** | Selected Public Subnet with CIDR `10.0.1.0/28` |
| **Route Table** | Created `Route Table` and associated it with `Public Subnet` |
| **Internet Gateway** | Created `Internet Gateway` and attached with `Route Table` |
| **Security Group** | Created `Security Group` with port **TCP(22),UDP(1194)** |
| **EC2 Instance** | Created `EC2 Instance` of name `OpenVPN-Server` |

***

# OpenVPN Setup

### Launching EC2 Instance

**Step-1 First, launch an `EC2 Instance`, access the EC2 dashboard, Click on `Launch Instance`.**

![Screenshot 2024-02-19 133820](https://github.com/CodeOps-Hub/Documentation/assets/156057205/45575af6-697e-4fd2-9b5d-cd5aa43338ae)

***

**Step-2 Choose an appropriate name for the instance and choose `Ubuntu AMI`**

![Screenshot 2024-02-19 133937](https://github.com/CodeOps-Hub/Documentation/assets/156057205/6b269d7d-2aec-4a8a-8b91-90f2d9ff693a)

***

**Step-3 Select the instance type as `t2-micro`. Generate a key pair with an appropriate name.** 

![Screenshot 2024-02-19 134047](https://github.com/CodeOps-Hub/Documentation/assets/156057205/fcf83314-e725-4e8d-a43d-297856398546)

***

**Step-4 In the network settings, pick a VPC; in this case, choose the `Management-VPC`. Choose a subnet , preferably a `public subnet`. Enable `auto-assign IP`.Navigate to the security group settings. Ensure that `port 1194` is open if using UDP (default for OpenVPN).** 

<img width="900" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/3345eede-e95b-464f-af07-2d4e8b1c748c">

***

**Step-5 Leave other settings as default for now. Finally click on `launch instance`. That's it, we have successfully launched the `instance`.**

![Screenshot 2024-02-19 134504](https://github.com/CodeOps-Hub/Documentation/assets/156057205/c87c30c3-40dc-4ec9-b0e0-ea46568b7389)

***

## Configuring OpenVPN

**Step-1 Let's set up our OpenVPN server. Start by updating your system packages using the below commands.**

```shell
sudo apt update
sudo apt upgrade -y
```

<img width="937" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/938be885-f051-4735-8006-080ab6b90bcf">

***

**Step-2  We'll use this script to install and configure the OpenVPN server. To get the script, run the following command mentioned below.**

```shell
wget https://raw.githubusercontent.com/angristan/openvpn-install/master/openvpn-install.sh
```
<img width="770" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/295830f7-f712-4833-9bd0-aa644d256242">

***

**Step-3 Grant the script necessary (execution) permission by running the below command.**

```shell
sudo chmod +x openvpn-install.sh
```

<img width="573" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/819dc2ee-f265-478d-8d71-9d9d2c341c5d">

***

**Step-4 Execute the script with the following command.**

```shell
sudo ./openvpn-install.sh
```
***

**Step-5 As soon as the script runs, it will pose below given questions.** 

**The first question verifies your Linux machine's IP address. Press Enter if it's correct; otherwise, press any other key. it will automatically detect your IP address; press enter to continue.**

<img width="656" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/4c7175cf-3a5f-4f52-b123-815a1919ffbc">

***

**The script will then inquire about enabling IPv6 connectivity. You can stick with the default option and press enter.**

<img width="521" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f975be29-65cd-45ce-9130-5f7d9a057243">

***

**You'll be prompted to choose a port. The default is 1194, but you can either stick with it or specify a different one. Note that you'll need to open this chosen port in your firewall, a step we'll cover later.**

<img width="671" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/ef134517-959f-4bd8-bae1-ddd379c4dbce">

***

**Another question will ask whether to use TCP or UDP. Simply press Enter to select UDP.**

<img width="637" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/57075f94-5b80-41d5-9e0b-06da1ba5a839">

***

**Following that, the script will inquire about your DNS resolver preference. I suggest opting for Google, which is option 9.**

<img width="443" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/f0ed0e51-c7b9-4b60-8e74-01566a01df02">

***

**Then, it will question you about using compression. Stick with the default choice, which is "no," and press Enter.**

![Screenshot 2024-02-19 154939](https://github.com/CodeOps-Hub/Documentation/assets/156057205/0d03e0ac-e416-40b6-b1d8-7bd08a9ab438)

***

**Finally, there's a query about customizing encryption settings. Once again, go with the default option, which is "no," and press Enter.**

![Screenshot 2024-02-19 155004](https://github.com/CodeOps-Hub/Documentation/assets/156057205/d2b5b367-4435-4357-82d1-9f139e952390)

***

**Press enter at the next prompt to start the process of applying the settings and installing the required certificates.**

<img width="764" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/13a4caba-520f-4b13-8eba-7b55de91a2e0">

***

**Provide the name of the client that will establish a connection with our VPN server. Subsequently, you will be prompted to indicate your preference regarding the use of a password for the client. opt for option 2 and input your desired password.**

<img width="650" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/8c3f5d01-9de1-4b44-8dee-227fd7eefc45">

<img width="658" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/6c58361f-ee0f-48c2-8e07-d965c58d9dd2">

***

**Enter the password. The configuration process is complete. OpenVPN now generates a file for each user you create.**

<img width="634" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/88b3794c-0c65-41ec-ae80-ac4af9141ff0">

***

**To add additional clients, revoke an existing client, or uninstall OpenVPN, rerun the script, and it will provide you with the respective options.**

<img width="469" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/ef846c5e-b24f-462d-944f-2bd9ba1a8015">

***

**This file, containing all the necessary configuration details, has been generated for connecting to the VPN server.**

![Screenshot 2024-02-19 141959](https://github.com/CodeOps-Hub/Documentation/assets/156057205/1dc0b636-0a14-4440-8ba2-514f13b839fa)

***

 ## We can connect to the VPN server from a Linux client using below command.
 
 ```shell
sudo openvpn –config path_to_client_configuration_file
```

![Screenshot 2024-02-19 145226](https://github.com/CodeOps-Hub/Documentation/assets/156057205/6ff89a36-7a5a-4deb-8f8e-a00155b1677e)

***

# Open-VPN Tool Configuration

Uploaded the `open-vpn.ovpn`file that we created above and tested the connection

<img width="293" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/2276496f-375b-4765-9ed5-e0d4565fa60e">

***

# Result

```shell
route -n
curl ifconfig.me
```

<img width="750" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/98c300f4-d695-4ccf-8e2c-62bcce6842b9">

<img width="750" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/532297f5-3102-4bd5-868e-dd4db54da1a0">

**Able to connect to the Private Subnet Instance locally**

<img width="750" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/552fa9fe-dc9f-4d37-aa5f-b21806eabf19">

***

# Conclusion

In conclusion, this Proof of Concept (PoC) documentation has provided a detailed overview of deploying OpenVPN on Ubuntu Server for establishing secure remote access. Through the installation and configuration steps outlined in this documentation, users can successfully set up a functional OpenVPN server and initiate secure VPN connections.

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# References

| **Source** | **Description** |
| ---------- | --------------- |
| [Link](https://www.youtube.com/watch?v=jmTtvxgspbk) | Video Reference for OpenVPN Setup |
| [Link](https://surfshark.com/blog/what-is-openvpn) | Reference Link for understanding the concept of OpenVPN |



