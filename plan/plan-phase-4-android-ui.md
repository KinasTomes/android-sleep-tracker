# Phase 4: Energy Schedule UI & Smart Notifications Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Hiển thị biểu đồ năng lượng spline và gửi thông báo nhắc nhở thói quen theo thời gian thực.

**Architecture:** Sử dụng Compose Canvas để vẽ đồ thị động dựa trên dữ liệu từ Algorithm Engine.

**Tech Stack:** Jetpack Compose (Canvas), Android Notification System.

---

### Task 1: Vẽ biểu đồ Lịch trình Năng lượng (Energy Schedule Graph)

- [ ] **Step 1: Xây dựng Repository lấy dữ liệu Energy Schedule từ Backend API**
- [ ] **Step 2: Vẽ đường cong Spline bằng `drawPath` trong Compose Canvas**
- [ ] **Step 3: Thêm các layer màu sắc để phân biệt vùng Peak (Vàng), Dip (Xám), Melatonin (Tím) và Commit**

### Task 2: Hệ thống Thông báo & Báo thức thông minh

- [ ] **Step 1: Viết logic `HabitReminderManager` để đặt lịch thông báo cục bộ**
- [ ] **Step 2: Kích hoạt thông báo tại thời điểm bắt đầu "Melatonin Window"**
- [ ] **Step 3: Tích hợp báo thức rung dựa trên chu kỳ ngủ (Smart Alarm) và Commit**
