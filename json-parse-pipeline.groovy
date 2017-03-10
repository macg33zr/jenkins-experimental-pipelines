import groovy.json.JsonSlurperClassic

/**
 * Parse JSON to a map.
 *
 * It needs script approvals:
 *
 *  'method groovy.json.JsonSlurperClassic parseText java.lang.String'
 *  'new groovy.json.JsonSlurperClassic'
 *  'new java.util.HashMap java.util.Map'
 *
 * Also see:
 * http://stackoverflow.com/questions/37864542/jenkins-pipeline-notserializableexception-groovy-json-internal-lazymap
 *
 * @param json Json string to parse with name / value properties
 * @return A map of properties
 */
@NonCPS
def parseJsonToMap(String json) {
    final slurper = new JsonSlurperClassic()
    return new HashMap<>(slurper.parseText(json))
}

pipeline {

    agent any

    stages {
        stage('json') {
            steps {
                script {

                    def json = "{\n" +
                                "  \"foo\":\"f00\",\n" +
                                "  \"bar\":\"baa\"\n" +
                                "}"

                    echo "Parsing JSON: ${json}"

                    def map = parseJsonToMap(json)

                    echo  "foo = ${map.foo}"
                    echo  "bar = ${map.bar}"
                }
            }
        }
    }
}
