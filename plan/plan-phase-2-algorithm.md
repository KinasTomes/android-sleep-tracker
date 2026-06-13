# Phase 2: Core Algorithm Implementation Plan (Golang)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Hiện thực hóa mô hình Borbély (Process S, C) bằng ngôn ngữ Go để tính toán nợ giấc ngủ và biểu đồ năng lượng 24h.

**Architecture:** Sử dụng thư viện `Gonum` cho các phép tính toán học phức tạp và package `math` chuẩn của Go.

**Tech Stack:** Go (Golang), Gonum.

---

### Task 1: Module tính toán Nợ giấc ngủ (Sleep Debt)

- [ ] **Step 1: Viết hàm `CalculateSleepDebt` lấy dữ liệu 14 ngày từ DB qua Repository**
- [ ] **Step 2: Triển khai thuật toán EMA (Exponential Moving Average) bằng Go để tính SNOP thực tế**
- [ ] **Step 3: Viết test case kiểm chứng con số nợ giấc ngủ và Commit**

### Task 2: Module mô phỏng Process S & Process C

- [ ] **Step 1: Viết hàm giải phương trình vi phân cho Process S sử dụng `math.Exp`**
- [ ] **Step 2: Viết hàm tính toán Process C dựa trên chuỗi 5 sóng hài (sử dụng `math.Sin` và `math.Pi`)**
- [ ] **Step 3: Viết hàm `GenerateEnergySchedule` để hợp nhất S và C thành mảng `[]float64` độ dài 1440**
- [ ] **Step 4: Thuật toán tìm điểm cực trị trong Slice năng lượng để xác định Peaks và Melatonin Window và Commit**
