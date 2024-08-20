package org.terraform

def call(String rootPath, String childPath) {
    stage('Terraform fmt') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform fmt"
        }
    }
}
