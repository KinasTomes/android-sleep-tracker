## 3. Đặc tả Yêu cầu Phần mềm (Software Requirement Specification - SRS)
Phần này quy định chi tiết các đặc điểm kỹ thuật mà đội ngũ kỹ sư phải hiện thực hóa. Để tạo ra trải nghiệm liền mạch, ứng dụng phải che giấu sự phức tạp toán học bên dưới một giao diện người dùng trực quan, phản hồi nhanh.

### 3.1. Các Yêu cầu Chức năng (Functional Requirements)
Hệ thống phải đáp ứng đầy đủ các chức năng cốt lõi sau để đảm bảo chu trình tính toán nhịp sinh học diễn ra không có điểm chết (dead end).

#### Bảng 2: Danh sách Đặc tả Yêu cầu Chức năng (FRs)
| Mã FR | Tên Chức năng (Feature) | Mô tả Chi tiết Luồng Nghiệp vụ | Ràng buộc Kỹ thuật & Chấp nhận (Acceptance Criteria) |
|------|--------------------------|--------------------------------|------------------------------------------------------|
| FR-01 | Đăng ký & Định chuẩn Sinh học (Onboarding & SNOP Calibration) | Xây dựng luồng hội thoại thiết lập hồ sơ. Thu thập thông tin nhân khẩu học (Tuổi, Giới tính, Thói quen sinh hoạt). Quan trọng nhất: Kích hoạt quyền truy cập Health Connect để kéo lịch sử giấc ngủ trong quá khứ nhằm thiết lập ngưỡng SNOP (Sleep Need) cơ sở. | Phải hiển thị màn hình phân tích đồ họa (loading screen) minh bạch về cách hệ thống đang xử lý dữ liệu. Cho phép người dùng ghi đè thủ công (manual override) giá trị SNOP nếu họ cảm thấy máy tính toán sai lệch. |
| FR-02 | Tích hợp Nguồn Dữ liệu Y tế (Health Connect API Sync) | Khởi tạo giao thức kết nối IPC với ứng dụng Android Health Connect. Đọc luồng dữ liệu SleepSessionRecord, HeartRateRecord và truyền tải (sync) các gói JSON lên máy chủ đám mây một cách đồng bộ mỗi khi ứng dụng được mở. | Yêu cầu cấp quyền READ_SLEEP rõ ràng. Nếu người dùng từ chối, hệ thống phải kích hoạt cơ chế thoái lui (graceful degradation) bằng cách yêu cầu nhập thời gian ngủ thủ công. Tuân thủ vòng đời Background Service của Android. |
| FR-03 | Trang tính toán Nợ Giấc ngủ (Sleep Debt Dashboard) | Tính tổng lượng chênh lệch giữa Nhu cầu Giấc ngủ (SNOP) và thời gian ngủ thực tế trong khoảng thời gian trượt 14 ngày. Cập nhật và hiển thị con số chính xác trên màn hình trung tâm (ví dụ: "4h 30m Nợ"). | Tính toán là chuỗi thời gian liên tục. Phải cộng gộp các bản ghi giấc ngủ trưa (nap) với kiểu sleep_type hợp lệ vào tổng thời lượng ngủ hàng ngày trước khi tính phần dư nợ. |
| FR-04 | Trình diễn Biểu đồ Lịch trình Năng lượng (Energy Schedule Rendering) | Hiển thị biểu đồ dạng sóng cong (spline curve) 24 giờ phản ánh hàm tương tác H(t) và C(k). UI cần sử dụng đổ bóng màu (color-coding) để định nghĩa rõ các vùng: Wake Zone, Morning/Evening Peaks, Afternoon Dip, và Melatonin Window. | Phải tính toán và vẽ lại toàn bộ đường cong mỗi sáng ngay sau khi tải bản ghi giấc ngủ đêm qua, vì thời gian thức dậy thay đổi sẽ dịch chuyển toàn bộ các pha phía sau. Hỗ trợ cuộn ngang vô cực (infinite horizontal scroll) qua các ngày. |
| FR-05 | Hệ thống Cảnh báo & Báo thức Thông minh (Smart Alarms & Nudge Habits) | Tự động kích hoạt 16 thói quen khoa học dưới dạng push notification (ví dụ: cảnh báo cắt giảm ánh sáng xanh khi bắt đầu Melatonin Window, nhắc nhở uống cà phê). Kích hoạt báo thức rung đa tần số thông minh để đánh thức người dùng khỏi chu kỳ ngủ nông. | Lịch trình gửi Notification phải linh động, không cố định giờ mà dịch chuyển theo hàm của nhịp sinh học ngày hôm đó. Chuông báo thức phải hoạt động ổn định trên OS, vượt qua chế độ tiết kiệm pin Doze Mode của Android bằng AlarmManager/WorkManager. |
| FR-06 | Tích hợp Lịch trình Công việc (Calendar Integration) | Cung cấp kết nối 2 chiều với Google Calendar/Outlook. Hệ thống tự động phân tích các khe thời gian trống trong các vùng Đỉnh Năng lượng (Peaks) và đề xuất sắp xếp (hoặc cảnh báo xung đột) các công việc đòi hỏi sự tập trung cao. | Đảm bảo tính nhất quán của token xác thực OAuth 2.0. Không lưu trữ nội dung chi tiết sự kiện nhằm bảo vệ quyền riêng tư, chỉ đánh dấu và quét định dạng khối thời gian (time-blocks). |

