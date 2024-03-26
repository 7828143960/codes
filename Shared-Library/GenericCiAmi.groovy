package org.avengers.template.genericCi

import org.avengers.common.*
import org.avengers.genericCi.packerAmi.*

def call(String url, String creds, String branch){
    gitCheckoutPrivate = new GitCheckoutPrivate()
    def awsCred = new AwsCreds()
    def packerInit = new PackerInit()
    def packerBuild = new PackerBuild()

    gitCheckoutPrivate.call(url, creds, branch)
    awsCred.awsCredentials()  // Call the correct method
    packerInit.call()
    packerBuild.call()
}
