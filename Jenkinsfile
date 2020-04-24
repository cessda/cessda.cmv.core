pipeline {
    options {
        buildDiscarder logRotator(artifactNumToKeepStr: '5', numToKeepStr: '10')
    }

    environment {
        product_name = "cmv"
		component_name = "core"
	}

	agent {
        docker {
            image 'maven:3-jdk-8'
        }
    }

	stages {
		// Building on master
		stage('Build Project') {
			steps {
				withMaven {
					sh "$MVN_CMD clean install -U"
				}
			}
			when { branch 'master' }
		}
        // Not running on master - test only (for PRs and integration branches)
		stage('Test Project') {
			steps {
                withMaven {
                    sh '$MVN_CMD clean test'
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
                        sh "$MVN_CMD sonar:sonar"
                    }
                }
            }
            when { branch 'master' }
        }
        stage("Get Sonar Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
            when { branch 'master' }
        }
		stage('Deploy Project') {
			steps {
				withMaven {
					sh "$MVN_CMD jar:jar javadoc:jar source:jar deploy:deploy"
				}
			}
			when { branch 'master' }
		}
	}
}