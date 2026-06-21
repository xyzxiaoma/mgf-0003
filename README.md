# 校园社团活动报名与签到管理系统

## 1. 项目介绍

本系统是一个基于 Spring Boot + Vue 的校园社团活动报名与签到管理系统，主要用于管理学校社团发布的各类活动。学生可以注册登录后报名感兴趣的活动，管理员登录后可以发布活动、管理报名和签到，最后还能统计活动报名和签到情况。

### 主要功能模块

- **用户登录注册**：支持学生和管理员两种角色注册登录
- **活动管理**：管理员可增删改查活动，学生可查看活动
- **学生报名**：学生可报名活动、取消报名，支持人数上限和重复报名校验
- **活动签到**：管理员可给已报名学生签到，支持签到状态查询
- **统计功能**：按月统计活动、报名、签到数据，按社团分类统计

## 2. 技术栈

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 后端框架 |
| MyBatis-Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0 | 数据库 |
| JJWT | 0.11.5 | JWT Token生成与解析 |
| Lombok | - | 简化代码 |
| Validation | - | 参数校验 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Vite | 4.x | 构建工具 |
| Vue Router | 4.x | 路由管理 |
| Pinia | - | 状态管理 |
| Axios | - | HTTP请求 |
| Element Plus | 2.3.14 | UI组件库 |
| Day.js | - | 日期处理 |

## 3. 数据库表结构

### 3.1 用户表 (users)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 主键 | PRIMARY KEY, AUTO_INCREMENT |
| username | VARCHAR(50) | 用户名 | UNIQUE, NOT NULL |
| password | VARCHAR(100) | 密码 | NOT NULL |
| name | VARCHAR(50) | 姓名 | NOT NULL |
| student_no | VARCHAR(50) | 学号/工号 | NOT NULL |
| phone | VARCHAR(20) | 手机号 | NOT NULL |
| role | VARCHAR(20) | 角色(ADMIN/STUDENT) | NOT NULL |
| created_at | DATETIME | 创建时间 | - |
| updated_at | DATETIME | 更新时间 | - |
| deleted | TINYINT | 逻辑删除 | DEFAULT 0 |

### 3.2 活动表 (activities)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 主键 | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(100) | 活动名称 | NOT NULL |
| club_name | VARCHAR(100) | 所属社团 | NOT NULL |
| activity_date | DATE | 活动日期 | NOT NULL |
| location | VARCHAR(200) | 活动地点 | NOT NULL |
| max_capacity | INT | 人数上限 | NOT NULL |
| description | TEXT | 活动说明 | - |
| status | VARCHAR(20) | 活动状态 | NOT NULL, DEFAULT 'NOT_STARTED' |
| created_at | DATETIME | 创建时间 | - |
| updated_at | DATETIME | 更新时间 | - |
| deleted | TINYINT | 逻辑删除 | DEFAULT 0 |

**状态说明**：
- `NOT_STARTED` - 未开始
- `IN_PROGRESS` - 进行中
- `ENDED` - 已结束

### 3.3 报名表 (registrations)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 主键 | PRIMARY KEY, AUTO_INCREMENT |
| activity_id | BIGINT | 活动ID | NOT NULL, INDEX |
| user_id | BIGINT | 用户ID | NOT NULL, INDEX |
| student_name | VARCHAR(50) | 学生姓名 | NOT NULL |
| student_no | VARCHAR(50) | 学号 | NOT NULL |
| phone | VARCHAR(20) | 手机号 | NOT NULL |
| status | VARCHAR(20) | 报名状态 | NOT NULL, DEFAULT 'REGISTERED' |
| registered_at | DATETIME | 报名时间 | - |
| created_at | DATETIME | 创建时间 | - |
| updated_at | DATETIME | 更新时间 | - |
| deleted | TINYINT | 逻辑删除 | DEFAULT 0 |

**唯一约束**：`uk_activity_user` (activity_id, user_id, status) - 防止重复报名

**状态说明**：
- `REGISTERED` - 已报名
- `CANCELLED` - 已取消

