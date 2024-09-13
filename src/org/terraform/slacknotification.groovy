package org.terraform

def call() {
    // Define a function for Slack notification
    def sendSlackNotification(String buildStatus) {
        def branchName = params.branch ?: 'main'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('GMT'))
        
        // Construct the message based on the build status
        def message = "Job Status: ${buildStatus} on branch ${branchName} at ${jobStartTime} GMT."

        // Determine the Slack message color based on the build status
        def color
        switch (buildStatus) {
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
        slackSend channel: 'jenkinss', // Ensure the correct Slack channel name
            color: color,
            message: """
            ${message}
            Find Status of Pipeline: ${buildStatus}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build URL: ${env.BUILD_URL}
            """
    }
