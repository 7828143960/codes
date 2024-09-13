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

    try {
        gitcheckout.call(url, creds, branch)
        tfinit.call(rootPath, childPath)
        tfvalidate.call(rootPath, childPath)
        tfapply.call(rootPath, childPath)
        // Only set SUCCESS if all steps are successful
        currentBuild.result = 'SUCCESS'
    } catch (Exception e) {
        // Set FAILURE if an exception occurs
        if (currentBuild.result != 'ABORTED') {
            currentBuild.result = 'FAILURE'
        }
        throw e
    } finally {
        // This block will execute whether the build succeeds, fails, or is aborted
        notification.call()
    }
}
