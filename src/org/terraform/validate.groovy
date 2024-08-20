package org.terraform

def call(String rootPath, String childPath) {
    stage('Terraform Validate') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform validate"
        }
    }
}
