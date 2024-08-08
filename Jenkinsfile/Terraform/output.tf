output "eks_cluster_name" {
  value = aws_eks_cluster.my_cluster.name
}
output "eks_role_name" {
  value = aws_iam_role.eks_cluster.name
}
output "eks_node_group_name" {
  value = aws_eks_node_group.my_node_group.node_group_name
}
output "ecr_repository_url" {
  value = aws_ecr_repository.my_ecr_repo.repository_url
}
output "eks_node_group_arn" {
  value = aws_iam_role.eks_node_group.arn
}
