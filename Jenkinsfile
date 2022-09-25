pipeline {
  agent {
    label 'node-awsec2-docker'
  }
  environment {
    gitcommit = "${gitcommit}"
  }
  tools {
    maven 'maven-jenkins'
  }
  stages {
    stage("Build") {
      steps {
        configFileProvider([configFile(fileId: '9a904863-5c8a-4a8f-a39a-fdb501efe48c', variable: 'MAVEN_SETTINGS_XML')]) {
          sh 'mvn -s $MAVEN_SETTINGS_XML -DskipTests clean package'
        }
      }
    }
    stage("Test") {
      steps {
        configFileProvider([configFile(fileId: '9a904863-5c8a-4a8f-a39a-fdb501efe48c', variable: 'MAVEN_SETTINGS_XML')]) {
          sh 'mvn -s $MAVEN_SETTINGS_XML test'
        }
      }
    }
    stage('Scan & Quality Gate'){
      steps{
        withSonarQubeEnv(installationName: 'SonarQubeServer') {
          sh 'mvn clean package sonar:sonar'
        }
        timeout(time: 6, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
    stage('Docker Build & Push') {
      steps {
        script {
          sh "git rev-parse --short HEAD > .git/commit-id"
          gitcommit = readFile('.git/commit-id').trim()
          docker.withRegistry('https://registry.hub.docker.com', 'docker-hub') {
            def image = docker.build("plchavez98/tds-microservice-products:${gitcommit}", ".")
            image.push()
          }
        }
      }
    }
  }
}