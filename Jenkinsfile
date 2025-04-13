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
                 listSize          : '10'
                ]
        ])
])

String JAR_FILE
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
                    script {
                        sh 'mvn clean package'
                        def finalName = sh(script: "mvn help:evaluate -Dexpression=project.build.finalName -q -DforceStdout", returnStdout: true).trim()
                        JAR_FILE = "${finalName}.jar"
                        currentBuild.displayName = JAR_FILE
                    }
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
