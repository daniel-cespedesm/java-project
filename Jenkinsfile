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
        sh "mkdir /var/www/html/rectangles/all/${env.BRANCH_NAME}"
        sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}"
      }
    }

    stage ('Running on CentOS'){
      agent {label 'CentOS'}
      steps{
        sh "wget http://54.147.40.92/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 10 20"
      }
    }

    stage("Test on Debian"){
      agent{
        docker {
          image 'openjdk:8u121-jre'
          label 'master'
        }
      }
      steps {
        sh "wget http://54.147.40.92/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 20 25"
      }
    }

    stage ("Promote to green"){
      agent {label 'apache'}
      when {
        branch 'master'
      }
      steps{
        sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/"
      }
    }
    stage("promote development branch to master"){
      agent {label 'apache'}
      when {
        branch 'development'
      }
      steps {
        echo "Stashing Any Local Changes"
        sh "git stash"
        echo "Checking Out Development Branch"
        sh "git checkout development"
        sh 'git pull origin'
        echo "Checking Out Master Branch"
        sh "git checkout master"
        echo "Merging Development into Master Branch"
        sh "git merge development"
        echo "Pushing to Origin Master"
        sh "git push -f origin master"
      }
    }
  }
}
