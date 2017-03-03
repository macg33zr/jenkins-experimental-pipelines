//
// Pipeline to handle build result changing from failure to success. For example, if it needs to send an email when it recovers.
//
pipeline {
    agent any

    parameters {
        string(name: 'FAIL', defaultValue: 'false', description: 'Whether to fail')
    }

    stages {
        stage('Test') {
            steps {
                script {

                    if(params.FAIL == 'true') {
                        echo "This build will fail"
                        currentBuild.result = 'FAILURE'
                        error("Build has failed")
                    }
                    else {
                        echo "This build is a success"
                        currentBuild.result = 'SUCCESS'
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

            script {
                if(currentBuild.result == 'SUCCESS') {
                    echo 'Build has changed to SUCCESS status'
                }
            }
        }
        failure {
            echo 'Build failed'
        }
        success {
            echo 'Build was a success'
        }        
        unstable {
            echo 'Build has gone unstable'
        }
    }
}
