def call(Map pipelineParams) {
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
