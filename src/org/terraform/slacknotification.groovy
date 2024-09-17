package org.terraform

def call(Map stepParams) {
    stage('Slack Notification') {
        // Use currentBuild.currentResult to get the build status
        def status = currentBuild.currentResult ?: 'SUCCESS'
        def branchName = params.branch ?: 'main'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('GMT'))
        def message

        // Construct the message based on the build status
        if (status == 'FAILURE') {
            message = """
            Job Build Failed on branch ${branchName} at ${jobStartTime} GMT.
            """
        } else if (status == 'SUCCESS') {
            message = """
            Job Build successfully on branch ${branchName} at ${jobStartTime} GMT.
            """
        } else if (status == 'ABORTED') {
            message = """
            Job Build was aborted on branch ${branchName} at ${jobStartTime} GMT.
            """
        } else {
            message = """
            Job Build status is ${status} on branch ${branchName} at ${jobStartTime} GMT.
            """
        }

        // Determine the color based on build status
        def color
        if (status == 'SUCCESS') {
            color = "good"
        } else if (status == 'FAILURE') {
            color = "danger"
        } else if (status == 'ABORTED') {
            color = "warning"
        } else {
            color = "warning"
        }

        // Send Slack message
        slackSend channel: stepParams.CHANNEL,
            color: color,
            message: """
            ${message}
            Find Status of Pipeline: ${status}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build URL: ${env.BUILD_URL}
            BUILD_USER: ${env.BUILD_USER}
            CHANNEL: ${stepParams.CHANNEL}
            DOMAIN: ${stepParams.DOMAIN}
            TOKEN_ID: ${stepParams.TOKEN_ID}
            """
    }
}

