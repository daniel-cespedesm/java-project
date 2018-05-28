pipeline {
  agent none

  stages {

    stage('Unit Tests') {
      agent {label 'apache'}
      steps{
        sh 'ant -f test.xml -v'
        junit 'reports/result.xml'
      }
    }

    stage('build') {
      agent {label 'apache'}
      steps{
        sh 'ant -f build.xml -v'
      }
      post {
        success {
          archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
        }
      }
    }

    stage ('deploy'){
      agent {label 'apache'}
      steps{
        sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/"
      }
    }

    stage ('Running on CentOS'){
      agent {label 'CentOS'}
      steps{
        sh "wget http://54.147.40.92/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 10 20"
      }
    }

    stage("Test on Debian"){
      agent{
        docker 'openjdk:8u121-jre'
        label 'master'
      }
      steps {
        sh "wget http://54.147.40.92/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 20 25"
      }
    }

    stage ("Promote to green"){
      agent {label 'master'}
      steps{
        sh "cp /var/www/html/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/"
      }
    }

  }
}
