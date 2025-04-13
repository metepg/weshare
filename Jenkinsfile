pipeline {
    agent any

    tools {
        maven '3.9.9'
    }

    stages {
        stage('Clone') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: "${BRANCH_OR_TAG}"]],
                          userRemoteConfigs: [[url: 'https://github.com/metepg/weshare.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                dir('server') {
                    sh 'mvn clean package'
                }
            }
        }
    }
}
