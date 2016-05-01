#!/bin/bash
TIME=$(date +%s)
DEPLOYMENT_NAME=tango-videos-ui:v$TIME
PORT=80

cd ../src/main/app

docker build -t gcr.io/tango-videos/$DEPLOYMENT_NAME .
# docker run -d -p 8084:$PORT gcr.io/tango-videos/$DEPLOYMENT_NAME
# docker run -d -p 8084:80 gcr.io/tango-videos/tango-videos-ui:v1462138058

gcloud docker push gcr.io/tango-videos/$DEPLOYMENT_NAME

#create a deployment
kubectl delete deployment tango-video-ui
kubectl run tango-video-ui --image=gcr.io/tango-videos/$DEPLOYMENT_NAME --port=$PORT

#create a port
kubectl delete service tango-video-ui
kubectl expose deployment tango-video-ui --port=$PORT --type="LoadBalancer"

# Get the IP
kubectl get service tango-video-ui
echo "kubectl get service tango-video-ui"

echo gcr.io/tango-videos/$DEPLOYMENT_NAME
