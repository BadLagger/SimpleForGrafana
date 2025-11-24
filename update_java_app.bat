docker build --no-cache -t spring-app:latest --build-arg GIT_REPO="https://github.com/BadLagger/SimpleForGrafana.git" .

minikube image load spring-app:latest

kubectl rollout restart deployment/spring-app -n monitoring