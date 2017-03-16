//
// Pipeline to handle build result changing from failure to success. For example, if it needs to send an email when it recovers.
//
// ** Trying to notify changed only for success from fail, not report twice when changed to fail **
//

// Used to track failure any stages using a stage level post section
Boolean buildFailed = false

pipeline {
    
    agent any

    parameters {
        booleanParam(name: 'FAIL', defaultValue: false, description: 'Check to make it fail')
    }

    stages {
        stage('Test') {
            steps {
                script {

                    if(params.FAIL == true) {
                        echo "This build will fail"
                        error("Build has failed")
                    }
                    else {
                        echo "This build is a success"
                    }
                }
            }
            
            // Do this on every stage to detect failures
            post { failure { script { buildFailed = true } }
        }
        
        stage('Another') {
          steps { echo 'another stage..' }
          post { failure { script { buildFailed = true } }
        }
    }

    post {
        always  {
            echo "Build completed"
        }
        changed {
            echo 'Build result changed'
            
            // Notify only on change to success
            script { 
              if(!buildFailed) {
                echo 'NOTIFY: Build changed to SUCCESS'
              }
            }
        }
        failure {
            echo 'NOTIFY: Build failed'
        }
        success {
            echo 'Build was a success'
        }        
        unstable {
            echo 'Build has gone unstable'
        }
    }
}
