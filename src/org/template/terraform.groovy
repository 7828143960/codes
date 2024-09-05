package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath){

    gitcheckout = new checkout()
    tfinit = new init()
    tfvalidate = new validate()
    tfapply = new apply()
    notification = new slack-notification()
    
    
  
    gitcheckout.call(url, creds, branch)
    tfinit.call(rootPath, childPath)
    tfvalidate.call(rootPath, childPath)
    tfapply.call(rootPath, childPath)
    notification.call(rootPath, childPath)
    
}
