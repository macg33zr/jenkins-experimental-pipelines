/**
 * Example of running 2 pipeline declarations in the same Job
 *
 * Why? You might want an approval section that does not tie up a node or some script that runs outside any
 * pipeline declaration without using an executor.
 *
 * It does seem to be a loophole to allow multiple pipelines in a single job
 *
 */


// First pipeline declaration
pipeline {

    // Run on any node
    agent any

    stages {
        stage('Section 1') {
            steps {
                echo "Section 1 pipeline"

                // This will tie up a node. Look at executors while waiting.
                input message: 'Wait for process', ok: 'Done waiting'

            }
        }
    }
}

// This does not tie up a nodeLook at executors while waiting.
stage('Approval') {
    echo "Approval stage"
    input message: 'Approve next section', ok: 'Approve'
}

// Second pipelne declaration
pipeline {

    // Run on any node
    agent any

    stages {
        stage('Section 2') {
            steps {
                echo "Pipeline section 2"

            }
        }
    }
}
