| Author | Created on | Last Updated | Document Version |
| ------ | ---------- | ------------ | ---------------- |
| Shreya | 10-01-2024 | 15-01-2024   | v1               |

***

# Table Of Contents

**1. Introduction**

**2. Purpose**

**3. Evolution of Version Control Systems (VCS) Before Git**

**4. Key points that enhances GitHub tool follows**

**5. Additional Benefits of GitHub**

**6. Branching Strategies**

**7. Benefits of GitHub**

**8. Challenges of GitHub**

**9. Conclusion**

**10. Contact Information**

**11. Reference**

***

# Introduction:-
  This is the documentation for GitHub including it's features and also covered the necessary points to enhance the tool. It combines the distributed 
  version control system Git with powerful collaboration features, making it a central hub for developers to work on projects, share code, and 
  collaborate with others. 

***

# Purpose:-
*  GitHub serves as a comprehensive platform that goes beyond basic version control. 
*  It empowers developers to collaborate, manage projects, automate workflows, and build a global community around their code. 
*  The combination of powerful features and a user-friendly interface makes GitHub a central hub for modern software development.

***

# Evolution of Version Control Systems (VCS) Before Git:-
Before Git and GitHub, version control systems were crucial for managing source code changes. Centralized systems like CVS (Concurrent Versions System) and SVN (Apache Subversion) were popular. However, they had limitations such as the need for a constant network connection, difficulty in branching and merging, and a single point of failure with the central server.

**01.Introduction of Git**:-
Git, created by Linus Torvalds in 2005, revolutionized version control. It is a Distributed Version Control System (DVCS) designed to address the shortcomings of existing systems. Git allows for fast branching, merging, and decentralized workflows. Developers can work offline and commit changes to their local repositories before synchronizing with a central or remote repository.

**02.GitHub's Role in Modern Software Development**:-
GitHub, founded in 2008 by Chris Wanstrath, PJ Hyett, and Tom Preston-Werner, built on Git's foundations and introduced a platform for hosting Git repositories. GitHub has played a pivotal role in modern software development for several reasons:

| Features | Description |
| -------- | ----------- |
| **Collaboration and Social Coding** | GitHub transformed version control into a social platform. Developers can follow projects, star repositories, and contribute to open-source projects seamlessly. |
| **Pull Requests and Code Review** | GitHub introduced the concept of pull requests, allowing contributors to propose changes, discuss modifications, and request code reviews. This workflow has become a standard for collaboration. |

***

# Key points that enhances GitHub tool follows:-
| Features | Description |
| -------- | ----------- |
| **Version Control System** | Enable teams to work concurrently, manage code changes, review and discuss modifications, and ensure a smooth integration of new features or bug fixes into the main codebase. |
|  **Project Management** | GitHub's project management features contribute to efficient team coordination and streamlined workflow. |
|  **Documentation** |GitHub documentation typically refers to a dedicated folder within a repository that contains files such as README.md, CONTRIBUTING.md, and other markdown or plain text files that provide information about the project. |
|  **Continuous Integration/Continuous Deployment** | It allows you to automate workflows, run tests, and deploy code directly from your GitHub repository. |
|  **Security** | GitHub has robust security features, including code scanning, helps identify and address security vulnerabilities in the codebase. |
|  **Wiki** | GitHub Wikis are a separate feature that allows users to create collaboratively editable pages for documentation, notes, and other information. |
|  **Issue Tracking** | GitHub includes a robust issue-tracking system. Teams can use it to create, assign, and prioritize tasks, bugs, and feature requests. |

***

# Additional Benefits of GitHub:-

 # GitHub Pages:-
* **GitHub Pages is a feature provided by GitHub that allows users to host static websites directly from their GitHub repositories. It's a convenient 
    way to showcase projects, host documentation, or create a personal website. Using GitHub Pages is straightforward, and it provides a quick and 
    efficient way to share static content with a broader audience. Whether you're creating a personal portfolio, documenting a project, or showcasing a 
    tutorial, GitHub Pages simplifies the process of hosting and maintaining static websites directly from your GitHub repository.**

