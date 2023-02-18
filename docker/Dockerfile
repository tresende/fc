FROM nginx:latest

WORKDIR /app

RUN apt-get update && \ 
    apt-get install -y vim

COPY html/ /usr/share/nginx/html

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]