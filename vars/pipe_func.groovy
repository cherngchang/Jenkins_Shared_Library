def call(Map pipelineParams) {
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
