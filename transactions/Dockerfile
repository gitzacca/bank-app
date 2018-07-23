FROM java:8

VOLUME /tmp

EXPOSE 8080

ADD /build/libs/transactions-0.0.1-SNAPSHOT.jar transactions-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","transactions-0.0.1-SNAPSHOT.jar"]