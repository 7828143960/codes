package org.terraform

def call() {
 stage(Slack Notification) {  
    def status = env.BUILD_STATUS ?: 'SUCCESS' // Default to 'SUCCESS' if BUILD_STATUS is not set
    if (status == 'FAILURE') {
        slackSend channel: 'jenkinss', message: 'Job Failed'
    } else if (status == 'SUCCESS') {
        slackSend channel: 'jenkinss', message: 'Job Build successfully'
    }
}
}
