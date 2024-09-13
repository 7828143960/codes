// package org.terraform

// def call() {
//     stage('Slack Notification') {  
//         def status = env.BUILD_STATUS ?: 'SUCCESS' // Default to 'SUCCESS' if BUILD_STATUS is not set
//         def branchName = params.branch ?: 'Unknown Branch'
//         def buildNumber = env.BUILD_NUMBER ?: 'Unknown Build Number'
//         def message
        
//         if (status == 'FAILURE') {
//             message = "Job Failed on branch ${branchName} (Build #${buildNumber})"
//         } else if (status == 'SUCCESS') {
//             message = "Job Build successfully on branch ${branchName} (Build #${buildNumber})"
//         }
        
//         slackSend channel: 'jenkinss', message: message
//     }
// }

package org.terraform

def call() {
    stage('Slack Notification') {  
        def status = env.BUILD_STATUS ?: 'SUCCESS' 
        def branchName = params.branch ?: 'main'
        def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('GMT'))
        
        def message
        
        if (status == 'FAILURE') {
            message = "Job Failed on branch ${branchName} at ${jobStartTime} GMT."
        } else if (status == 'SUCCESS') {
            message = """
            Job Build successfully on branch ${branchName} at ${jobStartTime} GMT.
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
        slackSend channel: 'jenkinss',
            color: color,
            message: """
            ${message}
            Find Status of Pipeline: ${currentBuild.currentResult}
            Job Name: ${env.JOB_NAME}
            Build Number: ${env.BUILD_NUMBER}
            Build URL: ${env.BUILD_URL}
            """
    }
}

