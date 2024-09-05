package org.terraform

def call() {
    stage('Slack Notification') {  
        def status = env.BUILD_STATUS ?: 'SUCCESS' // Default to 'SUCCESS' if BUILD_STATUS is not set
        def branchName = params.branch ?: 'Unknown Branch'
        def buildNumber = env.BUILD_NUMBER ?: 'Unknown Build Number'
        def message
        
        if (status == 'FAILURE') {
            message = "Job Failed on branch ${branchName} (Build #${buildNumber})"
        } else if (status == 'SUCCESS') {
            message = "Job Build successfully on branch ${branchName} (Build #${buildNumber})"
        }
        
        slackSend channel: 'jenkinss', message: message
    }
}
