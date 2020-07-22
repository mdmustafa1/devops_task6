//starting the code for job1 building the source code as container and pushing to docker hub

job('job1_pull_build'){
     scm{
             github('https://github.com/mdmustafa1/devops_task6.git')      
         }triggers{
                upstream('job2_seed','SUCCESS')
           }
    
    steps{shell('if ls | grep .html; then sudo docker build -t any0/phtml:v1 . -f html.Dockerfile; sudo docker push any0/phtml:v1;  elif ls | grep .php; then sudo docker build -t any0/php:v1 . -f php.Dockerfile; sudo docker push  any0/php:v1;  else echo "all builded"; fi')
       
       }
    
}

//code for job2 deployment on kubernetes using various resourcses
job('job2_deploy'){
     triggers{
            upstream('job1_pull_build','SUCCESS')
         }
     steps{
 shell('cd /var/lib/jenkins/workspace/job2_seed/; if (ls | grep .html)
then
if(sudo kubectl get deploy | grep htmldeploy)
then
echo "html env already running"
kubectl rollout restart deploy/htmldeploy
kubectl rollout status deploy/htmldeploy
else
sudo kubectl apply -k /task3/html
sudo kubectl expose deploy/htmldeploy --port=80  --type=NodePort
fi
fi

if (ls | grep .php)
then
if(sudo kubectl get deploy | grep phpdeploy)
then
echo "php env already running"
kubectl rollout restart deploy/phpdeploy
kubectl rollout status deploy/phpdeploy
else
sudo kubectl apply -k /task3/php
sudo kubectl expose deploy/phpdeploy --port=80 --type=NodePort
fi
fi')
        }}
        
  // job3 for monitoring the job and sending the email to developer      
       
       job('job3_test_monitor'){
     triggers{
           upstream('job1_pull_build','SUCCESS')
         }
    steps{
            shell('status=$(curl -o /dev/null -s -w "%{http_code}" 192.168.99.100:31772)
if [[ $status = 200 ]]
then
exit 0
else
exit 1
fi

status=$(curl -o /dev/null -s -w "%{http_code}" 192.168.99.100:31909)
if [[ $status = 200 ]]
then
exit 0
else
exit 1
fi')
       }
     publishers {
        extendedEmail {
            recipientList('mdmustafahusain4u@gmail.com')
    
            triggers {
     
                failure {
                    
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
    }}
