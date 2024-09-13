package org.terraform

def call() {
    stage('Slack Notification') {  
        // Capture the current build result
        def BUILD_STATUS = "${currentBuild.currentResult}"
        
        def branchName = params.branch ?: 'main'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('GMT'))
        
        def message
        
        // Construct the message based on the build status
        if (BUILD_STATUS == 'FAILURE') {
            message = "Job Failed on branch ${branchName} at ${jobStartTime} GMT."
        } else if (BUILD_STATUS == 'SUCCESS') {
            message = "Job Build successfully on branch ${branchName} at ${jobStartTime} GMT."
        } else if (BUILD_STATUS == 'ABORTED') {
            message = "Job Aborted on branch ${branchName} at ${jobStartTime} GMT."
        } else {
            message = "Job Status: ${BUILD_STATUS} on branch ${branchName} at ${jobStartTime} GMT."
        }
        
        // Determine the Slack message color based on build status
        def color
        switch (BUILD_STATUS) {
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
            Find Status of Pipeline: ${BUILD_STATUS}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build URL: ${env.BUILD_URL}
            """
    }
}
