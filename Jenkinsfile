properties([
        parameters([
                [$class            : 'GitParameterDefinition',
                 name              : 'BRANCH_OR_TAG',
                 type              : 'PT_BRANCH_TAG',
                 defaultValue      : 'origin/dev',
                 description       : 'Select branch or tag',
                 sortMode          : 'DESCENDING_SMART',
                 selectedValue     : 'DEFAULT',
                 quickFilterEnabled: true,
                 listSize          : '10'
                ]
        ])
])

@Library('custom')
String PACKAGE_NAME
pipeline {
    agent any

    tools {
        maven '3.9.9'
    }

    stages {

        stage('Initialize workspace') {
            steps {
                cleanWs()
            }
        }

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
                script {
                    sh 'mvn clean package'
                    PACKAGE_NAME = getPackageName()
                    currentBuild.displayName = PACKAGE_NAME
                }
                archiveArtifacts artifacts: "server/target/${PACKAGE_NAME}", fingerprint: true
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                runDependencyCheck()
            }
        }

        stage('SonarQube Analysis') {
            steps {
                runSonarQube('weshare')
            }
        }

        stage('Trigger Deploy Job') {
            steps {
                build job: 'deploy',
                        parameters: [
                                string(name: 'PACKAGE_NAME', value: "${PACKAGE_NAME}")
                        ],
                        wait: false
            }
        }

    }

}
