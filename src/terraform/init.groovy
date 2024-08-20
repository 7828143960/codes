package src.terraform

def call(String rootPath, String childPath) {
    stage('Terraform init') {
        script {
            sh "cd ${rootPath}/${childPath} && terraform init"
        }
    }
}