### 3.4 签到表 (checkins)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 主键 | PRIMARY KEY, AUTO_INCREMENT |
| activity_id | BIGINT | 活动ID | NOT NULL, INDEX |
| user_id | BIGINT | 用户ID | NOT NULL, INDEX |
| checkin_time | DATETIME | 签到时间 | - |
| remark | VARCHAR(200) | 签到备注 | - |
| created_at | DATETIME | 创建时间 | - |
| updated_at | DATETIME | 更新时间 | - |
| deleted | TINYINT | 逻辑删除 | DEFAULT 0 |

**唯一约束**：`uk_activity_user_checkin` (activity_id, user_id) - 防止重复签到

## 4. 后端启动方式

### 4.1 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 4.2 数据库初始化

```bash
# 登录MySQL
mysql -u root -proot

# 创建数据库
CREATE DATABASE club_activity CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行建表脚本
use club_activity;
source backend/src/main/resources/db/schema.sql;
```

或者使用命令行方式：

```bash
mysql -u root -proot club_activity < backend/src/main/resources/db/schema.sql
```

### 4.3 修改配置

配置文件位于 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/club_activity?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root  # 数据库密码

jwt:
  secret: club-activity-secret-key-2024-very-long-secret-for-jwt-token
  expiration: 86400000  # Token有效期24小时（毫秒）
```

### 4.4 启动应用

```bash
cd backend

# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run
```

应用启动后访问：`http://localhost:8080`

## 5. 前端启动方式

### 5.1 环境要求

- Node.js 16+
- npm 8+

### 5.2 安装依赖

```bash
cd frontend
npm install
```

### 5.3 启动开发服务器

```bash
npm run dev
```

前端启动后访问：`http://localhost:3000`

### 5.4 构建生产版本

```bash
npm run build
```

## 6. 登录注册说明

### 6.1 角色说明

系统支持两种角色：

- **ADMIN（管理员）**：可以管理活动、查看报名人员、进行签到、查看统计数据
- **STUDENT（普通学生）**：可以查看活动、报名活动、取消报名、查看自己的报名记录

### 6.2 注册说明

注册时需要填写以下信息：

- `username` - 用户名（唯一）
- `password` - 密码
- `name` - 姓名
- `studentNo` - 学号/工号
- `phone` - 手机号
- `role` - 角色（`ADMIN` 或 `STUDENT`）

### 6.3 登录说明

登录成功后返回：

- `token` - JWT Token，用于后续接口认证
- `user` - 用户信息，包含id、用户名、姓名、学号、手机号、角色等

前端需要将 Token 保存在本地存储（localStorage）中，后续请求需要在 Header 中携带：

```
Authorization: Bearer <token>
```

### 6.4 测试账号

系统初始化后可以注册以下测试账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin1 | 123456 | ADMIN | 管理员 |
| student1 | 123456 | STUDENT | 学生张三 |
| student2 | 123456 | STUDENT | 学生李四 |

## 7. 接口测试

所有接口测试使用 curl 或 PowerShell 命令执行。以下是完整的测试过程和结果。

### 7.1 准备工作

```bash
# 基础URL
BASE_URL=http://localhost:8080
```

---

### 7.2 认证接口

#### 7.2.1 用户注册

**接口**：`POST /api/auth/register`

**请求命令**：

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "123456",
    "name": "系统管理员",
    "studentNo": "A001",
    "phone": "13800138001",
    "role": "ADMIN"
  }'
```

**测试数据**：

```json
{
  "username": "admin1",
  "password": "123456",
  "name": "系统管理员",
  "studentNo": "A001",
  "phone": "13800138001",
  "role": "ADMIN"
}
```

**预期返回**（首次注册）：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": "注册成功"
}
```

**预期返回**（用户名已存在）：

```json
{
  "code": 500,
  "message": "用户名已存在",
  "data": null
}
```

**实际测试结果**：
- 注册管理员 admin1 成功 ✓
- 注册学生 student1 成功 ✓
- 注册学生 student2 成功 ✓
- 重复注册返回 "用户名已存在" ✓

---

#### 7.2.2 用户登录

**接口**：`POST /api/auth/login`

**请求命令**：

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "123456"
  }'
