pipeline {

    // All stages will run on any node
    agent any

    stages {
        stage('Hello') {
            steps {
              echo "Hello"
            }
        }
        stage('World') {
            steps {
              echo "World"
            }
        }        
    }
}
