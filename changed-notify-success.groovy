//
// Pipeline to handle build result changing from failure to success. For example, if it needs to send an email when it recovers.
//
// ** Trying to notify changed only for success from fail, not report twice when changed to fail **
//

// Track fail and changed in first pipeline
Boolean buildFailed = false
Boolean buildChanged = false

// Main pipeline that does the processing
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
        }
    }

    post {
        always  {
            echo "Build completed. currentBuild.result = ${currentBuild.result}"
        }
        changed {
            echo 'Build result changed'
            script { buildChanged = true }
        }
        failure {
            echo 'Build failed'
            echo 'NOTIFY: Build failed'
            script { buildFailed = true }
        }
        success {
            echo 'Build was a success'
        }        
        unstable {
            echo 'Build has gone unstable'
        }
    }
}

// 'Floating' scripted stage to notify. Is this legit?
stage('Notify') {
    if(!buildFailed && buildChanged) {
        echo 'NOTIFY: Build changed to SUCCESS'
    }
}
