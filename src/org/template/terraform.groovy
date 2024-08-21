package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath, String ACTION){

    gitcheckout = new checkout()
    tfinit = new init()
    tfvalidate = new validate()
    tffmt = new fmt()
    tfapply = new apply()
    
    
  
    gitcheckout.call(url, creds, branch)
    tfinit.call(rootPath, childPath)
    tfvalidate.call(rootPath, childPath)
    tffmt.call(rootPath, childPath)
    tfapply.call(rootPath, childPath, ACTION)
    
}
