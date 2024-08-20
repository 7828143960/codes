resource "aws_instance" "example" {
  ami           = "ami-0c55b159cbfafe1f0"  # Change to your preferred AMI ID
  instance_type = "t2.micro"               # Instance type

  tags = {
    Name = "example-instance"
  }

  # Ensure you have an existing VPC and subnet
  subnet_id                   = "subnet-xxxxxxxx"  # Replace with your subnet ID
  associate_public_ip_address = true               # To associate a public IP address

  # Optional - Add security group to allow SSH and HTTP access
  security_groups = ["example-security-group"]
}

resource "aws_security_group" "example" {
  name        = "example-security-group"
  description = "Allow SSH and HTTP traffic"
  vpc_id      = "vpc-xxxxxxxx"  # Replace with your VPC ID

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
