//starting the code for job1 building the source code as container and pushing to docker hub

job('job1_pull_build'){
     scm{
             github('https://github.com/mdmustafa1/devops_task6.git')      
         } 
     
            triggers {
                upstream('seed_job2', 'SUCCESS')
                 }
    
    steps{shell('if ls | grep .html; then sudo docker build -t any0/phtml:v1 . -f html.Dockerfile; sudo docker push any0/phtml:v1;  elif ls | grep .php; then sudo docker build -t any0/php:v1 . -f php.Dockerfile; sudo docker push  any0/php:v1;  else echo "all builded"; fi')
       
       }
}


       
      
