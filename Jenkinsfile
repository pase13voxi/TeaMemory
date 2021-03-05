pipeline {
    agent any
    options { 
        disableConcurrentBuilds() 
        
    }
    environment {
        SONARQUBE_LOGIN = credentials('sonarqube-token')
    }
    triggers {
        pollSCM 'H/5 * * * *'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/pase13voxi/TeaMemory.git']]])
            }
        }
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
                    sh './gradlew -Dorg.gradle.java.home=$JAVA_HOME check'
                }
            }
        }
        stage('Sonar Analysis') {
            steps {
                script {
                    sh './gradlew -Dorg.gradle.java.home=$JAVA_HOME jacocoTestReport'
                    sh '''
                    ./gradlew \
                    sonarqube \
                    -Dorg.gradle.java.home=$JAVA_HOME \
                    -Dsonar.projectKey=TeaMemory \
                    -Dsonar.host.url=http://192.168.0.147:9000 \
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
        always { 
            cleanWs()
        }
    }
}
