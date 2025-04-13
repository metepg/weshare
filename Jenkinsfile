pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_OR_TAG', defaultValue: 'main', description: 'Branch or tag')
    }

    stages {
        stage('Clone') {
            steps {
                checkout([$class: 'GitSCM',
                  branches: [[name: "${params.BRANCH_OR_TAG}"]],
                  userRemoteConfigs: [[url: 'https://github.com/metepg/weshare.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
