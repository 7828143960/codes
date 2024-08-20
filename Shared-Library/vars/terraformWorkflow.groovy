// vars/terraformWorkflow.groovy

def call() {
    stage('Terraform Initialization') {
        try {
            sh 'terraform init'
        } catch (Exception e) {
            echo 'Failed while initializing terraform'
            throw e
        }
    }

    stage('Terraform Formatting') {
        try {
            sh 'terraform fmt'
        } catch (Exception e) {
            echo 'Failed while formatting terraform files'
            throw e
        }
    }

    stage('Terraform Validation') {
        try {
            sh 'terraform validate'
        } catch (Exception e) {
            echo 'Failed while validating terraform config files'
            throw e
        }
    }

    stage('Terraform Plan') {
        try {
            sh 'terraform plan -var-file=network.tfvars'
        } catch (Exception e) {
            echo 'Failed while applying terraform plan'
            throw e
        }
    }

    stage('Terraform Apply') {
        input 'Do you want to proceed or not?'

        try {
            sh 'terraform apply -var-file=network.tfvars -auto-approve'
        } catch (Exception e) {
            echo 'Failed while applying terraform'
            throw e
        }
    }
}
