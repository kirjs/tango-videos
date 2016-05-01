# This is just a helper file.

# Restart docker machine
docker-machine restart default      # Restart the environment
eval $(docker-machine env default)  # Refresh your environment settings


# Create cluster
gcloud container clusters create tango-videos

# Deploy docker-services
docker build -t gcr.io/tango-videos/tango-video-rest:v10 .
docker run -d -p 8088:8087 gcr.io/tango-videos/tango-video-rest:v10
gcloud docker push gcr.io/tango-videos/tango-video-rest:v10
kubectl run tango-video-rest --image=gcr.io/tango-videos/tango-video-rest:v10 --port=8088
kubectl expose deployment tango-video-rest --port=8087 --type="LoadBalancer"
kubectl get service tango-video-rest

# Deploy docker UI
docker build -t gcr.io/tango-videos/tango-videos-ui:v4 .
# docker run -d -p 8084:8084 gcr.io/tango-videos/tango-videos-ui:v4
gcloud docker push gcr.io/tango-videos/tango-videos-ui:v4

kubectl run tango-video-ui --image=gcr.io/tango-videos/tango-videos-ui:v4 --port=8084
kubectl expose deployment tango-video-ui --port=8084 --type="LoadBalancer"
kubectl get service tango-video-ui

