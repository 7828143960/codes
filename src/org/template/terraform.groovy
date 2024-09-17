// package org.template

// import org.terraform.*

// def call(String url, String creds, String branch, String rootPath, String childPath) {
//     gitcheckout = new checkout()
//     tfinit = new init()
//     tfvalidate = new validate()
//     tfapply = new apply()
//     notification = new slacknotification()

//     try {
//         gitcheckout.call(url, creds, branch)
//         tfinit.call(rootPath, childPath)
//         tfvalidate.call(rootPath, childPath)
//         tfapply.call(rootPath, childPath)
//         currentBuild.result = 'SUCCESS'
//     } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
//         currentBuild.result = 'ABORTED'
//         throw e
//     } catch (Exception e) {
//         currentBuild.result = 'FAILURE'
//         throw e
//     } finally {
//         notification.call()
//     }
// }

package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath) {
    gitcheckout = new checkout()
    tfinit = new init()
    tfvalidate = new validate()
    tfapply = new apply()
    notification = new slacknotification()

    try {
        gitcheckout.call(url, creds, branch)
        tfinit.call(rootPath, childPath)
        tfvalidate.call(rootPath, childPath)
        tfapply.call(rootPath, childPath)
        currentBuild.result = 'SUCCESS'
    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
        currentBuild.result = 'ABORTED'
        throw e
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        // Pass the parameters for the Slack notification
        notification.call([
            MESSAGE: "Build completed with status: ${currentBuild.result}",
            CHANNEL: "jenkinss",
            DOMAIN: "demoworkspace-6868926",
            TOKEN_ID: "slack"
        ])
    }
}
