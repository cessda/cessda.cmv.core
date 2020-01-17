pipeline {
    options {
        buildDiscarder logRotator(artifactNumToKeepStr: '5', numToKeepStr: '10')
    }

    environment {
        product_name = "cmv"
		component_name = "core"
	}

	agent any

	stages {
		// Building on master
		stage('Build Project') {
			steps {
				withMaven {
					sh "mvn clean install"
				}
			}
			when { branch 'master' }
		}
        // Not running on master - test only (for PRs and integration branches)
		stage('Test Project') {
			steps {
                withMaven {
                    sh 'mvn clean test'
				}
			}
            when { not { branch 'master' } }
		}
		stage('Record Issues') {
			steps {
				recordIssues(tools: [java()])
			}
		}
		stage('Run Sonar Scan') {
            steps {
                withSonarQubeEnv('cessda-sonar') {
                    withMaven {
                        sh "mvn sonar:sonar"
                    }
                }
            }
            when { branch 'master' }
        }
        stage("Get Sonar Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: false
                }
            }
            when { branch 'master' }
        }
		stage('Deploy Project') {
			steps {
				withMaven {
					sh "mvn jar:jar deploy:deploy"
				}
			}
			when { branch 'master' }
		}
	}
}