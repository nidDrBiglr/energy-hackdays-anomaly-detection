FROM nginx:1.17.8
RUN apt-get clean && apt-get update && apt-get install -y nano spawn-fcgi fcgiwrap wget curl
RUN rm -rf /usr/share/nginx/html/*
COPY /dist /usr/share/nginx/html
WORKDIR /usr/share/nginx
CMD nginx -g 'daemon off;'
