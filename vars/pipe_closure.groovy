def call(body) {
  // evaluate the body block, and collect configuration into the object
  def pipelineParams= [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  pipeline {
    agent any
    
    stages {
      stage ('deploy dev'){
        steps {
          echo "deploying to ${pipelineParams.devServer} port ${pipelineParams.devPort}"
        }
      }

      stage ('deploy uat'){
        steps {
          echo "deploying to ${pipelineParams.uatServer} port ${pipelineParams.uatPort}"
        }
      }

      stage ('deploy prod'){
        steps {
          echo "deploying to ${pipelineParams.prodServer} port ${pipelineParams.prodPort}"
        }
      }
    }
  }
}