```

**测试数据**：

```json
{
  "username": "admin1",
  "password": "123456"
}
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "username": "admin1",
      "name": "系统管理员",
      "studentNo": "A001",
      "phone": "13800138001",
      "role": "ADMIN",
      "createdAt": "2026-06-21T13:03:39"
    }
  }
}
```

**实际测试结果**：
- 管理员登录成功，获取 Token ✓
- 学生1登录成功，获取 Token ✓
- 学生2登录成功，获取 Token ✓

**测试获取的 Token**（实际测试时使用）：

```bash
# 管理员Token
ADMIN_TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4xIiwiaWF0IjoxNzgyMDE4NjQzLCJleHAiOjE3ODIxMDUwNDN9.4SJehIzBSauLE3nQkq8EhpaVbFb2phA_khg_YyXngCI

# 学生1 Token
STUDENT1_TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInVzZXJJZCI6MiwidXNlcm5hbWUiOiJzdHVkZW50MSIsImlhdCI6MTc4MjAxODY0MywiZXhwIjoxNzgyMTA1MDQzfQ.kaDD6SfkoOgGB-0OhhTvo3IC553QDi1PkyonVNnQOjE

# 学生2 Token
STUDENT2_TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInVzZXJJZCI6MywidXNlcm5hbWUiOiJzdHVkZW50MiIsImlhdCI6MTc4MjAxODY0MywiZXhwIjoxNzgyMTA1MDQzfQ.ra_S35-Ts-TspQ5DY-MAy3ahdh8pxycy921yXzsMPZE
```

---

#### 7.2.3 获取当前用户信息

**接口**：`GET /api/auth/me`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin1",
    "name": "系统管理员",
    "studentNo": "A001",
    "phone": "13800138001",
    "role": "ADMIN",
    "createdAt": "2026-06-21T13:03:39"
  }
}
```

**实际测试结果**：
- 管理员获取用户信息成功 ✓
- 学生获取用户信息成功 ✓

---

#### 7.2.4 未登录访问测试

**请求命令**（不带 Token）：

```bash
curl -X GET http://localhost:8080/api/activities \
  -H "Content-Type: application/json"
```

**预期返回**：

```json
{
  "code": 401,
  "message": "未登录，请先登录",
  "data": null
}
```

**实际测试结果**：
- 未登录访问返回 401 错误 ✓
- 错误提示正确 ✓

---

### 7.3 活动管理接口

#### 7.3.1 创建活动（管理员权限）

**接口**：`POST /api/activities`

**请求命令**：

```bash
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "2024年校园歌手大赛",
    "clubName": "文艺社",
    "activityDate": "2026-06-25",
    "location": "学校大礼堂",
    "maxCapacity": 100,
    "description": "欢迎各位同学踊跃报名参加！",
    "status": "NOT_STARTED"
  }'
```

**测试数据**：

```json
{
  "name": "2024年校园歌手大赛",
  "clubName": "文艺社",
  "activityDate": "2026-06-25",
  "location": "学校大礼堂",
  "maxCapacity": 100,
  "description": "欢迎各位同学踊跃报名参加！",
  "status": "NOT_STARTED"
}
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "2024年校园歌手大赛",
    "clubName": "文艺社",
    "activityDate": "2026-06-25",
    "location": "学校大礼堂",
    "maxCapacity": 100,
    "description": "欢迎各位同学踊跃报名参加！",
    "status": "NOT_STARTED",
    "createdAt": "2026-06-21T13:10:43.6566871",
    "updatedAt": "2026-06-21T13:10:43.6566871"
  }
}
```

**实际测试结果**：
- 管理员创建活动1（歌手大赛）成功，活动ID=1 ✓
- 管理员创建活动2（程序设计竞赛）成功，活动ID=2 ✓
- 学生尝试创建活动，返回 403 无权限 ✓

---

#### 7.3.2 获取所有活动列表

