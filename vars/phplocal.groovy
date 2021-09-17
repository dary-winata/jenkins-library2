#!/usr/bin/env groovy

def call(Map param) {
    pipeline {
        agent {
            label "${param.node}"
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage('Build image') {
                steps {
                    sh "mv target/*.jar ${param.nama_file}"
                }
            }
            stage('Run app') {
                steps {
                    sh "java -jar ${param.nama_file}"
                }
            }
        }
        post {
            always {
                deleteDir()
            }
        }
    }
}