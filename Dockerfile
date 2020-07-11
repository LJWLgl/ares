FROM tomcat:8-jre8
MAINTAINER "GanZhiQiang <2715815264@qq.com>"

WORKDIR /usr/local/tomcat/webapps

RUN mkdir -p /gzq/config/ares
ADD target/ares.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
