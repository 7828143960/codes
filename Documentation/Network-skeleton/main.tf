resource "aws_vpc" "dev-vpc" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = var.vpc_enable_dns_support
  enable_dns_hostnames = var.vpc_enable_dns_hostnames
  tags = var.vpc_name
}

resource "aws_subnet" "public_subnets" {
  count                   = length(var.pub_sub_cidr)
  vpc_id                  = aws_vpc.dev-vpc.id
  cidr_block              = var.pub_sub_cidr[count.index]
  availability_zone       = var.pub_sub_az[count.index]
  map_public_ip_on_launch = var.enable_map_public_ip_on_launch
  tags                    = var.pub_sub_names[count.index]
}

resource "aws_subnet" "private_subnets" {
  count             = length(var.pvt_sub_cidr)
  vpc_id            = aws_vpc.dev-vpc.id
  cidr_block        = var.pvt_sub_cidr[count.index]
  availability_zone = var.pvt_sub_az[count.index]
  tags              = var.pvt_sub_names[count.index]
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.dev-vpc.id
  route {
    cidr_block = var.vpc_cidr
    gateway_id = "local"
  }
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.dev_igw.id
  }
  tags = var.dev_pub_rt
}

resource "aws_route_table_association" "dev_pub_route_association01" {
  count = length(aws_subnet.public_subnets.*.id)
  subnet_id      = aws_subnet.public_subnets[count.index].id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.dev-vpc.id
  route {
    cidr_block = var.vpc_cidr
    gateway_id = "local"
  }
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.dev_nat.id
  }
  tags = var.dev_pvt_rt
  depends_on = [aws_nat_gateway.dev_nat]
}

resource "aws_route_table_association" "dev_pvt_route_association01" {

  count = length(aws_subnet.private_subnets.*.id)
  subnet_id      = aws_subnet.private_subnets[count.index].id
  route_table_id = aws_route_table.private_rt.id
  depends_on     = [aws_route_table.private_rt]
}

resource "aws_internet_gateway" "dev_igw" {
  vpc_id = aws_vpc.dev-vpc.id
  tags = var.igw_name
}

resource "aws_eip" "dev_elastic_ip" {
  domain = "vpc"
}

resource "aws_nat_gateway" "dev_nat" {
  allocation_id = aws_eip.dev_elastic_ip.id
  subnet_id     = aws_subnet.public_subnets[0].id
  tags          = var.nat_gtw
  depends_on    = [aws_eip.dev_elastic_ip]
}
locals {
  inbound_ports         = var.sg_inbound_ports
  outbound_ports        = var.sg_outbound_ports
}
resource "aws_security_group" "dev_sec_grp" {
  name                  = var.sec_grp_name
  description           = var.sec_grp_description
  vpc_id                = aws_vpc.dev-vpc.id

  dynamic "ingress" {
  for_each              = local.inbound_ports
  content {
   from_port            = ingress.value.port
   to_port              = ingress.value.port
   protocol             = ingress.value.protocol
   cidr_blocks          = [ingress.value.cidr_blocks]
  }
 }
 dynamic "egress" {
  for_each              = local.outbound_ports
  content {
   from_port            = egress.value.port
   to_port              = egress.value.port
   protocol             = egress.value.protocol
   cidr_blocks          = [egress.value.cidr_blocks]
  }
 }
  tags                  = var.Sec_grp_tags
}

resource "aws_network_acl" "nacl" {
  vpc_id     = aws_vpc.dev-vpc.id
  subnet_ids = [aws_subnet.public_subnets[0].id]
  tags       = var.nacl_tag

  dynamic "ingress" {
    for_each = var.nacl_inbound_ports
    content {
      rule_no     = ingress.key + 100
      protocol    = ingress.value.protocol
      action      = "allow"
      cidr_block  = ingress.value.cidr_blocks
      from_port   = ingress.value.port
      to_port     = ingress.value.port
    }
  }

  dynamic "egress" {
    for_each = var.nacl_outbound_ports
    content {
      rule_no     = egress.key + 100
      protocol    = egress.value.protocol
      action      = "allow"
      cidr_block  = egress.value.cidr_blocks
      from_port   = egress.value.port
      to_port     = egress.value.port
    }
  }
}

resource "aws_lb" "load_balancer" {
  internal           = false
  load_balancer_type = var.lb_type
  security_groups    = [aws_security_group.dev_sec_grp.id]
  subnets            = aws_subnet.public_subnets[*].id
  tags               = var.lb_name 
}

resource "aws_lb_target_group" "target_group" {
  port        = var.target_group_port
  protocol    = "HTTP"
  vpc_id      = aws_vpc.dev-vpc.id
  target_type = "ip"
  tags        = var.target_group_name
}

resource "aws_lb_listener" "listener" {
  load_balancer_arn = aws_lb.load_balancer.arn
  port              = var.lb_listener_port
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.target_group.arn
  }
}
