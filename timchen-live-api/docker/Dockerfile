# Use the OpenJDK 17 Alpine base image

FROM openjdk:17-jdk-alpine

# Define a mount point with the specified path and mark it as a volume

VOLUME /tmp

# Copy the JAR file from the host to the container
COPY /arthas-bin.zip /opts/arthas-bin.zip
ADD timchen-live-api-docker.jar app.jar

# Set environment variables for Java options

ENV JAVA_OPTS="-server -Xmx1g -Xms1g -Xmn256m"

# Define the entry point for the container to run the Java application

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.math=ALL-UNNAMED -jar app.jar