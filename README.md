# SpringBoot 后台脚手架项目

## 📋 项目简介

这是一个完整的、生产级别的 SpringBoot 后台脚手架项目，集成了认证、授权、分页、文件上传等常用功能。

## ✨ 主要功能

### 🔐 认证与授权
- JWT 令牌认证机制
- Spring Security 集成
- 基于角色的访问控制(RBAC)
- 权限细粒度控制
- 刷新令牌机制

### 👥 用户管理
- 用户注册/登录
- 用户信息管理
- 用户状态管理
- 最后登录时间记录

### 📄 分页查询
- PageHelper 分页插件集成
- 灵活的查询条件支持
- 支持排序和搜索

### 📁 文件管理
- 文件上传功能
- 文件下载功能
- 文件删除功能
- 文件大小验证
- 文件类型验证
- 上传记录管理

### 🛠 系统功能
- 全局异常处理
- 统一 API 响应格式
- Swagger API 文档
- 详细的日志记录
- Redis 缓存支持
- 数据验证

## 🚀 快速开始

### 前置要求
- Java 17+
- MySQL 8.0+
- Redis (可选)
- Maven 3.6+

### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/mi0907/springboot.git
cd springboot
```

2. **创建数据库**
```sql
CREATE DATABASE scaffold CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **配置应用**
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/scaffold
    username: root
    password: your_password
```

4. **构建项目**
```bash
mvn clean install
```

5. **运行应用**
```bash
mvn spring-boot:run
```

应用将在 http://localhost:8080/api 启动

## 📚 API 文档

Swagger UI 文档：http://localhost:8080/swagger-ui.html

### 认证接口

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}

# 响应
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "expiresIn": 86400000,
    "user": {
      "id": 1,
      "username": "testuser",
      "email": "testuser@example.com",
      "roles": ["user"],
      "permissions": ["user:read", "file:upload"]
    }
  }
}
```

#### 刷新令牌
```http
POST /api/auth/refresh-token
Authorization: Bearer {refreshToken}
```

### 用户接口

#### 分页查询用户列表
```http
GET /api/users?pageNum=1&pageSize=10&username=test
Authorization: Bearer {token}
```

#### 获取用户详情
```http
GET /api/users/{id}
Authorization: Bearer {token}
```

#### 修改用户信息
```http
PUT /api/users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "realName": "张三",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

#### 删除用户
```http
DELETE /api/users/{id}
Authorization: Bearer {token}
```

### 文件接口

#### 上传文件
```http
POST /api/files/upload
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [选择文件]
```

#### 下载文件
```http
GET /api/files/download/{fileId}
```

#### 删除文件
```http
DELETE /api/files/{fileId}
Authorization: Bearer {token}
```

## 📁 项目结构

```
springboot/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── controller/          # 控制器层
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── FileController.java
│   │   │   ├── service/             # 业务逻辑层
│   │   │   │   ├── UserService.java
│   │   │   │   ├── FileService.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── repository/          # 数据访问层
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── FileRecordRepository.java
│   │   │   ├── entity/              # 实体类
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Permission.java
│   │   │   │   └── FileRecord.java
│   │   │   ├── dto/                 # 数据传输对象
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── UserDto.java
│   │   │   │   ├── PageResponse.java
│   │   │   │   └── FileUploadResponse.java
│   │   │   ├── security/            # 安全模块
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── UserPrincipal.java
│   │   │   ├── config/              # 配置类
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── common/              # 通用模块
│   │   │   │   ├── response/
│   │   │   │   │   └── R.java
│   │   │   │   └── exception/
│   │   │   │       └── GlobalExceptionHandler.java
│   │   │   └── ScaffoldApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-dev.yml
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

## 🔑 核心技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.1.5 | 核心框架 |
| Spring Security | 6.1.x | 安全框架 |
| Spring Data JPA | 6.1.x | ORM框架 |
| JWT | 0.12.3 | 令牌认证 |
| MySQL | 8.0+ | 数据库 |
| Redis | - | 缓存服务 |
| PageHelper | 1.4.7 | ���页插件 |
| Lombok | 1.18.x | 代码生成 |
| Swagger | 2.0.2 | API文档 |

## ⚙️ 配置说明

### JWT 配置
编辑 `application.yml`：
```yaml
jwt:
  secret: 你的密钥（生产环境必须修改）
  expiration: 86400000  # 令牌有效期（毫秒）
```

### 文件上传配置
```yaml
file:
  upload-dir: ./uploads/  # 上传目录
  allowed-extensions: jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,zip  # 允许的文件类型
  max-file-size: 52428800  # 最大文件大小（字节）
```

## 🔒 安全建议

1. **修改 JWT 密钥** - 在生产环境中修改 `jwt.secret`
2. **使用 HTTPS** - 在生产环境中启用 HTTPS
3. **数据库备份** - 定期备份数据库
4. **日志审计** - 记录所有敏感操作
5. **权限最小化** - 为用户分配最小必要权限

## 📝 初始化数据

系统启动时会自动创建表结构。您可以手动添加角色和权限：

```sql
INSERT INTO sys_role (code, name, description) VALUES
('admin', '管理员', '系统管理员角色'),
('user', '普通用户', '普通用户角色'),
('guest', '访客', '访客角色');

INSERT INTO sys_permission (code, name, description) VALUES
('user:create', '创建用户', '创建用户权限'),
('user:read', '查看用户', '查看用户信息权限'),
('user:update', '修改用户', '修改用户信息权限'),
('user:delete', '删除用户', '删除用户权限'),
('file:upload', '上传文件', '上传文件权限'),
('file:delete', '删除文件', '删除文件权限');
```

## 🤝 贡献指南

欢迎提交 Pull Request 或报告 Issue

## 📄 许可证

MIT License

## 📞 联系方式

如有问题或建议，欢迎反馈

## 🎯 后续计划

- [ ] 集成 Spring Cloud
- [ ] 添加消息队列支持
- [ ] 集成分布式链路追踪
- [ ] 添加操作日志功能
- [ ] 集成企业微信/钉钉通知
- [ ] 完善单元测试
- [ ] Docker 容器化
- [ ] Kubernetes 部署文档

---

**最后更新**: 2026-05-11
