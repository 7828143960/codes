output "Security_Group_ID" {
  value = [aws_security_group.sec_grp.id]
}
output "server_id" {
  value = [aws_instance.standalone_server.id]
}
