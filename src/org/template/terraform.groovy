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

    // Keep track of whether an exception occurs
    def aborted = false

    try {
        gitcheckout.call(url, creds, branch)
        tfinit.call(rootPath, childPath)
        tfvalidate.call(rootPath, childPath)
        tfapply.call(rootPath, childPath)
        // Set SUCCESS if all steps are successful
        currentBuild.result = 'SUCCESS'
    } catch (Exception e) {
        // Set FAILURE if an exception occurs
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        // Check if the build was aborted
        if (currentBuild.result == null || currentBuild.result == 'ABORTED') {
            aborted = true
            currentBuild.result = 'ABORTED'
        }
        
        // Send Slack notification
        notification.call()
        
        // Debug output for tracking
        echo "Build result in finally block: ${currentBuild.result}"
    }
}
