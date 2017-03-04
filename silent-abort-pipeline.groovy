/**
 * Tracking if the build was aborted
 */
Boolean buildAborted = false

/**
 * Abort the build with a message
 */
def abortBuild = { String abortMessage ->
    buildAborted = true
    error(abortMessage)
}

/**
 * A pipeline that can abort without failing. Try-catch makes it a bit messy. A native abort() step
 * similar to error() that does not fail the build would make it easier, but does not fit the Jenkins model
 * I think. The error status cannot be cleared without a try-catch wrapper around the code becuase the 
 * Jenkins Run model does not support downgrading the currentBuild.result from FAILURE to SUCCESS, a fail
 * will always stick.
 */
pipeline {

    agent any

    parameters {
        string(name: 'FailOrAbort', defaultValue: 'ok', description: "Enter 'fail','abort' or 'ok'")
    }

    stages {

        stage('One') {

            steps {

                echo "FailOrAbort = ${params.FailOrAbort}"

                script {

                    try {

                        echo 'Doing stage 1'

                        if(params.FailOrAbort == 'fail') {

                            echo "This build will fail"
                            error("Build has failed")

                        }
                        else if(params.FailOrAbort == 'abort') {

                            echo "This build will abort with SUCCESS status"
                            abortBuild("This build was aborted")

                        }
                        else {

                            echo "This build is a success"

                        }

                        echo "Stage one steps..."

                    }
                    catch(e) {

                        echo "Error in Stage 1: ${e.getMessage()}"

                        if(buildAborted) {
                            echo "It was aborted, ignoring error status"
                        }
                        else {
                            error(e.getMessage())
                        }
                    }
                }
            }

            post {
                failure {
                    echo "Stage 1 failed"
                }
            }
        }

        stage('Two') {

            when {
                expression {
                    return !buildAborted
                }
            }

            steps {
                echo "Doing stage 2"
            }
        }

        stage('Three') {

            when {
                expression {
                    return !buildAborted
                }
            }

            steps {
                echo "Doing stage 3"
            }
        }
    }

    post {
        always  {
            echo "Build completed. currentBuild.result = ${currentBuild.result}"
        }
        failure {
            echo "Build failed"
        }
        success {
            script {
                if(buildAborted) {
                    echo "Build was aborted"
                } else {
                    echo 'Build was a complete success'
                }
            }
        }
        unstable {
            echo 'Build has gone unstable'
        }
    }
}
