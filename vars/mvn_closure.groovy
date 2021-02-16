def call(body) {
  // evaluate the body block, and collect configuration into the object
  def pipelineParams= [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  def LOG_LEVEL = '0'

  pipeline {
    agent {
      kubernetes {
        cloud "${pipelineParams.my_cloud}"
	label "Test-${pipelineParams.my_cloud}"
	yaml """\
        apiVersion: v1
        kind: Pod
        metadata:
          labels:
            some-label: some-label-value
        spec:
          containers:
          - name: maven
            image: maven:alpine
            command:
            - cat
            tty: true
          - name: busybox
            image: busybox
            command:
            - cat
            tty: true
        """.stripIndent()
      }
    }
    
    stages {
      stage ('Check out Repo') {
        steps {
          container('maven') {
            sh "mkdir mvn-build"
	    dir ("mvn-build") {
              script {
                git url: ${pipelineParams.git_url}
	      }
	    }
	  }
        }
      }

      stage ('Run Build'){
        steps {
	  container('maven') {
            dir ("mvn-build") {
              sh "mvn clean install"
	    }
	  }
        }
      }
    }
  }
}
