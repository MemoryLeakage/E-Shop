FROM gcr.io/distroless/java:11
WORKDIR /app
COPY ./eshop-app/target/*.jar /app/eshop-app.jar
ENTRYPOINT ["java","-jar","/app/eshop-app.jar"]