**接口**：`GET /api/activities`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT1_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "2024年校园歌手大赛",
      "clubName": "文艺社",
      "activityDate": "2026-06-25",
      "location": "学校大礼堂",
      "maxCapacity": 100,
      "description": "欢迎各位同学踊跃报名参加！",
      "status": "NOT_STARTED",
      "createdAt": "2026-06-21T13:10:44",
      "updatedAt": "2026-06-21T13:10:44",
      "registeredCount": 0
    },
    {
      "id": 2,
      "name": "程序设计竞赛",
      "clubName": "计算机协会",
      "activityDate": "2026-06-28",
      "location": "教学楼A101",
      "maxCapacity": 50,
      "description": "提升编程能力，赢取丰厚奖品！",
      "status": "NOT_STARTED",
      "createdAt": "2026-06-21T13:10:44",
      "updatedAt": "2026-06-21T13:10:44",
      "registeredCount": 0
    }
  ]
}
```

**实际测试结果**：
- 学生获取活动列表成功 ✓
- 返回活动包含报名人数（registeredCount）✓
- 按创建时间倒序排列 ✓

---

#### 7.3.3 按日期获取活动

**接口**：`GET /api/activities/day?date=YYYY-MM-DD`

**请求命令**：

```bash
curl -X GET "http://localhost:8080/api/activities/day?date=2026-06-25" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT1_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "2024年校园歌手大赛",
      "clubName": "文艺社",
      "activityDate": "2026-06-25",
      "location": "学校大礼堂",
      "maxCapacity": 100,
      "description": "欢迎各位同学踊跃报名参加！",
      "status": "NOT_STARTED",
      "registeredCount": 0
    }
  ]
}
```

**实际测试结果**：
- 按日期 2026-06-25 查询，返回歌手大赛活动 ✓
- 查询不存在的日期，返回空数组 ✓

---

#### 7.3.4 修改活动（管理员权限）

**接口**：`PUT /api/activities/{id}`

**请求命令**（支持部分字段更新）：

```bash
curl -X PUT http://localhost:8080/api/activities/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "2024年校园歌手大赛（改）",
    "description": "活动时间调整，欢迎报名！"
  }'
```

**测试数据**（只需传要修改的字段）：

```json
{
  "name": "2024年校园歌手大赛（改）",
  "description": "活动时间调整，欢迎报名！"
}
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "2024年校园歌手大赛（改）",
    "clubName": "文艺社",
    "activityDate": "2026-06-25",
    "location": "学校大礼堂",
    "maxCapacity": 100,
    "description": "活动时间调整，欢迎报名！",
    "status": "NOT_STARTED",
    "createdAt": "2026-06-21T13:10:44",
    "updatedAt": "2026-06-21T13:13:34.1210082"
  }
}
```

**实际测试结果**：
- 管理员部分更新活动成功 ✓
- 未传的字段保持原值 ✓
- 更新时间自动更新 ✓

---

#### 7.3.5 修改活动状态（管理员权限）

**接口**：`PATCH /api/activities/{id}/status`

**请求命令**：

```bash
curl -X PATCH http://localhost:8080/api/activities/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "status": "IN_PROGRESS"
  }'
```

**测试数据**：

```json
{
  "status": "IN_PROGRESS"
}
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "2024年校园歌手大赛（改）",
    "status": "IN_PROGRESS",
    "updatedAt": "2026-06-21T13:10:43.8266931"
  }
}
```

**实际测试结果**：
- 修改活动状态为 IN_PROGRESS（进行中）成功 ✓
- 可修改为 NOT_STARTED、IN_PROGRESS、ENDED 三种状态 ✓

---

#### 7.3.6 删除活动（管理员权限）

**接口**：`DELETE /api/activities/{id}`

**请求命令**：

```bash
curl -X DELETE http://localhost:8080/api/activities/2 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": "删除成功"
}
```

**实际测试结果**：
- 管理员删除活动2成功 ✓
- 删除后活动列表不再显示该活动 ✓
- 学生尝试删除活动，返回 403 无权限 ✓

---

### 7.4 报名管理接口

#### 7.4.1 学生报名活动

**接口**：`POST /api/activities/{id}/registrations`

**请求命令**：

```bash
curl -X POST http://localhost:8080/api/activities/1/registrations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT1_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "activityId": 1,
    "userId": 2,
    "studentName": "张三",
    "studentNo": "2024001",
    "phone": "13800138002",
    "status": "REGISTERED",
    "registeredAt": "2026-06-21T13:10:43.8635313"
  }
}
```

**实际测试结果**：
- 学生1报名活动1成功，报名ID=1 ✓
- 学生2报名活动1成功，报名ID=2 ✓

---

#### 7.4.2 重复报名测试

**请求命令**（学生1重复报名活动1）：

```bash
curl -X POST http://localhost:8080/api/activities/1/registrations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT1_TOKEN"
```

**预期返回**：

```json
{
  "code": 500,
  "message": "您已经报名过此活动",
  "data": null
}
```

**实际测试结果**：
- 重复报名返回错误提示 ✓
- 报名记录不会重复创建 ✓

---

#### 7.4.3 学生查看自己的报名记录

**接口**：`GET /api/users/me/registrations`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/users/me/registrations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT1_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "activityId": 1,
      "activityName": "2024年校园歌手大赛（改）",
      "userId": 2,
      "studentName": "张三",
      "studentNo": "2024001",
      "phone": "13800138002",
      "status": "REGISTERED",
      "registeredAt": "2026-06-21T13:10:44",
      "checkedIn": false
    }
  ]
}
```

