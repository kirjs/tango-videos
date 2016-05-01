FROM tomcat:jre8

RUN apt-get update && \
	apt-get install -y vim && \
	apt-get clean 

ENV CATALINA_HOME /usr/local/tomcat

# Should be a helios-api-bundle.war copy at the Dockerfile path
RUN rm -rf $CATALINA_HOME/webapps/ROOT
COPY ./target/tango-videos-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war

# TODO: Use volumes instead of just copying the files
COPY neo4jDb12/ /usr/local/tomcat/bin/neo4jDb12/

# Create a temp config with preconfigured port
RUN sed "s/8080/8087/g" < conf/server.xml > /tmp/tomcat.xml

EXPOSE 8087

CMD ["catalina.sh", "run", "-config", "/tmp/tomcat.xml"]