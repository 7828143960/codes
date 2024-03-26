package org.avengers.template.genericCi

import org.avengers.genericCi.slackNotification.*

def call(){
  dslJob = new DslJob()
  sendNotification = new SendNotification()

  dslJob .call()
  sendNotification.call()
}
