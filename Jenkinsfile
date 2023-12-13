pipeline {
    options {
        buildDiscarder logRotator(artifactNumToKeepStr: '5', numToKeepStr: '10')
    }

    environment {
        product_name = "cmv"
		component_name = "core"
	}

	agent {
		label 'jnlp-himem'
	}

	stages {
		stage('Pull SDK Docker Image') {
			agent {
				docker {
					image 'openjdk:8'
					reuseNode true
				}
			}
			stages {
				// Building on master
				stage('Build Project') {
					steps {
						withMaven {
							sh './mvnw clean install -U'
						}
					}
					when { branch 'main' }
				}
				// Not running on master - test only (for PRs and integration branches)
				stage('Test Project') {
					steps {
						withMaven {
							sh './mvnw clean test'
						}
					}
					when { not { branch 'main' } }
				}
			}
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
						sh './mvnw sonar:sonar'
					}
				}
				timeout(time: 1, unit: 'HOURS') {
					waitForQualityGate abortPipeline: true
				}
			}
			when { branch 'main' }
		}
		stage('Deploy Project') {
			agent {
				docker {
					image 'openjdk:8'
					reuseNode true
				}
			}
			steps {
				withMaven {
					sh './mvnw jar:jar javadoc:jar source:jar deploy:deploy'
				}
			}
			when {
				anyOf {
					branch 'main'
					buildingTag()
				}
			}
		}
	}
}
