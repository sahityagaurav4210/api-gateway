FROM openjdk:21

WORKDIR /app

COPY . .

RUN ./mvnw clean
RUN ./mvnw install


RUN rm -rf src .gitignore .gitattributes

EXPOSE 7779
CMD ["java","-jar","target/admin-service-gateway.jar","--spring.profiles.active=dev"]