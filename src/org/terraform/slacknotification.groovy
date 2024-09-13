package org.terraform

def call() {
    stage('Slack Notification') {  
        // Use currentBuild.result to get the build status reliably
        def status = currentBuild.result  
        
        def branchName = params.branch ?: 'main'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('GMT'))
        
        def message
        
        // Construct the message based on the build status
        if (status == 'FAILURE') {
            message = "Job Failed on branch ${branchName} at ${jobStartTime} GMT."
        } else if (status == 'SUCCESS') {
            message = "Job Build successfully on branch ${branchName} at ${jobStartTime} GMT."
        } else {
            message = "Job Status: ${status} on branch ${branchName} at ${jobStartTime} GMT."
        }
        
        // Determine the Slack message color based on build status
        def color
        switch (status) {
            case 'SUCCESS':
                color = "good"
                break
            case 'FAILURE':
                color = "danger"
                break
            case 'ABORTED':
                color = "warning"
                break
            default:
                color = "warning"
        }

        // Send the Slack notification
        slackSend channel: 'jenkinss', // Ensure the channel name is correct
            color: color,
            message: """
            ${message}
            Find Status of Pipeline: ${status}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build URL: ${env.BUILD_URL}
            """
    }
}
