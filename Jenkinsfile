pipeline{

    agent { label "proworker" }

    environment{
        CAPROVER_URL="https://captain.dev.sgaurav.me"
        CAPROVER_APP_NAME="admin-service-gateway"
    }

    stages{
        stage("Cloner"){
            steps{
                git credentialsId: 'github-creds', url: 'https://github.com/sahityagaurav4210/api-gateway.git', branch: env.BRANCH_NAME
                echo "Code clone was successful 👍👍👍👍"
            }
        }

        stage("Configurer"){
            steps{
                sh "mkdir -p ./src/main/resources"
                sh "cp /root/deployment-files/admin-service-gateway.yml ./src/main/resources/application-dev.yml"
                echo "Deployment environment is ready 🚀🚀🚀🚀"
            }
        }

        stage("Cleaner"){
            when {
                not {
                    anyOf {
                        branch 'master'
                        branch 'dev'
                    }
                }
            }

            steps{
                sh "chmod 777 ./mvnw"
                sh "./mvnw clean"
                echo "Deployment environment is ready 🚀🚀🚀🚀"
            }
        }

        stage("Installer"){
            when {
                not {
                    anyOf {
                        branch 'master'
                        branch 'dev'
                    }
                }
            }

            steps{
                sh "./mvnw install"
                echo "Deployment environment is ready 🚀🚀🚀🚀"
            }
        }

        stage("Builder"){
            when {
                anyOf {
                    branch 'dev'
                    branch 'master'
                }
            }

            steps{
                sh "docker build -t sgauravdev/admin-service-gateway ."
                echo "Build was successful 👍👍👍👍"
            }
        }

        stage("Pusher"){
            when {
                anyOf {
                    branch 'dev'
                    branch 'master'
                }
            }

            steps{
               withCredentials([usernamePassword(credentialsId:"docker-creds",passwordVariable:"dockerCredPwd",usernameVariable:"dockerCredUsr")]){
                    sh "docker login -u ${env.dockerCredUsr} -p ${env.dockerCredPwd}"
                    sh "docker push ${env.dockerCredUsr}/admin-service-gateway"

                    echo "Build pushed successfully 👍👍👍👍"
               }
            }
        }

        stage("Deployer"){
            when {
                anyOf {
                    branch 'dev'
                    branch 'master'
                }
            }

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

    post {
        success {
            echo "${env.BRANCH_NAME.toUpperCase()} branch is live now"
        }

        failure {
            echo 'Admin service gateway pipeline has failed'
        }
    }
}