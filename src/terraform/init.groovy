package org.avengers.terraform_CICD

def call(String rootPath, String childPath) {
    stage('Terraform init') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform init"
        }
    }
}
