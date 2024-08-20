package org.template

import org.terraform.*

def call(String url, String creds, String branch, String rootPath, String childPath, String ACTION){

    checkout = new gitcheckout()
    init = new tfinit()
    validate = new tfvalidate()
    fmt = new tffmt()
    apply = new tfapply()
    
    
  
    gitcheckout.call(url, creds, branch)
    tfinit.call(rootPath, childPath)
    tfvalidate.call(rootPath, childPath)
    tffmt.call(rootPath, childPath, ACTION)
    tfapply.call(rootPath, childPath, ACTION)
    
}
