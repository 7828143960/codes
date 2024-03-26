package org.avengers.genericCi.packerAmi

def call() {
    stage('Build AMI') {
        withCredentials([[
            $class: 'AmazonWebServicesCredentialsBinding',
            credentialsId: 'aws creds',
            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
        ]]) {
            sh '/usr/bin/packer build .'
        }
    }
}


