# Security Group #
locals {
  inbound_ports         = var.inbound_ports
  outbound_ports        = var.outbound_ports
}
resource "aws_security_group" "sec_grp" {
  name                  = var.sec_grp_name
  description           = var.Sec_grp_description
  vpc_id                = var.vpc_id

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

/*------------Genrate-Key--------------*/
resource "tls_private_key" "rsa_4096" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

/*----------pem Key----------------------*/
resource "aws_key_pair" "key_pair" {
  key_name   = var.key_name
  public_key = tls_private_key.rsa_4096.public_key_openssh
}

/*----------Download Pem Key-------------------*/
resource "local_file" "private_key" {
  content  = tls_private_key.rsa_4096.private_key_pem
  filename = var.key_name
}

/*-----------Standalone Server-------------------*/

resource "aws_instance" "standalone_server" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = var.server_type
  key_name                    = aws_key_pair.key_pair.key_name
  subnet_id                   = var.subnet_id
  associate_public_ip_address = false
  vpc_security_group_ids      = [aws_security_group.sec_grp.id]
  tags = {
    Name = var.server_name
    Type = "dev"
  }

}
