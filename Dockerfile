# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
# cache deps
RUN mvn -B -DskipTests dependency:go-offline
COPY . .
RUN mvn -B -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
# install small tools (pg_isready) and tini for signal handling
USER root
RUN apt-get update && apt-get install -y --no-install-recommends \
    postgresql-client \
    tini \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /workspace/target/*jar /app/app.jar

# copy wait script
COPY scripts /app/scripts
RUN chmod +x /app/scripts/*.sh

ENV JAVA_OPTS=""

# create non-root user (optional but recommended)
RUN useradd -m -d /home/appuser appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

# use tini to forward signals; exec form avoids extra shell
ENTRYPOINT ["/usr/bin/tini", "--"]
CMD [ "sh", "-c", "/app/scripts/wait-for-db.sh && exec java $JAVA_OPTS -jar /app/app.jar" ]
