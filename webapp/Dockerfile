FROM maven:3.6.3-jdk-11 as maven_builder
WORKDIR /webapp
COPY pom.xml .
RUN mvn clean package
COPY src ./src
RUN mvn clean package 
FROM tomcat:9.0.39
COPY --from=maven_builder /webapp/target/graphqlexample.war $CATALINA_HOME/webapps/graphqlexample.war

