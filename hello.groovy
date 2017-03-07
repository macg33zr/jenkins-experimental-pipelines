/**
 *
 * To use this:
 *
 * 0) Review my GitHub repos mentioned below and decide that you are happy to pull this onto your Jenkins server.
 *
 * 1) On a Folder in Jenkins or globally configure a shared pipeline library pointing to this GitHub repo:
 *  https://github.com/macg33zr/jenkins-pipeline-experiments.git
 *
 * 2) Create a Jenkins Pipeline job pulling in the file hello.groovy from this repo:
 *  https://github.com/macg33zr/jenkins-experimental-pipelines.git
 *
 */
pipeline {

    // All stages will run on any node
    agent any

    stages {
        stage('Hello') {
            steps {
                script {

                    // Invokes the helloTest global var (https://github.com/macg33zr/jenkins-pipeline-experiments/blob/master/vars/helloTest.groovy)
                    helloTest {
                        message = "hello this is a test"
                    }
                }
            }
        }
    }
}
