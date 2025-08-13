# Tên dự án Spring Boot của anh

> *Mục tiêu: README này hướng dẫn ****từ A → Z**** để ai clone repo cũng chạy được ngay, có kèm hình ảnh, snipcode, và checklist rõ ràng.*



---

## Mục lục

- [Thiết lập nhanh](#thiết-lập-nhanh)
- [Kiến trúc & Tech Stack](#kiến-trúc--tech-stack)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt công cụ (Java, Maven, Docker)](#cài-đặt-công-cụ-java-maven-docker)
- [Clone & Cấu trúc dự án](#clone--cấu-trúc-dự-án)
- [Cấu hình môi trường](#cấu-hình-môi-trường)
  - [Tạo ](#tạo-env-dùng-cho-docker-compose)[`.env`](#tạo-env-dùng-cho-docker-compose)[ (dùng cho Docker Compose)](#tạo-env-dùng-cho-docker-compose)
  - [Cấu hình ](#cấu-hình-applicationproperties-theo-profile)[`application.properties`](#cấu-hình-applicationproperties-theo-profile)[ theo profile](#cấu-hình-applicationproperties-theo-profile)
- [Database bằng Docker Compose](#database-bằng-docker-compose)
- [Chạy ứng dụng](#chạy-ứng-dụng)
  - [Chạy tất cả bằng Docker Compose](#chạy-tất-cả-bằng-docker-compose)
- [API Docs (Swagger/OpenAPI)](#api-docs-swaggeropenapi)
- [Test (JUnit5, Testcontainers)](#test-junit5-testcontainers)
---

## Thiết lập nhanh

```bash
# 0) Yêu cầu: JDK 21, Maven 3.9+, Docker

# 1) Clone
git clone https://github.com/myrng44/rs-management4.git && cd rs-management4

# 2) Spin up Postgres + pgAdmin
cp .env.example .env

# 3) Run app
Xem mục chạy app bằng Docker Compose

# 4) Mở Swagger UI
# http://localhost:8080/swagger-ui.html
```

> Ảnh:
>
> - `docs/images/docker-up.png` – Docker Compose up
> - `docs/images/swagger.png` – Swagger UI

---

## Kiến trúc & Tech Stack

- **Java 21**, **Spring Boot** (Web, Validation, Data JPA, QueryDSL, JWT)
- **PostgreSQL**, **Flyway** (migration)
- **Docker Compose** (db, pgAdmin)
- **Lombok**, **MapStruct** (tùy chọn)
- **springdoc-openapi** (Swagger UI)
- **JUnit 5**, **Testcontainers** (integration test)

> Mục tiêu kiến trúc: clean, dễ khởi chạy.

---

## Yêu cầu hệ thống

- Java **JDK 21** (bắt buộc)
- Maven **3.9+** (hoặc dùng `./mvnw` đi kèm repo)
- Docker Desktop/Engine **24+**

Kiểm tra nhanh:

```bash
java -version
mvn -version
docker version
```

---

## Cài đặt công cụ (Java, Maven, Docker)

### Windows

- Cài **Temurin JDK 21** hoặc Oracle JDK 21
- Thiết lập `JAVA_HOME` trỏ tới thư mục JDK 21
- Cài **Maven** (hoặc dùng `mvnw`), set `MAVEN_HOME` và add vào `PATH`
- Cài **Docker Desktop** và bật WSL 2 backend

### macOS

```bash
brew install --cask temurin@21
brew install maven
brew install --cask docker
```

### Linux (Ubuntu/Debian)

```bash
sudo apt-get update
sudo apt-get install -y wget git
# Cài JDK 21 (temurin hoặc openjdk tùy distro)
# Ví dụ:
sudo apt-get install -y openjdk-21-jdk
sudo apt-get install -y maven
# Docker: theo hướng dẫn chính thức (cần thêm repo Docker)
```
---

## Clone & Cấu trúc dự án

```bash
git clone https://github.com/myrng44/rs-management4.git
cd rs-management4
```

Cấu trúc tham khảo:

```
rs-management4/
├─ docs/
│  └─ images/
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/app/...
│  │  ├─ resources/
│  │  │  ├─ application.properties
│  │  │  └─ db/migration/ (Flyway)
│  └─ test/
├─ .env.example
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
└─ README.md
```

> Ảnh minh họa cấu trúc: `docs/images/tree.png`

---

## Cấu hình môi trường

### Tạo `.env` (dùng cho Docker Compose)

Tạo file `.env` từ mẫu:

```bash
cp .env.example .env
```

Ví dụ `.env.example`:

```env
# =========================
# APP / Spring Boot
# =========================
APP_NAME=your-app-name
APP_PORT=your-app-port
SPRING_PROFILES_ACTIVE=dev

# =========================
# POSTGRES / DATABASE
# =========================
# Nếu chạy DB bằng docker compose thì POSTGRES_HOST=db
# Nếu chạy DB cục bộ (local postgres) thì để localhost
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=your-database-name
POSTGRES_USER=user
POSTGRES_PASSWORD=password

# =========================
# PGADMIN (tùy chọn)
# =========================
PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin123
PGADMIN_PORT=5050

# =========================
# JAVA / JVM (tùy chọn khi chạy container app)
# =========================
JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC

# =========================
# SECURITY / JWT
# =========================
RS_SECURITY_JWT_EXPIRATION=1800
RS_SECURITY_JWT_REFRESH_EXPIRATION=3000
```

### Cấu hình `application.properties` theo profile

`src/main/resources/application.properties`:

```yaml
# =================================================
# Application identity & server
# =================================================
spring.application.name=${APP_NAME:rs-management4}
server.port=${APP_PORT:8080}
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}

# Database Configuration
## Local database
# =================================================
# Database Configuration (đọc từ biến môi trường)
# POSTGRES_HOST: có thể là 'localhost' (local dev) hoặc 'db' (docker-compose service)
# =================================================
spring.datasource.url=jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:ecommerce_db}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:mypassword}
spring.datasource.driver-class-name=org.postgresql.Driver

# =================================================
# Flyway (DB migration)
# =================================================
spring.flyway.enabled=true
spring.flyway.locations=classpath:/db/migration
logging.level.org.flywaydb=DEBUG
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=0

# =================================================
# JPA / Hibernate
# =================================================
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# =================================================
# (Optional) Allow Hibernate export DDL schema to file
# =================================================
#spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
#spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=ddl/V1__init_schema.sql
#spring.jpa.generate-ddl=true


# =================================================
# Security config (keeps your existing keys paths & jwt settings)
# =================================================
rs.security.jwt.expiration=${RS_SECURITY_JWT_EXPIRATION:1800}
rs.security.jwt.refresh-token.expiration=${RS_SECURITY_JWT_REFRESH_EXPIRATION:3000}
logging.level.org.springframework.security=DEBUG

# RSA Key paths (kept as-is from your repo; consider moving keys to env/secret manager for prod)
rs.key.public-key-file=${RS_KEY_PUBLIC_FILE:src/main/resources/keys/public.pem}
rs.key.private-key-file=${RS_KEY_PRIVATE_FILE:src/main/resources/keys/private.pem}
rs.key.secret=${RS_KEY_SECRET:jkNF544fDFfdvvdgvDvd5Vd15svd5fvg14vg14d5SDDBVFdbvd5bv1gvvg51dgVDVDv1d5vdvsvBNFNLKHSoledadSmileOpera2TheStar}

# =================================================
# Swagger / OpenAPI
# =================================================
## Specify the path of the OpenAPI documentation
springdoc.api-docs.path=/v3/api-docs
## Specify the path of the Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
## Enable or disable Swagger UI
springdoc.swagger-ui.enabled=true
##http://localhost:8080/swagger-ui/index.html

# =================================================
# API config
# =================================================
rs.api.main.baseUrl=secured/rest/v1
rs.api.main.publicUrl=public/rest/v1

# =================================================
# CORS
# =================================================
rs.cors.allowed-origins=\
http://localhost:*,\
https://ckmanrs.vercel.app,\
https://rs-man-4-production.up.railway.app,\
https://storeman4.netlify.app


```
---

## Chạy ứng dụng

### Chạy tất cả bằng Docker Compose

`Dockerfile` (multi-stage):

```dockerfile
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

```

`docker-compose.yml`:

```yaml
services:
  db:
    image: postgres:16
    container_name: ${APP_NAME:-rs-management4}-db
    restart: unless-stopped
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "${POSTGRES_PORT:-5432}:5432"
    volumes:
      - db_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"]
      interval: 5s
      timeout: 5s
      retries: 10

  pgadmin:
    image: dpage/pgadmin4:8
    container_name: ${APP_NAME:-rs-management4}-pgadmin
    restart: unless-stopped
    depends_on:
      db:
        condition: service_healthy
    environment:
      PGADMIN_DEFAULT_EMAIL: ${PGADMIN_DEFAULT_EMAIL}
      PGADMIN_DEFAULT_PASSWORD: ${PGADMIN_DEFAULT_PASSWORD}
    ports:
      - "${PGADMIN_PORT:-5050}:80"
    volumes:
      - pgadmin_data:/var/lib/pgadmin

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: ${APP_NAME:-rs-management4}-app
    restart: on-failure
    depends_on:
      db:
        condition: service_healthy
    environment:
      APP_NAME: ${APP_NAME}
      APP_PORT: ${APP_PORT}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
      POSTGRES_HOST: db
      POSTGRES_PORT: ${POSTGRES_PORT}
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      JAVA_OPTS: ${JAVA_OPTS}
    ports:
      - "${APP_PORT:-8080}:8080"
    # volumes:
    #   - ./:/workspace
    working_dir: /app
    command: ["sh", "-c", "/app/scripts/wait-for-db.sh && exec java $JAVA_OPTS -jar /app/app.jar"]

volumes:
  db_data:
  pgadmin_data:
```

Chạy tất cả:

```bash
docker compose up -d --build
```

---

## API Docs (Swagger/OpenAPI)

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

`pom.xml` snippet (springdoc):

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.6.0</version>
</dependency>
```

Ảnh: `docs/images/swagger.png`

---

## Test (JUnit5, Testcontainers)

Chạy test:

```bash
mvn test
```

---