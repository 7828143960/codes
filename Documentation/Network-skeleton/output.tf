output "vpc_id" {
  description = "The ID of the Dev VPC"
  value       = aws_vpc.dev-vpc.id
}
output "pub_subnet_id" {
  description = "A list of IDs for the public subnets"
  value       = aws_subnet.public_subnets.*.id
}
output "pvt_subnet_id" {
  description = "A list of IDs for the private subnets"
  value       = aws_subnet.private_subnets.*.id
}
output "dev-pub-rt-id" {
  description = "ID of Public Route Table"
  value = aws_route_table.public_rt.id
}

output "dev-pvt-rt-id" {
  description = "ID of Private Route Table"
  value = aws_route_table.private_rt.id
}

output "dev-igw-id" {
  description = "Internet Gateway ID"
  value = aws_internet_gateway.dev_igw.id
}

output "dev-nat-id" {
  description = "NAT Gateway ID"
  value = aws_nat_gateway.dev_nat.id
}

output "Security_Group_ID" {
  description = "ID of Security Group"
  value = aws_security_group.dev_sec_grp.id
}

output "nacl_id" {
  description = "The ID of the created NACL"
  value       = aws_network_acl.nacl.id
}

output "lb_dns_name" {
  description = "The DNS name of the load balancer"
  value       = aws_lb.load_balancer.dns_name
}

output "lb_arn" {
  description = "The ARN of the load balancer"
  value       = aws_lb.load_balancer.arn
}

output "target_group_arn" {
  description = "The ARN of the target group"
  value       = aws_lb_target_group.target_group.arn
}
