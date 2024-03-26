package org.avengers.genericCi.packerAmi

def call() {
    stage('Initialize Packer') {
            sh '/usr/bin/packer init .'
        }
    }


