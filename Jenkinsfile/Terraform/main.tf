# EKS Cluster
resource "aws_eks_cluster" "my_cluster" {
  name     = "eks-cluster"
  role_arn = aws_iam_role.eks_cluster.arn

  vpc_config {
    subnet_ids          = flatten([aws_subnet.my_public_subnets.*.id, aws_subnet.my_private_subnets.*.id])
    security_group_ids  = [aws_security_group.shared_sg.id]
  }

  depends_on = [aws_iam_policy_attachment.eks_cluster_policy_attachment_1]
}

# IAM Role for EKS Cluster
resource "aws_iam_role" "eks_cluster" {
  name = "eks-cluster-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
        Principal = {
          Service = "eks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "eks-cluster-role"
  }
}

resource "aws_iam_policy_attachment" "eks_cluster_policy_attachment_1" {
  name       = "eks-cluster-policy-attachment-1"
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  roles      = [aws_iam_role.eks_cluster.name]
}

resource "aws_iam_policy_attachment" "eks_cluster_policy_attachment_2" {
  name       = "eks-cluster-policy-attachment-2"
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSServicePolicy"
  roles      = [aws_iam_role.eks_cluster.name]
}

resource "aws_iam_policy_attachment" "eks_cluster_policy_attachment_3" {
  name       = "eks-cluster-policy-attachment-3"
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
  roles      = [aws_iam_role.eks_cluster.name]
}

# IAM Role for EKS Node Group
resource "aws_iam_role" "eks_node_group" {
  name = "eks-node-group-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "eks-node-group-role"
  }
}

resource "aws_iam_policy_attachment" "eks_node_group_worker_policy" {
  role       = aws_iam_role.eks_node_group.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
}

resource "aws_iam_policy_attachment" "eks_node_group_ecr_policy" {
  role       = aws_iam_role.eks_node_group.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

resource "aws_iam_policy_attachment" "example_AmazonEKS_CNI_Policy" {
  role       = aws_iam_role.eks_node_group.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
}

# VPC
resource "aws_vpc" "my_vpc" {
  cidr_block = var.networking.cidr_block

  tags = {
    Name  = "eks-vpc"
    Owner = "shreya"
  }
}

# Public Subnets
resource "aws_subnet" "my_public_subnets" {
  count = length(var.networking.public_subnets)

  cidr_block            = var.networking.public_subnets[count.index]
  vpc_id                = aws_vpc.my_vpc.id
  availability_zone     = var.networking.azs[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name = "public_subnet-${count.index}"
  }
}

# Private Subnets
resource "aws_subnet" "my_private_subnets" {
  count = length(var.networking.private_subnets)

  vpc_id                = aws_vpc.my_vpc.id
  cidr_block            = var.networking.private_subnets[count.index]
  availability_zone     = var.networking.azs[count.index]
  map_public_ip_on_launch = false

  tags = {
    Name = "private_subnet-${count.index}"
  }
}

# Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.my_vpc.id

  tags = {
    Name = "eks-igw"
  }
}

# Elastic IPs
resource "aws_eip" "elastic_ip" {
  count = var.networking.nat_gateways ? length(var.networking.private_subnets) : 0

  depends_on = [aws_internet_gateway.igw]

  tags = {
    Name = "eip-${count.index}"
  }
}

# NAT Gateways
resource "aws_nat_gateway" "nats" {
  count = var.networking.nat_gateways ? length(var.networking.private_subnets) : 0

  subnet_id         = aws_subnet.my_public_subnets[count.index].id
  connectivity_type = "public"
  allocation_id     = aws_eip.elastic_ip[count.index].id

  depends_on = [aws_internet_gateway.igw]
}

# Public Route Tables
resource "aws_route_table" "public_table" {
  vpc_id = aws_vpc.my_vpc.id

  tags = {
    Name = "public-route-table"
  }
}

resource "aws_route" "public_routes" {
  route_table_id         = aws_route_table.public_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.igw.id
}

resource "aws_route_table_association" "public_table_association" {
  count      = length(var.networking.public_subnets)
  subnet_id  = aws_subnet.my_public_subnets[count.index].id
  route_table_id = aws_route_table.public_table.id
}

# Private Route Tables
resource "aws_route_table" "private_tables" {
  count = length(var.networking.private_subnets)
  vpc_id = aws_vpc.my_vpc.id

  tags = {
    Name = "private-route-table-${count.index}"
  }
}

resource "aws_route" "private_routes" {
  count                 = length(var.networking.private_subnets)
  route_table_id        = aws_route_table.private_tables[count.index].id
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id        = aws_nat_gateway.nats[count.index].id
}

resource "aws_route_table_association" "private_table_association" {
  count      = length(var.networking.private_subnets)
  subnet_id  = aws_subnet.my_private_subnets[count.index].id
  route_table_id = aws_route_table.private_tables[count.index].id
}

# Security Group
resource "aws_security_group" "shared_sg" {
  vpc_id = aws_vpc.my_vpc.id

  tags = {
    Name = "shared-sg"
  }
}

resource "aws_security_group_rule" "shared_inbound" {
  type              = "ingress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = [aws_vpc.my_vpc.cidr_block]
  security_group_id = aws_security_group.shared_sg.id
}

resource "aws_security_group_rule" "shared_outbound" {
  type              = "egress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.shared_sg.id
}

# EKS Node Group
resource "aws_eks_node_group" "my_node_group" {
  cluster_name    = aws_eks_cluster.my_cluster.name
  node_group_name = "my-node-group"
  version         = aws_eks_cluster.my_cluster.version
  node_role_arn   = aws_iam_role.eks_node_group.arn
  subnet_ids      = aws_subnet.my_private_subnets.*.id

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  depends_on = [
    aws_iam_policy_attachment.eks_node_group_worker_policy,
    aws_iam_policy_attachment.eks_node_group_ecr_policy,
    aws_iam_policy_attachment.example_AmazonEKS_CNI_Policy,
  ]
}

# ECR Repository
resource "aws_ecr_repository" "my_ecr_repo" {
  name = "my-ecr-repo"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Name        = "my-ecr-repo"
    Environment = "local"
  }
}
