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
        stage('Build') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    sh '''
                    ./gradlew \
                    -Dorg.gradle.java.home=$JAVA_HOME \
                    clean \
                    test \
                    jacocoTestReport
                    '''
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
                    -Dsonar.projectKey=TestProject1 \
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
