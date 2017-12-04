FROM frolvlad/alpine-oraclejdk8:slim
RUN apk add --no-cache bash
VOLUME /tmp
ADD customer-analytics-1.0.jar app.jar
ADD wait-for-it.sh /wait-for-it.sh
RUN chmod 777 /wait-for-it.sh
RUN sh -c 'touch /app.jar'
RUN sh -c 'touch /wait-for-it.sh'
VOLUME ["/var/log/app"]
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]