FROM java:8
MAINTAINER LEE
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
VOLUME /tmp
ADD target/bookstore-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
