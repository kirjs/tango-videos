# Tango videos

# Developing
3 Steps to run the app:

## 1. Copy the db to tomcat folder
cp -r ./db/neo4jDb12/ /usr/local/tomcat/bin/neo4jDb12

## 2. Run the back-end
1. Install TomCat http://tomcat.apache.org/download-90.cgi
2. Run `mvn package`
3. Run tango-videos.war-exploded on port 8087


## 3. Run the UI
```bash
(cd src/main/app && npm install && npm start)
```
go to https://localhost:8087/