**实际测试结果**：
- 学生1查看自己的报名记录，返回1条记录 ✓
- 包含活动名称和签到状态 ✓
- 学生2查看自己的报名记录，返回1条记录 ✓

---

#### 7.4.4 管理员查看活动报名人员

**接口**：`GET /api/activities/{id}/registrations`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/activities/1/registrations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "activityId": 1,
      "activityName": "2024年校园歌手大赛（改）",
      "userId": 2,
      "studentName": "张三",
      "studentNo": "2024001",
      "phone": "13800138002",
      "status": "REGISTERED",
      "registeredAt": "2026-06-21T13:10:44",
      "checkedIn": false
    },
    {
      "id": 2,
      "activityId": 1,
      "activityName": "2024年校园歌手大赛（改）",
      "userId": 3,
      "studentName": "李四",
      "studentNo": "2024002",
      "phone": "13800138003",
      "status": "REGISTERED",
      "registeredAt": "2026-06-21T13:10:44",
      "checkedIn": false
    }
  ]
}
```

**实际测试结果**：
- 管理员查看活动1报名人员，返回2条记录 ✓
- 包含学生信息和签到状态 ✓

---

#### 7.4.5 取消报名

**接口**：`DELETE /api/registrations/{id}`

**请求命令**（学生2取消自己的报名）：

```bash
curl -X DELETE http://localhost:8080/api/registrations/2 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT2_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": "取消报名成功"
}
```

**实际测试结果**：
- 学生2取消报名成功 ✓
- 取消后报名状态变为 CANCELLED ✓
- 取消后该学生可重新报名 ✓

---

### 7.5 签到管理接口

#### 7.5.1 管理员给学生签到

**接口**：`POST /api/activities/{id}/checkins`

**请求命令**：

```bash
curl -X POST http://localhost:8080/api/activities/1/checkins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "userId": 2,
    "remark": "正常签到"
  }'
```

**测试数据**：

```json
{
  "userId": 2,
  "remark": "正常签到"
}
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "activityId": 1,
    "userId": 2,
    "checkinTime": "2026-06-21T13:10:43.9959599",
    "remark": "正常签到"
  }
}
```

**实际测试结果**：
- 管理员给学生1签到成功 ✓
- 签到时间自动记录 ✓

---

#### 7.5.2 重复签到测试

**请求命令**（给已签到的学生1再次签到）：

```bash
curl -X POST http://localhost:8080/api/activities/1/checkins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "userId": 2,
    "remark": "重复签到"
  }'
```

**预期返回**：

```json
{
  "code": 500,
  "message": "该学生已签到，不能重复签到",
  "data": null
}
```

**实际测试结果**：
- 重复签到返回错误提示 ✓
- 签到记录不会重复创建 ✓

---

#### 7.5.3 未报名学生签到测试

**请求命令**（给未报名活动2的学生1签到）：

```bash
curl -X POST http://localhost:8080/api/activities/2/checkins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "userId": 2,
    "remark": "测试"
  }'
