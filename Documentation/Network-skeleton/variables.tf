variable "vpc_name" {
  description = "Name of the VPC"
  type        = map(string)
  default = {
    Name = "dev_vpc"                      
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "vpc_tenancy" {
  description = "Tenancy of the VPC"
  type        = string
  default     = "default"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.1.0/25"
}

variable "vpc_enable_dns_support" {
  description = "Enable DNS support for the VPC"
  type        = bool
  default     = true
}

variable "vpc_enable_dns_hostnames" {
  description = "Enable DNS Hostnames for the VPC"
  type        = bool
  default     = true
}

variable "pub_sub_cidr" {
  description = "public subnet cidr blocks"
  type        = list(string)
  default     = ["10.0.1.0/28", "10.0.1.64/28"]

}

variable "pub_sub_az" {
  description = "Public subnet availability zones"
  type        = list(string)
  default     = ["ap-south-1a", "ap-south-1b"]
}

variable "enable_map_public_ip_on_launch" {
  type    = bool
  default = true
}

variable "pub_sub_names" {
  description = "Public subnet tags"
  type        = list(map(string))
  default = [{
    Name = "dev-public-subnet-01"
    Enviroment = "dev"
    Owner = "shreya"
    }, {
    Name = "dev-public-subnet-02"
    Enviroment = "dev"
    Owner = "shreya"
  }]
}

variable "pvt_sub_cidr" {
  description = "Private subnet cidr blocks"
  type        = list(string)
  default     = ["10.0.1.16/28", "10.0.1.32/28", "10.0.1.48/28"]

}

variable "pvt_sub_az" {
  description = "Private subnet availability zones"
  type        = list(string)
  default     = ["ap-south-1a", "ap-south-1b", "ap-south-1c"]
}

variable "pvt_sub_names" {
  description = "Private subnet tags"
  type        = list(map(string))
  default = [{
    Name = "dev-frontend-subnet"
    Enviroment = "dev"
    Owner = "shreya"
    }, {
    Name = "dev-backend-subnet"
    Enviroment = "dev"
    Owner = "shreya"
  }, {
    Name = "dev-database-subnet"
    Enviroment = "dev"
    Owner = "shreya"
  }]
}

variable "dev_pub_rt" {
  type    = map(string)
  default = {
    Name = "dev-pub-rt"
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "dev_pvt_rt" {
  type    = map(string)
  default = {
    Name = "dev-pvt-rt"
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "igw_name" {
  type    = map(string)
  default = {
    Name = "dev-igw"
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "nat_gtw" {
  type    = map(string)
  default = {
    Name = "dev-nat-gtw"
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "sec_grp_name" {
  description     = "Name for the security group"
  type            = string
  default         = "dev_sg"
}

variable "sec_grp_description" {
  description     = "Description for the security group"
  type            = string
  default         = "Security group for Dev Env"
}

variable "sg_inbound_ports" {
  description     = "List of inbound ports, protocols and cidr block"
  type            = list(map(any))
  default         = [
    { port = 22, protocol = "tcp", cidr_blocks = "10.0.1.0/28" }, 
    { port = 3000, protocol = "tcp", cidr_blocks = "0.0.0.0/0" }, 
  ]
}

variable "sg_outbound_ports" {
  description     = "List of outbound ports, protocols and Cidr block "
  type            = list(map(any))
  default         = [
    { port = 0, protocol = "-1", cidr_blocks = "0.0.0.0/0" },
  ]
}

variable "Sec_grp_tags" {
  type            = map(string)
  default         = {
    Name          = "dev-sg"
    Enviroment    = "dev"
    Owner         = "shreya"
  }
}

variable "nacl_tag" {
  type    = map(string)
  default = {
    Name = "dev-nacl"
    Enviroment = "dev"
    Owner = "shreya"
  }
}

variable "nacl_inbound_ports" {
  description = "The list of inbound ports to allow"
  type        = list(object({
    port        = number
    protocol    = string
    cidr_blocks = string
  }))
  default     = [
    { port = 22, protocol = "tcp", cidr_blocks = "10.0.1.0/28" }, 
    { port = 443, protocol = "tcp", cidr_blocks = "10.0.1.0/28" },
    { port = 1194, protocol = "udp", cidr_blocks = "10.0.1.0/28" }, 
  ]
}

variable "nacl_outbound_ports" {
  description = "The list of outbound ports to allow"
  type        = list(object({
    port        = number
    protocol    = string
    cidr_blocks = string
  }))
  default     = [
    { port = 22, protocol = "tcp", cidr_blocks = "10.0.1.0/28" }, 
    { port = 443, protocol = "tcp", cidr_blocks = "10.0.1.0/28" },
    { port = 1194, protocol = "udp", cidr_blocks = "10.0.1.0/28" }, 
  ]
}

variable "lb_name" {
  description  = "Name of the load balancer"
  type         = map(string)
  default      = {
    Name       = "dev-LB"
    Enviroment = "dev"
    Owner      = "shreya"
  }
}

variable "lb_type" {
  description = "Type of the load balancer"
  type        = string
  default     = "application"
}

variable "lb_listener_port" {
  description = "Port for the load balancer listener"
  type        = number
  default     = 80
}

variable "target_group_name" {
  description = "Name of the target group"
  type        = map(string)
  default     = {
    Name      = "dev-TG"
    Environment = "dev"
    Owner       = "shreya"
  }
}

variable "target_group_port" {
  description = "Port for the target group"
  type        = number
  default     = 80
}
