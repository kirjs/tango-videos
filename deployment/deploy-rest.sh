#!/bin/bash
TIME=$(date +%s)
DEPLOYMENT_NAME=tango-videos-rest:v$TIME

cd ../

# Temp hack to copy the database
cp -r /usr/local/tomcat/bin/neo4jDb12/ ./neo4jDb12

docker build -t gcr.io/tango-videos/$DEPLOYMENT_NAME .

# Temp hack to copy the database
rm -rf ./neo4jDb12


# docker run -d -p 8088:8087 gcr.io/tango-videos/$DEPLOYMENT_NAME
gcloud docker push gcr.io/tango-videos/$DEPLOYMENT_NAME


#create a deployment
kubectl delete deployment tango-video-rest
kubectl run tango-video-rest --image=gcr.io/tango-videos/$DEPLOYMENT_NAME

#create a port
kubectl delete service tango-video-rest
kubectl expose deployment tango-video-rest --port=8087 --type="LoadBalancer"


# Get the IP
kubectl get service tango-video-rest
echo "kubectl get service tango-video-rest"

echo gcr.io/tango-videos/$DEPLOYMENT_NAME