![github pages](https://github.com/avengers-p7/Documentation/assets/156057205/c936878b-b6bb-4b99-a1de-217dd4c46941)

***

 # GitHub Copilot :-
* **GitHub Copilot is an AI tool that provides you code suggestions based on comments and the context of the file you’re editing. It is an AI-powered coding assistant developed collaboratively by GitHub and OpenAI. It’s built on the foundation of the GPT (Generative Pre-trained Transformer) technology, which enables it to understand natural language descriptions and generate code based on context.**

![image](https://github.com/avengers-p7/Documentation/assets/156057205/f9de33c5-a1d2-4416-be5b-eb734680ab43)

***

# Branching Strategies:-

## 01.Gitflow Workflow:-
Gitflow defines a branching model with branches for features, releases, and hotfixes. It provides a structured approach for managing releases and hotfixes.It defines specific branch responsibilities, such as main/master for production, develop for active development, feature for new features, release as a gatekeeper to production, and hotfix for addressing urgent issues.

![image](https://github.com/avengers-p7/Documentation/assets/156057205/78699006-db3e-4287-8692-e120197ebb66)

## 02.GitHub-Flow:-
GitHub-Flow simplifies Git-Flow by eliminating release branches. It revolves around one active development branch (often main or master) that is directly deployed to production. Features and bug fixes are implemented using long-living feature branches. 

![image](https://github.com/avengers-p7/Documentation/assets/156057205/350ed7f1-acbb-40a2-98fd-d15e2eae5480)

## 03.GitLab-Flow:-
GitLab-Flow strikes a balance between Git-Flow and GitHub-Flow. It adopts GitHub-Flow’s simplicity while introducing additional branches representing staging environments before production. The main branch still represents the production environment.

![image](https://github.com/avengers-p7/Documentation/assets/156057205/a6b14fdf-cf7c-41bc-9e7b-75f3410e8e24)

## 04.Trunk Based Development:-
Trunk Based Development promotes a single shared branch called “trunk” and eliminates long-living branches. There are two variations based on team size: smaller teams commit directly to the trunk, while larger teams create short-lived feature branches. Frequent integration of smaller feature slices is encouraged to ensure regular merging.

![image](https://github.com/avengers-p7/Documentation/assets/156057205/ce4652f1-6571-4600-9c96-3a61f620bc2c)

***

# Benefits of GitHub:-
| Features | Description |
| -------- | ----------- |
| **Collaboration and Code Review** | GitHub provides a platform for collaborative development with features such as pull requests, which facilitate code review and discussion. |
| **Repository Hosting** | GitHub hosts Git repositories, providing a centralized location for storing, sharing, and managing source code. Repositories can be public or private, allowing for open-source projects or private development work. |
| **Community Support** | GitHub has a large and active community of developers. This community support can be valuable for troubleshooting, seeking advice, and learning best practices from others in the field. |

*** 

# Challenges of GitHub:-
| Challenge | Problem | Solution |
| --------- | ------- | -------- |
|  **Large File Storage** | GitHub has limitations on large file storage, which can impact repositories with large assets | Use Git LFS (Large File Storage) for managing large files and consider external storage solutions for assets.|
|  **Merge Conflicts** | When multiple contributors make changes to the same file, it can result in merge conflicts during pull requests | Regularly pull changes from the main branch, communicate with team members, and resolve conflicts promptly. |

***

# Conclusion:-
* In conclusion, the documentation presented here serves as a comprehensive and user-friendly guide for navigating, contributing to, and maintaining 
  the GitHub project.
* GitHub has become an indispensable tool for developers, providing a centralized hub for code hosting, version control, issue tracking, and 
  collaboration. Its user-friendly interface, powerful features like pull requests and project boards, and seamless integration with CI/CD tools 
  contribute to its widespread adoption.

***

# Contact Information:-
| Name | Email Address |
| ---- | ------------- |
| Shreya Jaiswal | shreya.jaiswal.snaatak@mygurukulam.co |

***
                                                                       
# References:-
| Links | Description |
| ----- | ----------- |
| https://unstop.com/blog/what-is-github | reference link used for the documentation |
| https://marker.io/blog/github-vs-gitlab-vs-bitbucket | documentation explored for GitHub features |
| https://www.sitepoint.com/github-copilot-ai-pair-programming/ | documentation for GitHub Copilot |
| https://www.cloudbees.com/blog/branching-strategy | link for Branching Strategies |



