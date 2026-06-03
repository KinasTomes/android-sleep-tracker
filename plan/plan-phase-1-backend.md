# Phase 1: Backend Infrastructure Granular Implementation Plan (Golang)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Thiết lập hạ tầng Go Backend hoàn chỉnh với TimescaleDB, hỗ trợ quản lý User, đồng bộ dữ liệu Sleep và bảo mật JWT cơ bản.

**Architecture:** Clean Architecture (Handlers -> Services -> Repositories).
- **Go Framework:** Gin (High-performance HTTP router).
- **Database:** TimescaleDB (PostgreSQL) + `sqlx` (Lightweight SQL mapper).
- **Security:** JWT (JSON Web Token) cho Authentication.

**File Structure:**
- `backend/cmd/api/main.go`: Entry point.
- `backend/internal/domain/`: Structs định nghĩa User, SleepSession.
- `backend/internal/repository/`: Tương tác trực tiếp với SQL.
- `backend/internal/service/`: Logic nghiệp vụ (Business logic).
- `backend/internal/handler/`: Xử lý HTTP Request/Response.
- `backend/internal/middleware/`: Auth & Logging.

---

### Task 1: Thiết lập Môi trường & Database Migrations

**Files:**
- Create: `backend/docker-compose.yml`
- Create: `backend/init-db.sql`

- [ ] **Step 1: Viết `docker-compose.yml` chạy TimescaleDB và Redis**
- [ ] **Step 2: Viết script `init-db.sql` tạo bảng `users`, `user_survey_responses` và Hypertable `sleep_sessions`**
- [ ] **Step 3: Khởi động container và kiểm tra các bảng đã được tạo thành công**
- [ ] **Step 4: Commit**

### Task 2: Khởi tạo Project & Kết nối Database (sqlx)

**Files:**
- Create: `backend/go.mod`
- Create: `backend/internal/repository/db.go`

- [ ] **Step 1: Init module và cài đặt dependencies (`gin`, `sqlx`, `pq`, `testify`, `jwt-go`)**
- [ ] **Step 2: Viết hàm `ConnectDB` trả về `*sqlx.DB` với cơ chế retry logic**
- [ ] **Step 3: Viết Unit Test kiểm tra kết nối DB (Ping test)**
- [ ] **Step 4: Commit**

### Task 3: Phân hệ Quản lý Người dùng (User Management) - 3 Endpoints

**Files:**
- Create: `backend/internal/handler/user.go`
- Create: `backend/internal/service/user.go`
- Create: `backend/internal/repository/user.go`

- [ ] **Step 1: Hiện thực hóa `POST /api/v1/users` (Registration & Survey)**
    - Nhận: `email`, `password`, `age`, `gender`, `initial_snop` và mảng `survey_responses`.
    - Logic: Lưu User và các câu trả lời khảo sát vào DB trong cùng một Transaction.
- [ ] **Step 2: Hiện thực hóa `GET /api/v1/users/:id` (Get Profile)**
    - Trả về thông tin cá nhân, chỉ số `current_sleep_debt` và kết quả khảo sát.
- [ ] **Step 3: Hiện thực hóa `PATCH /api/v1/users/:id` (Update SNOP & Profile)**
    - Cho phép cập nhật cả thông tin cơ bản và các lựa chọn khảo sát mới.
- [ ] **Step 4: Viết bộ Unit Tests cho cả 3 Endpoints và Commit**

### Task 4: Phân hệ Đồng bộ Dữ liệu Giấc ngủ (Sleep Data Ingestion) - 2 Endpoints

**Files:**
- Create: `backend/internal/handler/sleep.go`
- Create: `backend/internal/repository/sleep.go`

- [ ] **Step 1: Hiện thực hóa `POST /api/v1/sync/sleep` (Bulk Sync)**
    - Nhận mảng các `SleepSessionRecord` từ Android (Health Connect).
    - Logic: Kiểm tra trùng lặp (duplication check) dựa trên `user_id`, `start_time` và `end_time` trước khi INSERT.
- [ ] **Step 2: Hiện thực hóa `GET /api/v1/sync/sleep/:user_id` (Sync History)**
    - Trả về danh sách các phiên ngủ gần nhất để người dùng kiểm tra trên App.
- [ ] **Step 3: Viết Unit Test kiểm tra logic chống trùng lặp dữ liệu và Commit**

### Task 5: Hạ tầng Bảo mật & Kiểm tra (Auth & Health)

**Files:**
- Create: `backend/internal/middleware/auth.go`
- Create: `backend/internal/handler/health.go`

- [ ] **Step 1: Viết Middleware `JWTAuth` để bảo vệ các route `/api/v1/sync/*`**
- [ ] **Step 2: Hiện thực hóa `GET /health` (System Health Check)**
    - Kiểm tra đồng thời kết nối DB và Redis.
- [ ] **Step 3: Thiết lập Router trung tâm gom tất cả các Handler lại trong `backend/internal/router/router.go`**
- [ ] **Step 4: Chạy toàn bộ hệ thống bằng `go run cmd/api/main.go` và kiểm tra qua Postman/Curl. Commit.**

---

**Danh sách Endpoints sau khi hoàn thành Phase 1:**
1. `POST /api/v1/users` (Đăng ký)
2. `GET /api/v1/users/:id` (Lấy thông tin cá nhân)
3. `PATCH /api/v1/users/:id` (Cập nhật SNOP/Profile)
4. `POST /api/v1/sync/sleep` (Đồng bộ dữ liệu - Cần JWT)
5. `GET /api/v1/sync/sleep/:user_id` (Xem lịch sử đồng bộ - Cần JWT)
6. `GET /health` (Kiểm tra hệ thống)
