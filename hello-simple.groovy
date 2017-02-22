pipeline {

    // All stages will run on any node
    agent any

    stages {
        stage('Hello') {
            steps {             
              echo "Hello"
                echo "env.GIT_URL = '${env.GIT_URL}'"
            }
        }
        stage('World') {
            steps {
              echo "World"
            }
        }        
    }
}
