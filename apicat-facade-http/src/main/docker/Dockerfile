FROM openjdk:8-jdk

RUN apt-get update && apt-get install -y dos2unix

VOLUME /tmp

ENV VERSION v0.3.0
ENV DOWNLOAD_URL https://github.com/jwilder/dockerize/releases/download/$VERSION/dockerize-alpine-linux-amd64-$VERSION.tar.gz

RUN wget -qO- $DOWNLOAD_URL | tar xvz -C /usr/local/bin

COPY entrypoint.sh /

RUN dos2unix /entrypoint.sh && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

COPY apicat-web-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'

ENTRYPOINT ["/entrypoint.sh"]