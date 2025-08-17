pipeline{

    agent { label "proworker" }

    environment{
        CAPROVER_URL="https://captain.dev.sgaurav.me"
        CAPROVER_APP_NAME="admin-service-gateway"
    }

    stages{
        stage("Cloning the code...."){
            steps{
                git credentialsId: 'github-creds', url: 'https://github.com/sahityagaurav4210/api-gateway.git', branch: 'master'
                echo "Code clone was successful 👍👍👍👍"
            }
        }

        stage("Setting up the deployment environment..."){
            steps{
                sh "cp /root/deployment-files/admin-service-gateway.yml ./src/main/resources/application-dev.yml"
                echo "Deployment environment is ready 🚀🚀🚀🚀"
            }
        }

        stage("Building the docker image...."){
            steps{
                sh "docker build -t sgauravdev/admin-service-gateway ."
                echo "Build was successful 👍👍👍👍"
            }
        }

        stage("Pushing the build....."){
            steps{
               withCredentials([usernamePassword(credentialsId:"docker-creds",passwordVariable:"dockerCredPwd",usernameVariable:"dockerCredUsr")]){
                    sh "docker login -u ${env.dockerCredUsr} -p ${env.dockerCredPwd}"
                    sh "docker push ${env.dockerCredUsr}/admin-service-gateway"

                    echo "Build pushed successfully 👍👍👍👍"
               }
            }
        }

        stage("Deploying the code....."){
            steps{
                withCredentials([usernamePassword(credentialsId:"caprover-creds",passwordVariable:"caproverCredPwd",usernameVariable:"caproverCredUsr")]){
                    withCredentials([usernamePassword(credentialsId:"docker-creds",passwordVariable:"dockerCredPwd",usernameVariable:"dockerCredUsr")]){
                        sh '''
                            export PATH=/root/.nvm/versions/node/v22.17.0/bin:$PATH
                            caprover deploy \
                            -h "$CAPROVER_URL" \
                            -p "$caproverCredPwd" \
                            -i "$dockerCredUsr/admin-service-gateway" \
                            --appName "$CAPROVER_APP_NAME"
                        '''
                        echo "App deployed successfully and is available at ${env.CAPROVER_URL} 🚀🚀🚀🚀"
                    }
                }
            }
        }
    }
}