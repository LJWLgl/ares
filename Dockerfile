FROM tomcat:8-jre8
MAINTAINER "GanZhiQiang <2715815264@qq.com>"

ADD target/ares.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]