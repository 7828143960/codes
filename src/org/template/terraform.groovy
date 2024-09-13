// package org.template

// import org.terraform.*

// def call(String url, String creds, String branch, String rootPath, String childPath){

//     gitcheckout = new checkout()
//     tfinit = new init()
//     tfvalidate = new validate()
//     tfapply = new apply()
//     notification = new slacknotification()
    
    
  
//     gitcheckout.call(url, creds, branch)
//     tfinit.call(rootPath, childPath)
//     tfvalidate.call(rootPath, childPath)
//     tfapply.call(rootPath, childPath)
//     notification.call()
    
// }
package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath){
    gitcheckout = new checkout()
    tfinit = new init()
    tfvalidate = new validate()
    tfapply = new apply()
    notification = new slacknotification()

    def buildStatus = 'SUCCESS'  // Default to SUCCESS

    try {
        gitcheckout.call(url, creds, branch)
        tfinit.call(rootPath, childPath)
        tfvalidate.call(rootPath, childPath)
        tfapply.call(rootPath, childPath)
    } catch (Exception e) {
        buildStatus = 'FAILURE'
        throw e
    } finally {
        // Check if the build is aborted
        if (currentBuild.result == 'ABORTED') {
            buildStatus = 'ABORTED'
        }
        currentBuild.result = buildStatus
        // Send Slack notification
        notification.call()
        // Debug output
        echo "Final build result in finally block: ${buildStatus}"
    }
}