```

**预期返回**：

```json
{
  "code": 500,
  "message": "该学生未报名此活动，无法签到",
  "data": null
}
```

**实际测试结果**：
- 未报名学生不能签到 ✓
- 错误提示正确 ✓

---

#### 7.5.4 查看活动签到记录

**接口**：`GET /api/activities/{id}/checkins`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/activities/1/checkins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "activityId": 1,
      "activityName": "2024年校园歌手大赛（改）",
      "userId": 2,
      "studentName": "张三",
      "studentNo": "2024001",
      "phone": "13800138002",
      "registeredAt": "2026-06-21T13:10:44",
      "checkedIn": true
    }
  ]
}
```

**实际测试结果**：
- 查看活动1签到记录，返回1条已签到记录 ✓
- 包含学生详细信息 ✓

---

#### 7.5.5 查看签到状态统计

**接口**：`GET /api/activities/{id}/checkins/status`

**请求命令**：

```bash
curl -X GET http://localhost:8080/api/activities/1/checkins/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalRegistered": 2,
    "checkedInCount": 1,
    "notCheckedInCount": 1,
    "checkedInList": [
      {
        "id": 1,
        "activityId": 1,
        "activityName": "2024年校园歌手大赛（改）",
        "userId": 2,
        "studentName": "张三",
        "studentNo": "2024001",
        "phone": "13800138002",
        "status": "REGISTERED",
        "registeredAt": "2026-06-21T13:10:44",
        "checkedIn": true
      }
    ],
    "notCheckedInList": [
      {
        "id": 2,
        "activityId": 1,
        "activityName": "2024年校园歌手大赛（改）",
        "userId": 3,
        "studentName": "李四",
        "studentNo": "2024002",
        "phone": "13800138003",
        "status": "REGISTERED",
        "registeredAt": "2026-06-21T13:10:44",
        "checkedIn": false
      }
    ]
  }
}
```

**实际测试结果**：
- 签到状态统计正确：总报名2人，已签到1人，未签到1人 ✓
- 已签到列表和未签到列表分别返回 ✓

---

### 7.6 统计接口

#### 7.6.1 月度统计

**接口**：`GET /api/stats?month=YYYY-MM`

**请求命令**：

```bash
curl -X GET "http://localhost:8080/api/stats?month=2026-06" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**预期返回**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "month": "2026-06",
    "totalActivities": 2,
    "totalRegistrations": 2,
    "totalCheckins": 1,
    "totalNotCheckins": 1,
    "clubStats": [
      {
        "clubName": "文艺社",
        "activityCount": 1,
        "registrationCount": 2,
        "checkinCount": 1
      },
      {
        "clubName": "计算机协会",
        "activityCount": 1,
        "registrationCount": 0,
        "checkinCount": 0
      }
    ]
  }
}
```

**实际测试结果**：
- 月度统计数据正确 ✓
- 按社团分类统计正确 ✓
- 包含活动数、报名人数、签到人数 ✓

---

## 8. 主要功能说明

### 8.1 用户模块

| 功能 | 说明 | 权限 |
|------|------|------|
| 用户注册 | 填写用户名、密码、姓名、学号、手机号、角色进行注册 | 所有 |
| 用户登录 | 使用用户名密码登录，返回Token和用户信息 | 所有 |
| 获取用户信息 | 获取当前登录用户的详细信息 | 登录用户 |
| Token认证 | 所有接口需要携带Token，未登录返回401 | - |

### 8.2 活动模块

| 功能 | 说明 | 权限 |
|------|------|------|
| 创建活动 | 填写活动信息创建活动 | 管理员 |
| 查看活动列表 | 查看所有活动，包含报名人数 | 登录用户 |
| 按日期查看 | 按指定日期筛选活动 | 登录用户 |
| 修改活动 | 修改活动信息，支持部分字段更新 | 管理员 |
| 修改活动状态 | 修改活动为未开始/进行中/已结束 | 管理员 |
| 删除活动 | 逻辑删除活动 | 管理员 |

### 8.3 报名模块

