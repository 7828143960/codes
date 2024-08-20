package org.terraform

def call(String rootPath, String childPath) {
    stage("Terraform Plan") {
        script {
            sh "cd ${rootPath}/${childPath} && terraform plan"
        }
    }

    stage('Terraform Apply') {
        script {
            // Run Terraform apply
            sh 'cd ${rootPath}/${childPath} && terraform apply -auto-approve'
        }
    }
} 

