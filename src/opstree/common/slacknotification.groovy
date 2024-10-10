package opstree.common

import opstree.common.*

def slack_notification_factory(Map step_params) {
    logger = new logger()
    if (step_params.slack_notification_enabled == 'true') {
        slack_notification(step_params)
    } else {
        logger.logger('msg': 'No valid option selected for Notification. Please mention correct values.', 'level': 'WARN')
    }
}

def slack_notification(Map step_params) {
    logger = new logger()
    parser = new parser()

    logger.logger('msg': 'Performing Notification', 'level': 'INFO')
    build_status =  "${step_params.build_status}"
    slack_channel = "${step_params.slack_channel}" 

    if (slack_channel == 'jenkinss') {
            def branchName = params.branch ?: 'main'
            def userName = "${env.BUILD_USER}."
            def jobStartTime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('Asia/Kolkata'))
            def message 

            // Construct the message based on the build status
            if (build_status == 'FAILURE') {
                message = "Job Build Failed on branch ${branchName} at ${jobStartTime} IST."
            } else if (build_status == 'SUCCESS') {
                message = "Job Build successfully on branch ${branchName} at ${jobStartTime} IST."
            } else if (build_status == 'ABORTED') {
                message = "Job Build was aborted on branch ${branchName} at ${jobStartTime} IST."
            } else {
                message = "Job Build status is ${build_status} on branch ${branchName} at ${jobStartTime} IST."
            }

            // Determine the color based on build status
            def color
            switch (build_status) {
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

            // Send the Slack notification using a variable for the channel
            slackSend(
                channel: slack_channel,
                color: color,
                message: """
                ${message}
                Find Status of Pipeline: ${build_status}
                Triggered By: ${userName}
                Job Name: ${env.JOB_NAME}
                Build Number: ${env.BUILD_NUMBER}
                Build URL: ${env.BUILD_URL}
                """
            )
        }
    } else {
        logger.logger('msg': 'No valid notification channel selected', 'level': 'WARN')
    }
