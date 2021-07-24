pipeline {
    agent any
    environment {
        SONARQUBE_LOGIN = credentials('sonarqube-token')
        TELEGRAM_TOKEN = credentials('telegram-token')
        TELEGRAM_CHAT_ID = credentials('telegram-chat-id')
    }
    options { 
        disableConcurrentBuilds() 
        
    }
    triggers {
        pollSCM 'H/5 * * * *'
    }
    stages {
        stage('Compile') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    sh './gradlew -Dorg.gradle.java.home=$JAVA_HOME clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    sh './gradlew -Dorg.gradle.java.home=$JAVA_HOME check jacocoTestReport'
                }
            }
        }
        stage('Sonar Analysis') {
            steps {
                script {
                    sh '''
                    ./gradlew \
                    sonarqube \
                    -Dorg.gradle.java.home=$JAVA_HOME \
                    -Dsonar.projectKey=TeaMemory \
                    -Dsonar.host.url=http://192.168.2.108:9000 \
                    -Dsonar.login=$SONARQUBE_LOGIN \
                    -Dsonar.language=java \
                    -Dsonar.java.binaries=**/javac/debug/classes \
                    -Dsonar.coverage.jacoco.xmlReportPaths=**/reports/jacocoTestReport/jacocoTestReport.xml
                    '''
                }
            }
        }
    }
    post { 
        success {
            script {
                sh 'curl -s -X POST https://api.telegram.org/bot$TELEGRAM_TOKEN/sendMessage -d chat_id=$TELEGRAM_CHAT_ID -d text="TeaMemory successfully build \n [SonarQube](http://192.168.2.108:9000/dashboard?id=TeaMemory) \n [Jenkins build]($BUILD_URL)"'
            }
        }
        failure {
            script {
                sh 'curl -s -X POST https://api.telegram.org/bot$TELEGRAM_TOKEN/sendMessage -d chat_id=$TELEGRAM_CHAT_ID -d text="TeaMemory failed to build \n [Jenkins build]($BUILD_URL)"'
            }
        }
        always { 
            cleanWs()
        }
    }
}
