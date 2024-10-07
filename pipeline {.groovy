pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    // Change to the specified directory and checkout the Automation branch
                    dir('/home/akshay/Akshay') {
                        sh 'git checkout Automation'
                        withCredentials([usernamePassword(credentialsId: 'fc7d4413-495d-4057-9752-b4ef33f918ea', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                            sh 'git push -u https://${USER}:${PASS}@github.com/Akshaay7/Nikita.git Automation'
                        }
                        
                    }
                }
            }
        }

        stage('Access Docker Containers') {
            steps {
                script {
                    // Access Docker container 1
                    sh '''
                        docker exec -it container1 bash -c "
                        mkdir -p /home/akshay/Akshay && 
                        cd /home/akshay/Akshay && 
                        git checkout Automation && 
                        git pull origin Automation
                        "
                    '''

                    // Access Docker container 2
                    sh '''
                        docker exec -it container2 bash -c "
                        mkdir -p /home/akshay/Akshay && 
                        cd /home/akshay/Akshay && 
                        git checkout Automation && 
                        git pull origin Automation
                        "
                    '''
                }
            }
        }

        stage('Run Java Applications') {
            steps {
                script {
                    // Run the JAR files in both containers
                    sh '''
                        docker exec -d container1 java -jar /home/akshay/Akshay/Akshay.jar
                        docker exec -d container2 java -jar /home/akshay/Akshay/Tarun.jar
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
