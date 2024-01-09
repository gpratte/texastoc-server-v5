# export DB=[h2|mysql]
# mvn -P $DB -pl server clean package
# docker build -t texastoc-v4-$DB-image .
FROM openjdk:17-alpine
EXPOSE 8080
COPY server/target/texastoc-server-v5-1.0.0.jar /server/
ENTRYPOINT ["java"]
CMD [ "-jar", \
    "/server/texastoc-server-v5-1.0.0.jar" \
]
