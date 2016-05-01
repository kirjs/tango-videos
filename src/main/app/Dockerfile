FROM node:4.2

ENV PORT 8084
ENV TANGO_VIDEOS_REST http://tango-video-rest:8087/
ENV TANGO_VIDEOS_PORT 80

ADD ./ /tango-videos-ui
WORKDIR /tango-videos-ui

EXPOSE 8084

RUN npm install --unsafe-perm

CMD npm start
