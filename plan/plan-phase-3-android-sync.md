# Phase 3: Android Core & Health Connect Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Khởi tạo App Android, xin quyền và đồng bộ dữ liệu Health Connect trong chế độ nền.

**Architecture:** MVVM với Jetpack Compose và WorkManager để xử lý tác vụ nền (background sync).

**Tech Stack:** Kotlin, Jetpack Compose, Health Connect SDK, WorkManager, Retrofit.

---

### Task 1: Thiết lập Android App & Quyền Health Connect

- [ ] **Step 1: Khởi tạo dự án Android mới trong thư mục `android/`**
- [ ] **Step 2: Khai báo các quyền `READ_SLEEP`, `READ_HEART_RATE` trong Manifest và Health Connect metadata**
- [ ] **Step 3: Xây dựng màn hình Onboarding xin quyền truy cập dữ liệu và Commit**

### Task 2: Background Sync với WorkManager

- [ ] **Step 1: Tạo `SleepSyncWorker` kế thừa từ `CoroutineWorker`**
- [ ] **Step 2: Viết logic trích xuất `SleepSessionRecord` từ `HealthConnectClient`**
- [ ] **Step 3: Sử dụng Retrofit để POST dữ liệu về `/sync/sleep` API (đã viết ở Phase 1)**
- [ ] **Step 4: Lập lịch Worker chạy định kỳ (PeriodicWorkRequest) và Commit**
