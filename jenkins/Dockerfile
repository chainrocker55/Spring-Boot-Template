FROM harbordev.se.scb.co.th/library/adoptopenjdk/openjdk11:jre-11.0.2.9-alpine-scb as builder

WORKDIR /app

ARG JAR_FILE=./build/libs/easyinvest-da-authen-service.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

###

FROM harbordev.se.scb.co.th/library/adoptopenjdk/openjdk11:jre-11.0.2.9-alpine-scb

#TODO: Change to other user
USER root

RUN apk add --no-cache tzdata
ENV TZ=Asia/Bangkok
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /usr/src/app

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./

#RUN mkdir -p /usr/src/app && mkdir -p /usr/src/config && mkdir -p /usr/src/logs && chmod -R 755 /usr/src
#COPY ./build/libs/easyinvest-da-service.jar /usr/src/app/easyinvest-da-service.jar
#COPY ./src/main/resources/application.yml /usr/src/config/application.yml

EXPOSE 8897

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
