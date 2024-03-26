package org.avengers.genericCi.slackNotification

def call() {
    // Define the DSL for creating a Freestyle job
    def jobDSL = '''
        job('Freestyle-Job') {
            description('This is a sample Freestyle job created using a Scripted Pipeline')
            steps {
                shell('echo "Hello, world!"')
            }
        }
    '''
    // Execute the job DSL to create the Freestyle job
    jobDsl(scriptText: jobDSL)
}
