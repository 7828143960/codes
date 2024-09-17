package org.terraform

def call() {
    stage('Slack Notification') {
        def status = currentBuild.currentResult ?: 'SUCCESS'
        def branchName = params.branch ?: 'main'
        def userName = env.BUILD_USER ? env.BUILD_USER : 'User'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('Asia/Kolkata'))
        def message
        
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

        slackSend channel: 'jenkinss',
            color: color,
            message: """
            ${message}
            Find Status of Pipeline: ${status}
            Build username: ${userName}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build url: ${env.BUILD_URL}
            """
    }
}

