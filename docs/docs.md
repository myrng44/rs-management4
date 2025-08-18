# RS-Management - Hệ Thống Quản Lý Bán Hàng & Kho Hàng

## 📋 Tổng Quan

RS-Management là hệ thống quản lý bán hàng và kho hàng toàn diện, xây dựng bằng Spring Boot với kiến trúc microservices.

## 🏗️ Kiến Trúc Hệ Thống

### Tech Stack
- **Backend**: Java 21, Spring Boot 3.5.3, Spring Security, JPA
- **Database**: PostgreSQL với Flyway migration
- **Authentication**: JWT tokens
- **Documentation**: Swagger/OpenAPI
- **Containerization**: Docker & Docker Compose

### Package Structure
```
ck4.nvb.rsmanagement/
├── base/                    # Base classes & utilities
├── config/                  # Configuration classes
├── core/                    # Core business modules
│   ├── module/
│   │   ├── dashboard/      # Dashboard & reporting
│   │   ├── order/          # Sales order management
│   │   ├── stores/         # Inventory management
│   │   └── users/          # User management
│   └── web/                # Web security & common
```

## 📁 Các Module Chính

### 1. Base Package
- **Domain Layer**: Base entities với audit fields
- **Application Layer**: DTOs, Service interfaces
- **Web Layer**: Base controllers cho CRUD operations

### 2. Core Modules
- **Dashboard**: Thống kê tổng quan, báo cáo
- **Order Management**: Quản lý đơn hàng, khách hàng, thanh toán
- **Store Management**: Quản lý sản phẩm, kho hàng, nhà cung cấp
- **User Management**: Phân quyền, vai trò, xác thực

## 🗄️ Thiết Kế Database

### Các Bảng Chính
- **Core**: users, role, permission, user_role
- **Store**: store, product, category, supplier, batch, batch_stock
- **Sales**: sale_order, sale_line, sale_allocation, customer
- **Inventory**: inventory_adjustment, store_transfer
- **Voucher**: voucher, voucher_customer, voucher_redemption

### Design Patterns
- **Audit Pattern**: Tất cả entities có audit fields
- **Soft Delete**: Sử dụng flag deleted thay vì xóa thực sự
- **Batch Stock Pattern**: Quản lý tồn kho theo lô với trạng thái

## 🔄 Luồng Hoạt Động

### 1. Authentication Flow
```
Client → AuthController → JwtTokenService → UserService → Database
```

### 2. Sales Order Flow
```
Create Order → Validate → Allocate Inventory → Update Stock → Complete
```

### 3. Inventory Management
```
Import → Create Batch → Distribute to Stores → Track Status → Update on Sales
```

## 📚 API Documentation

### Base URLs
- **Secured**: `/${rs.api.main.baseUrl}/` (default: `/secured/rest/v1/`)
- **Public**: `/${rs.api.main.publicUrl}/` (default: `/public/rest/v1/`)

### Key Endpoints
- **Auth**: `/public/rest/v1/auth/login`, `/public/rest/v1/auth/register`
- **Products**: `/secured/rest/v1/products`
- **Orders**: `/secured/rest/v1/orders`
- **Users**: `/secured/rest/v1/users`

### Swagger UI
Truy cập tại: `http://localhost:8080/swagger-ui/index.html`

## 🚀 Triển Khai

### Yêu Cầu
- Java JDK 21
- Maven 3.9+
- Docker 24+
- PostgreSQL 16+

### Cài Đặt Nhanh
```bash
# 1. Clone repository
git clone https://github.com/myrng44/rs-management4.git
cd rs-management4

# 2. Cấu hình environment
cp .env.example .env

# 3. Chạy với Docker Compose
docker compose up -d --build
```

### Environment Variables
```env
APP_NAME=rs-management4
APP_PORT=8080
POSTGRES_DB=rs_management_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=mypassword
```

## 🔐 Bảo Mật

### Authentication
- **JWT-based**: Stateless authentication
- **Role-based Access Control**: SYSADMIN, ADMIN, STAFF
- **Permission System**: Granular permissions

### Security Features
- CSRF disabled (stateless API)
- CORS configured
- Password encryption với BCrypt
- RSA key pairs cho JWT signing

## 🧪 Testing

### Test Strategy
- **Unit Tests**: Service, Repository, Controller layers
- **Integration Tests**: Database integration, API testing
- **Security Tests**: Authentication & authorization

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ProductControllerTest
```

## 📊 Monitoring

### Health Checks
- Database connectivity
- Application status
- Custom business metrics

### Logging
- Structured logging với SLF4J
- Performance metrics
- Business KPIs

## 🔧 Troubleshooting

### Common Issues
1. **Database Connection**: Kiểm tra PostgreSQL container
2. **Port Conflicts**: Đảm bảo ports 8080, 5432, 5050 available
3. **Memory Issues**: Tăng JVM heap size nếu cần

### Debug Commands
```bash
# Check container status
docker compose ps

# View logs
docker compose logs app

# Database connection
docker exec -it rs-management4-db psql -U postgres -d rs_management_db
```

## 📈 Performance & Optimization

### Database Optimization
- **Indexes**: Strategic indexing cho query performance
- **Partitioning**: Monthly partitioning cho large tables
- **Views**: Performance views cho complex queries

### Application Optimization
- **Connection Pooling**: HikariCP optimization
- **Query Optimization**: QueryDSL dynamic queries
- **Caching**: Future Redis implementation

## 🚀 Production Deployment

### Considerations
- **HTTPS**: Enable SSL/TLS
- **Load Balancing**: Nginx hoặc cloud load balancer
- **Monitoring**: APM tools, centralized logging
- **Backup**: Regular database backups

### Cloud Deployment
- **Docker**: Production-ready containers
- **Kubernetes**: Container orchestration
- **CI/CD**: Automated deployment pipelines

---

*Tài liệu này mô tả dự án RS-Management - Hệ thống quản lý bán hàng và kho hàng toàn diện.*
