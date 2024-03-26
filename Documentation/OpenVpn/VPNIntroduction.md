# Documentation of VPN(Virtual Private Network)

<img width="300" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/ab10b871-4d78-4042-876c-bf5ee725a469">

***

| **Author** | **Created On** | **Last Updated** | **Document Version** |
| ---------- | -------------- | ---------------- | -------------------- |
| **Shreya Jaiswal** | **18 Feb 2024** | **18 Feb 2024** | **v1** |

***

# Table of Contents
+ [Introduction](#Introduction)
+ [What is VPN?](#What-is-VPN)
+ [Why VPN](#Why-VPN)
+ [Types of VPN](#Types-of-VPN)
+ [How VPN Works](#How-VPN-Works)
+ [Advantages of VPN](#Advantages-of-VPN)
+ [Disadvantages of VPN](#Disadvantages-of-VPN)
+ [Best Practices](#Best-Practices)
+ [Conclusion](#Conclusion)
+ [Contact Information](#Contact-Information)
+ [References](#References)

***

# Introduction

 In this guide, we'll explore Virtual Private Networks (VPNs), their importance, best practices for using them effectively, and conclude with a summary of their benefits. In today's interconnected digital landscape, ensuring secure and private communication is paramount. VPNs offer a robust solution to safeguard your online activities, protect sensitive data, and maintain anonymity in an increasingly vulnerable cyber environment.

***

# What is VPN?

VPN stands for the Virtual Private Network. A virtual private network (VPN) is a technology that creates a safe and encrypted connection over a less secure network, such as the Internet. A Virtual Private Network is a way to extend a private network using a public network such as the Internet. The name only suggests that it is a “Virtual Private Network”, i.e. user can be part of a local network sitting at a remote location. It makes use of tunnelling protocols to establish a secure connection.

***

# Why VPN

| **Reason** | **Description** |
| ---------- | --------------- |
| **Enhanced Security** | VPNs encrypt data, preventing unauthorized access, and protecting sensitive information such as passwords, financial transactions, and personal communications. |
| **Privacy Protection** | VPNs mask your IP address, making it difficult for websites, advertisers, and third parties to track your online activities and location. | 
| **Secure Remote Access** | VPNs facilitate secure remote access to corporate networks for employees working remotely, ensuring confidentiality and integrity of company data. |
| **Public Wi-Fi Security** | VPNs provide a secure connection on public Wi-Fi networks, protecting users from potential threats such as Wi-Fi spoofing and packet sniffing. |
| **Flexible and Scalable Solutions** | Provides flexible and scalable connectivity solutions that can adapt to changing network requirements and accommodate increased traffic as organizations grow. |       

***

# Types of VPN

| **Types** | **Description** |
| --------- | --------------- |
| **Remote Access VPN** | Remote Access VPN permits a user to connect to a private network and access all its services and resources remotely. The connection between the user and the private network occurs through the Internet and the connection is secure and private. |
| **Site to Site VPN** | A Site-to-Site VPN is also called as Router-to-Router VPN and is commonly used in the large companies. Companies or organizations, with branch offices in different locations, use Site-to-site VPN to connect the network of one office location to the network at another office location. |
| **Cloud VPN** | A Cloud VPN is a virtual private network that allows users to securely connect to a cloud-based infrastructure or service. It uses the internet as the primary transport medium to connect the remote users to the cloud-based resources. |
| **Mobile VPN** | Mobile VPN is a virtual private network that allows mobile users to securely connect to a private network, typically through a cellular network. It creates a secure and encrypted connection between the mobile device and the VPN server, protecting the data transmitted over the connection. |
| **SSL VPN** | SSL VPN (Secure Sockets Layer Virtual Private Network) is a type of VPN that uses the SSL protocol to secure the connection between the user and the VPN server. It allows remote users to securely access a private network by establishing an encrypted tunnel between the user’s device and the VPN server. |
| **PPTP (Point-to-Point Tunneling Protocol) VPN** | PPTP (Point-to-Point Tunneling Protocol) is a type of VPN that uses a simple and fast method for implementing VPNs. It creates a secure connection between two computers by encapsulating the data packets being sent between them. |
| **L2TP (Layer 2 Tunneling Protocol) VPN** | L2TP (Layer 2 Tunneling Protocol) is a type of VPN that creates a secure connection by encapsulating data packets being sent between two computers. L2TP is an extension of PPTP, it adds more security to the VPN connection by using a combination of PPTP and L2F (Layer 2 Forwarding Protocol) and it uses stronger encryption algorithm than PPTP. |
| **OpenVPN** | OpenVPN is an open-source software application that uses SSL and is highly configurable and secure. It creates a secure and encrypted connection between two computers by encapsulating the data packets being sent between them.OpenVPN can be used to access internal resources such as email, file servers, or databases. |

***

# How VPN Works

A VPN works by creating a secure encrypted connection between your device and a remote server. This way, your data can travel in secrecy instead of through your internet service provider. A VPN hides your real IP address and encrypts your internet connection to make your browsing safer and more private.

<img width="704" alt="image" src="https://github.com/CodeOps-Hub/Documentation/assets/156057205/dbcdaf6b-9ae4-4c74-ad85-a85a42f05347">

### It’s worth finding out how a VPN works so you can make use of its full potential. Here’s what’s going on behind the scenes :-

| **Step** | **Description** |
| -------- | --------------- |
| **Authentication** | When you connect to a virtual private network service, it authenticates your client with a VPN server. |
| **Encryption** | The server then applies an encryption protocol to all data you send and receive. |
| **Tunneling** | The VPN service creates an encrypted “tunnel” over the internet. That secures the data traveling between you and the VPN server. |
| **Encapsulation** | To ensure each data packet stays secure, a VPN wraps it in an outer packet, which is then encrypted through encapsulation. That is the core element of the VPN tunnel, keeping the data safe during transfer. |
| **Decryption** | When the data arrives at the server, the outer packet is removed through a decryption process. |

***

# Advantages of VPN

| **Advantage** | **Description** |
| ------------- | ---------------- |
| **Enhanced Security** | VPNs encrypt data transmitted over the internet, ensuring confidentiality and protecting sensitive information from hackers and eavesdroppers. |
| **Privacy Protection** | By masking users' IP addresses and encrypting their internet traffic, VPNs provide anonymity and prevent websites, advertisers, and ISPs from tracking online activities. |
| **Cost-Effective** | VPNs provide a cost-effective solution for businesses to establish secure connections between remote offices and employees without the need for dedicated leased lines. |
| **Improved Performance** | In some cases, VPNs can improve internet performance by bypassing network congestion and optimizing routing paths, resulting in faster speeds and reduced latency. |

***

# Disadvantages of VPN

| **Disadvantage** | **Description** |
| ---------------- | --------------- |
| **Potential Slowdowns** | Due to encryption and rerouting of traffic, VPNs may introduce latency and decrease internet speed, especially when using servers located far away from the user's physical location. |
| **Complexity** | Setting up and configuring a VPN can be complex, especially for novice users, requiring technical knowledge and troubleshooting skills to resolve connectivity issues or compatibility conflicts. |
| **Compatibility Issues** | Some VPN protocols or configurations may not be compatible with certain devices, operating systems, or network environments, leading to connectivity issues or reduced functionality. |
| **Reliance on VPN Provider** | Users rely on their VPN provider for security and privacy, and any vulnerabilities or breaches in the provider's infrastructure could compromise user data and privacy. |

***

# Best Practices

| **Practice** | **Description** |
| ------------ | --------------- |
| **Choose a Reliable VPN Provider** | Select a reputable VPN service with a strong track record for security and privacy. |
| **Use Kill Switch Feature** | Activate the kill switch feature in your VPN client to automatically disconnect internet traffic if the VPN connection drops, preventing data leaks. |
| **Regularly Update VPN Software** | Keep your VPN software up-to-date to patch any vulnerabilities and ensure optimal performance. |
| **Avoid Free VPN Services** | Free VPNs often come with limitations and may compromise your privacy by logging your activities or serving ads. |
| **Enable Two-Factor Authentication (2FA)** | Add an extra layer of security by enabling 2FA for your VPN account, further safeguarding against unauthorized access. |
| **Review Privacy Policy** | Read and understand the VPN provider's privacy policy to ensure they have a strict no-logs policy and respect user privacy. |
| **Regularly Audit Connected Devices** | Monitor and review the list of connected devices to your VPN account to identify any unauthorized access. |

***

# Conclusion

In conclusion, VPNs play a crucial role in safeguarding online security and privacy in today's digital age. By encrypting internet traffic, masking IP addresses, and bypassing restrictions, VPNs offer users a secure and private online experience. However, it's essential to implement best practices such as choosing a reliable VPN provider, enabling strong encryption, and regularly updating software to maximize security benefits. 

***

# Contact Information

| **Name** | **Email Address** |
| -------- | ----------------- |
| **Shreya Jaiswal** | shreya.jaiswal.snaatak@mygurukulam.co |

***

# References

| **Source** | **Reference** |
| ---------- | ------------- |
| [Link](https://www.kaspersky.com/resource-center/definitions/what-is-a-vpn) | Reference Link for understanding of VPN |
| [Link](https://nordvpn.com/what-is-a-vpn/) | Reference Link for the concept of VPN |
| [Link](https://www.geeksforgeeks.org/what-is-vpn-and-how-it-works/) | Working Of VPN |

