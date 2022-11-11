pipeline {
  agent any
  options {
    buildDiscarder(logRotator(artifactDaysToKeepStr:"", artifactNumToKeepStr: "5", daysToKeepStr: "", numToKeepStr: "5"))
    disableConcurrentBuilds()
  }
  environment {
    gitcommit = "${gitcommit}"
  }
  tools {
    maven "maven-jenkins"
  }
  stages {
    stage("Build") {
      steps {
        configFileProvider([configFile(fileId: "fcf30dba-9aae-4f37-b7a7-c7ccb6ab9219", variable: "MAVEN_SETTINGS_XML")]) {
          sh "mvn -s $MAVEN_SETTINGS_XML -DskipTests clean package"
        }
      }
    }
    stage("Test") {
      steps {
        configFileProvider([configFile(fileId: "fcf30dba-9aae-4f37-b7a7-c7ccb6ab9219", variable: "MAVEN_SETTINGS_XML")]) {
          sh "mvn -s $MAVEN_SETTINGS_XML test"
        }
      }
    }
    stage("Scan & Quality Gate"){
      steps{
        withSonarQubeEnv(installationName: "SonarQubeServer") {
          sh "mvn clean package sonar:sonar"
        }
        timeout(time: 2, unit: "MINUTES") {
          waitForQualityGate abortPipeline: false
        }
      }
    }
    stage("Docker Build & Push") {
      when {
        branch "develop"
      }
      steps {
        script {
          sh "git rev-parse --short HEAD > .git/commit-id"
          gitcommit = readFile(".git/commit-id").trim()

          def app = docker.build("plchavez98/tds-microservice-suppliers")

          docker.withRegistry("https://registry.hub.docker.com", "docker-hub") {
            app.push("${gitcommit}")
            app.push("latest")
          }
        }
      }
    }
  }
  post {
    success {
      slackSend message: "Build successfully - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
    }
    failure {
      slackSend message: "Build failed - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
    }
  }
}