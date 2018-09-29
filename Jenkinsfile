Jenkinsfile (Declarative Pipeline)
pipeline {
    agent any

    stages {
    def mvnHome
       stage('Preparation') { // for display purposes
          // Get some code from a GitHub repository
          git branch:'develop', url:'https://github.com/edwinbylu/lby-fix-engine.git'
          // Get the Maven tool.
          // ** NOTE: This 'M3' Maven tool must be configured
          // **       in the global configuration.
          mvnHome = tool 'M3'
       }
        stage('Build') {
              // Run the maven build
              if (isUnix()) {
                 sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
              } else {
                 bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
              }
           }
           stage('Results') {
              junit '**/target/surefire-reports/TEST-*.xml'
              archive 'target/*.jar'
           }
    }
}