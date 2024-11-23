pipeline {
    agent any

   environment {
          mavenHome = tool 'jenkins-maven' // Ensure this matches the configured Maven name
          PATH = "${mavenHome}/bin:${env.PATH}" // Add Maven to PATH
      }

    tools {
        jdk 'java-17' // JDK tool name configured in Jenkins
    }

    stages {
        stage('Build') {
            steps {
                bat "mvn clean verify"
            }
        }

        stage('Test') {
            steps {
                bat "mvn test"
            }
        }

        stage('Deploy') {
            steps {
                bat "mvn spring-boot:run"
            }
        }
    }
}
