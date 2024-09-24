package opstree.common

import opstree.common.*

def notification_factory(Map step_params) {
    logger = new logger()
    if (step_params.notification_enabled == 'true') {
        notification(step_params)
    }
  else {
        logger.logger('msg':'No valid option selected for Notification. Please mention correct values.', 'level':'WARN')
  }
}

def notification(Map step_params) {
    logger = new logger()
    parser = new parser()

    logger.logger('msg':'Performing Notification', 'level':'INFO')
    build_status = "${step_params.build_status}"
    notification_channel = "${step_params.notification_channel}"

    if (step_params.notification_channel == 'teams') {
        def message = ''
        def color = ''
        def remarks = "Started by user ${env.BUILD_USER_ID}." // Customize as needed
        webhook_url_creds_id = "${step_params.webhook_url_creds_id}"

        if (build_status == 'Success') {
            message = "${env.JOB_NAME}: BUILD SUCCESS."
            color = '#008000'
        } else if (build_status == 'Failure') {
            message = "${env.JOB_NAME}: BUILD FAILED!!!"
            color = '#FF0000'
        } else if (build_status == 'UNSTABLE') {
            message = "${env.JOB_NAME}: BUILD UNSTABLE!!"
            color = '#FFFF00'
        } else {
            message = "${env.JOB_NAME}: BUILD RESULT UNKNOWN!!"
            color = '#FFA500'
        }

        withCredentials([string(credentialsId: webhook_url_creds_id, variable: 'WEBHOOK_URL')]) {
            office365ConnectorSend(
                webhookUrl: "${WEBHOOK_URL}",
                status: build_status,
                color: color,
                message: """<b>${message}</b><br><br>
                        <strong>Build No: #${env.BUILD_NUMBER}</strong><br><br>
                        <strong>Remarks</strong>: ${remarks}<br><br>"""
            )
        }
    }
}
