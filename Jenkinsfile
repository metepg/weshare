properties([
        parameters([
                [$class            : 'GitParameterDefinition',
                 name              : 'BRANCH_OR_TAG',
                 type              : 'PT_BRANCH_TAG',
                 defaultValue      : 'jenkins-test',
                 description       : 'Select branch or tag',
                 sortMode          : 'DESCENDING_SMART',
                 selectedValue     : 'DEFAULT',
                 quickFilterEnabled: true,
                 listSize          : '20'
                ]
        ])
])

pipeline {
    agent any

    tools {
        maven '3.9.9'
    }
    environment {
        NVD_API_KEY = credentials('nvd-api-key')
    }

    stages {
        stage('Clone') {
            steps {
                checkout([$class           : 'GitSCM',
                          branches         : [[name: "${BRANCH_OR_TAG}"]],
                          userRemoteConfigs: [[url: 'https://github.com/metepg/weshare.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                dir('server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('OWASP Dependency Check') {
            steps {
                dir('server') {
                    sh 'mvn dependency-check:check -DnvdApiKey=${NVD_API_KEY}'
                }
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
    }
}