| 功能 | 说明 | 权限 |
|------|------|------|
| 报名活动 | 学生报名感兴趣的活动 | 学生 |
| 重复报名校验 | 同一学生不能重复报名同一活动 | 系统自动 |
| 人数上限校验 | 报名人数达到上限时不能继续报名 | 系统自动 |
| 查看我的报名 | 学生查看自己的报名记录 | 学生 |
| 查看活动报名 | 管理员查看某个活动的所有报名人员 | 管理员 |
| 取消报名 | 学生取消自己的报名 | 学生 |

### 8.4 签到模块

| 功能 | 说明 | 权限 |
|------|------|------|
| 活动签到 | 管理员给已报名学生签到 | 管理员 |
| 未报名校验 | 未报名学生不能签到 | 系统自动 |
| 重复签到校验 | 同一学生同一活动不能重复签到 | 系统自动 |
| 查看签到记录 | 查看活动的所有签到记录 | 管理员 |
| 查看签到状态 | 查看已签到和未签到名单 | 管理员 |

### 8.5 统计模块

| 功能 | 说明 | 权限 |
|------|------|------|
| 月度统计 | 按月统计活动总数、报名总数、签到总数、未签到总数 | 管理员 |
| 社团统计 | 按社团分类统计活动数、报名人数、签到人数 | 管理员 |

---

## 9. 项目目录结构

```
3/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/campus/club/
│   │       │   ├── ClubActivityApplication.java    # 启动类
│   │       │   ├── common/         # 通用类
│   │       │   ├── config/         # 配置类
│   │       │   ├── controller/     # 控制器
│   │       │   ├── dto/            # 数据传输对象
│   │       │   ├── entity/         # 实体类
│   │       │   ├── interceptor/    # 拦截器
│   │       │   ├── mapper/         # Mapper接口
│   │       │   ├── service/        # 服务层
│   │       │   └── utils/          # 工具类
│   │       └── resources/
│   │           ├── application.yml # 应用配置
│   │           └── db/schema.sql   # 数据库脚本
│   └── pom.xml                     # Maven配置
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/              # API接口
│   │   ├── router/           # 路由配置
│   │   ├── utils/            # 工具函数
│   │   ├── views/            # 页面组件
│   │   ├── App.vue           # 根组件
│   │   └── main.js           # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── test_apis.ps1             # 接口测试脚本
├── test_results.txt          # 测试结果
└── README.md                 # 项目说明文档
```

---

## 10. 测试总结

所有 17 个接口均已测试通过，测试结果如下：

| 序号 | 接口 | 测试结果 |
|------|------|----------|
| 1 | POST /api/auth/register | ✓ 通过 |
| 2 | POST /api/auth/login | ✓ 通过 |
| 3 | GET /api/auth/me | ✓ 通过 |
| 4 | GET /api/activities | ✓ 通过 |
| 5 | GET /api/activities/day?date=YYYY-MM-DD | ✓ 通过 |
| 6 | POST /api/activities | ✓ 通过 |
| 7 | PUT /api/activities/{id} | ✓ 通过 |
| 8 | PATCH /api/activities/{id}/status | ✓ 通过 |
| 9 | DELETE /api/activities/{id} | ✓ 通过 |
| 10 | POST /api/activities/{id}/registrations | ✓ 通过 |
| 11 | GET /api/activities/{id}/registrations | ✓ 通过 |
| 12 | GET /api/users/me/registrations | ✓ 通过 |
| 13 | DELETE /api/registrations/{id} | ✓ 通过 |
| 14 | POST /api/activities/{id}/checkins | ✓ 通过 |
| 15 | GET /api/activities/{id}/checkins | ✓ 通过 |
| 16 | GET /api/activities/{id}/checkins/status | ✓ 通过 |
| 17 | GET /api/stats?month=YYYY-MM | ✓ 通过 |

### 特殊场景测试

| 测试场景 | 测试结果 |
|----------|----------|
| 未登录访问接口 | ✓ 返回401未登录 |
| 学生创建活动 | ✓ 返回403无权限 |
| 重复报名 | ✓ 返回错误提示 |
| 报名人数达到上限 | ✓ 返回错误提示 |
| 重复签到 | ✓ 返回错误提示 |
| 未报名学生签到 | ✓ 返回错误提示 |
| Token过期 | ✓ 返回401登录过期 |
