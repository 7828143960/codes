
package org.terraform

def call(String rootPath, String childPath, String ACTION) {
   // stage('Approval For Destroy') {
     //   script {
        //    if (ACTION == 'destroy') {
                // input "Do you want to Destroy Terraform Infrastructure?"
          //  }
       // }
   // }

    if (ACTION == 'destroy') {
        stage('Terraform destroy') {
            script {
                sh "cd ${rootPath}/${childPath} && terraform destroy -auto-approve"
            }
        }
    }
}
