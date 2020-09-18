def call(body) {
  // evaluate the body block, and collect configuration into the object
  def pipelineParams= [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  pipeline {
    agent any
    
    stages ('deploy dev'){
      steps {
        echo "deploying to ${pipelineParams.devServer} port ${pipelineParams.devPort}"
      }
    }

    stages ('deploy uat'){
      steps {
        echo "deploying to ${pipelineParams.uatServer} port ${pipelineParams.uatPort}"
      }
    }

    stages ('deploy prod'){
      steps {
        echo "deploying to ${pipelineParams.prodServer} port ${pipelineParams.prodPort}"
      }
    }
  }
}
