package org.avengers.genericCi.packerAmi

class AwsCreds {
    static def awsCredentials() {
        return [
            credentialsId: 'aws creds',
            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
        ]
    }
}