### 3.2. Các Yêu cầu Phi chức năng (Non-Functional Requirements)
Ứng dụng liên quan đến y tế đòi hỏi các tiêu chuẩn chất lượng nền tảng khắc nghiệt, không chỉ để đảm bảo trải nghiệm người dùng mà còn liên quan đến tính tuân thủ pháp lý.

#### a. Bảo mật Dữ liệu và Quyền riêng tư (Data Security & Privacy Compliance):
Thông tin sức khỏe cá nhân (PHI - Protected Health Information) bao gồm nhịp tim, dữ liệu giấc ngủ, và vị trí địa lý phải được bảo vệ tuyệt đối. Hệ thống cần tuân thủ các quy chuẩn cơ sở của HIPAA hoặc GDPR, mặc dù không phải là một thiết bị y tế.
- `Mã hóa`: Tất cả dữ liệu truyền tải giữa Mobile Client và Backend phải sử dụng chuẩn mã hóa TLS 1.3 (Encryption in transit). Tại cấp độ cơ sở dữ liệu (Database), dữ liệu phải được mã hóa theo chuẩn AES-256 (Encryption at rest).
- `Phi cá nhân hóa`: Thuật toán Machine Learning và phân tích phân tích trên đám mây (Cloud Analytics) chỉ được phép sử dụng các gói dữ liệu đã được ẩn danh hóa (anonymized), loại bỏ các mã định danh người dùng (PII) theo cơ chế băm mã độc lập.

#### b. Độ trễ Xử lý và Hiệu suất (Latency & Performance):
Mặc dù thuật toán toán học Process S và Process C đòi hỏi nhiều tài nguyên điện toán, trải nghiệm người dùng không thể bị gián đoạn.
- Phản hồi cho các lệnh truy xuất API render biểu đồ (RESTful GET requests) phải có thời gian xử lý tải (p95 latency) dưới 200 ms để duy trì sự mượt mà khi người dùng cuộn biểu đồ UI.
- Quá trình tính toán lại chỉ số nợ giấc ngủ (batch calculation) nên được thực hiện bất đồng bộ trên hàng đợi (queue) thay vì chặn luồng chính (main thread).

#### c. Khả năng Mở rộng và Thông lượng Hệ thống (Scalability & High Throughput):
Đặc thù của kiến trúc thiết bị đo sức khỏe (wearable IoT) là tần suất phát sinh dữ liệu liên tục theo dạng chuỗi thời gian (time-series).

Kiến trúc Backend và Database phải được thiết kế để xử lý khối lượng lớn các hoạt động ghi (INSERT-heavy workload) cùng lúc vào thời điểm đầu buổi sáng khi hàng triệu người dùng thức dậy và ứng dụng đồng bộ dữ liệu. Hệ thống phải có khả năng mở rộng ngang (horizontal scaling) để đáp ứng tối thiểu 10.000 TPS (Transactions Per Second) mà không bị "thắt cổ chai" (bottleneck) ở lớp lưu trữ, đồng thời không làm chậm quá trình truy vấn (SELECT) để tải ứng dụng.