pipeline {

    // No node allocated to stages automatically
    agent none

    stages {

        // This stage does not use a node
        stage('NoNode') {

            steps {

                echo "env.NODE_NAME = ${env.NODE_NAME}"

                script {

                    // This should allocate a node to do the work
                    nodeWrapper {

                    }
                }
            }
        }

        // This stage runs on any node
        stage('AnyNode') {

            agent label: ""

            steps {

                echo "env.NODE_NAME = ${env.NODE_NAME}"

                script {

                    // This should not allocate a node.
                    nodeWrapper {

                    }
                }
            }
        }
    }
}