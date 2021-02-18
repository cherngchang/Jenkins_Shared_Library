def call(body) {
  // evaluate the body block, and collect configuration into the object
  def pipelineParams= [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  //def LOG_LEVEL = '0'

  pipeline {
    agent {
      kubernetes {
        cloud "${pipelineParams.my_cloud}"
	//label "TestSharedLib"
	idleMinutes 5
	yaml libraryResource('podtemplates/build-pod.yaml')
	defaultContainer 'maven'
      }
    }
    
    stages {
      stage ('Checkout Application Repo') {
        steps {
          container('maven') {
            script {
              git url: "${pipelineParams.git_url}"
	    }
	  }
	}
      }

      stage ('Run Build'){
        steps {
	  container('maven') {
            sh "mvn clean package"
	  }
        }
      }

      stage('Build Docker Image') {
        steps {
          container('docker') {
            script {
              docker.withRegistry( '', 'dockerhub' ) { //dockerhub is the credential id
                def dockerImage = docker.build("changking/testsharedlib:dev")
                dockerImage.push()
              }
            }
	  }
	}
      }
   /* post {
      success {
        container('jnlp') {
	  script {
	    currentBuild.keepLog = true
	    echo "Success Build"
	  }
	}
      }*/
    }
  }
}
