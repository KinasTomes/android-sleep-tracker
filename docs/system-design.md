## 4. Tài liệu Thiết kế Hệ thống (System Design Document - SDD)
Thiết kế kiến trúc này được xây dựng xung quanh nguyên lý chia tách trách nhiệm (Separation of Concerns). Mobile app đóng vai trò là một "màn hình hiển thị câm" (dumb terminal) siêu mượt, nhận nhiệm vụ thu thập và hiển thị, trong khi mọi tác vụ tính toán phức tạp nhất được di chuyển lên nền tảng đám mây chuyên biệt.

### 4.1. Lựa chọn Ngăn xếp Công nghệ (Tech Stack) và Căn cứ Kỹ thuật
Theo yêu cầu của tổ chức, quá trình phát triển giai đoạn 1 sẽ hoàn toàn tập trung vào nền tảng hệ điều hành Android, bỏ qua iOS. Việc lựa chọn công nghệ nhằm phục vụ hệ thống có tính chất xử lý dữ liệu nặng về toán học và chuỗi thời gian.

#### a. Mobile Client (Giao diện Điện thoại Android):
- **Ngôn ngữ & Kiến trúc UI**: Ngôn ngữ Kotlin kết hợp với Jetpack Compose. Jetpack Compose cho phép xây dựng UI dạng khai báo (declarative UI) và quản lý trạng thái luồng dữ liệu phức tạp. Điều này cực kỳ quan trọng để vẽ các đồ thị năng lượng uốn lượn (spline charts) và thực hiện các hoạt ảnh chuyển đổi mượt mà giữa các vùng nhịp sinh học mà các hệ thống UI dựa trên XML cũ không làm tốt.
- **Lớp Tương tác Thiết bị**: Sử dụng SDK `HealthConnectClient` do Google cung cấp để truy xuất dữ liệu sức khỏe thông qua cơ chế giao tiếp liên tiến trình (IPC) với ứng dụng trung tâm Health Connect. Sử dụng thư viện `Coroutines` và `WorkManager` để quản lý các tác vụ xử lý nền (background jobs), lập lịch đánh thức thiết bị định kỳ để thu thập và đồng bộ dữ liệu giấc ngủ, khắc phục các quy định nghiêm ngặt về quản lý pin của Android.

#### b. Backend Services (Dịch vụ Đám mây Trung tâm):
- **Ngôn ngữ xử lý**: Ngôn ngữ Go (Golang) kết hợp với Framework Gin hoặc Echo. Lý do nền tảng: Go mang lại hiệu năng thực thi tương đương C++ nhưng với tốc độ phát triển nhanh và an toàn bộ nhớ. Khả năng xử lý hàng vạn Goroutines đồng thời giúp hệ thống tiếp nhận dữ liệu từ thiết bị đeo (IoT) cực kỳ mượt mà.
- **Thư viện Toán học**: Sử dụng `Gonum` (gonum.org). Đây là bộ thư viện tính toán khoa học chuẩn mực cho Go, cung cấp các công cụ đại số tuyến tính, giải phương trình vi phân và tối ưu hóa cần thiết để hiện thực hóa mô hình Borbély mà không cần phụ thuộc vào Python.
- **ORM & Database Driver**: Sử dụng `sqlx` hoặc `GORM` để tương tác với PostgreSQL/TimescaleDB, đảm bảo tính nhất quán về kiểu dữ liệu (Type-safety) ngay từ lớp ứng dụng.
- **Message Broker**: Apache Kafka hoặc RabbitMQ. Hoạt động như một bộ đệm giảm chấn (buffer). API Gateway viết bằng Go sẽ đẩy dữ liệu vào Kafka với độ trễ cực thấp.

#### c. Lớp Cơ sở Dữ liệu (Database Tier):
- **Cơ sở dữ liệu Cốt lõi**: Hệ quản trị quan hệ PostgreSQL kết hợp với siêu tiện ích mở rộng TimescaleDB.
- Lý do: Ứng dụng theo dõi giấc ngủ tạo ra dữ liệu hoàn toàn mang tính chất chuỗi thời gian (time-series). TimescaleDB biến đổi PostgreSQL bằng cơ chế bảng ảo Hypertable, tự động phân vùng (auto-partitioning) dữ liệu theo không gian thời gian. Kiến trúc này mang lại ưu thế tuyệt đối: Tốc độ chèn dữ liệu (insert) duy trì ở mức ổn định ngay cả khi bảng đạt hàng tỷ dòng (nhanh hơn 15 lần so với PostgreSQL truyền thống). Nó hỗ trợ nén dạng cột giảm dung lượng đĩa lên tới 90% và cung cấp các hàm chuyên biệt như time_bucket() để tổng hợp chuỗi dữ liệu nhịp tim/giấc ngủ một cách chớp nhoáng.
- **Lớp Bộ nhớ đệm (Caching)**: Redis in-memory data store. Kết quả biểu đồ năng lượng hàng ngày của từng cá nhân sẽ được kết xuất dưới dạng cấu trúc tĩnh và lưu vào Redis để phản hồi siêu tốc mỗi khi người dùng mở lại App.

### 4.2. Thiết kế Lược đồ Cơ sở Dữ liệu Thời gian thực (Time-Series Database Schema)
Để đáp ứng cả yêu cầu về lưu trữ thông tin nghiệp vụ và dữ liệu chuỗi thời gian tần suất cao, lược đồ (schema) được phân tách thành các bảng thông tin (metadata tables) truyền thống và các bảng chuỗi thời gian phân vùng (hypertables). Các khóa ngoại (foreign keys) liên kết hai cấu trúc này.

#### Bảng 3: Thông tin Tài khoản và Định chuẩn (Metadata Table) - users
Bảng này lưu trữ thông tin trạng thái ít biến động của người dùng, đóng vai trò căn cứ thiết lập hệ số thuật toán.

| Tên Cột (Column) | Kiểu Dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả & Chức năng |
|------------------|--------------------------|--------------------------|--------------------|
| `user_id` | UUID | PRIMARY KEY | Định danh duy nhất cho từng tài khoản hệ thống |
| `chronotype` | VARCHAR(50) | NOT NULL | Phân loại nhịp sinh học di truyền (Early Bird, Night Owl, Neutral) |
| `snop_hours` | DOUBLE PRECISION | DEFAULT 8.0 | Hằng số Nhu cầu Giấc ngủ tối ưu cá nhân hóa, được hiệu chuẩn liên tục |
| `current_sleep_debt` | DOUBLE PRECISION | DEFAULT 0.0 | Dữ liệu lưu trữ dư nợ giấc ngủ hiện tại (phút) để phản hồi API nhanh, cập nhật qua trigger |
| `created_at` | TIMESTAMPTZ | NOT NULL | Dấu thời gian tạo tài khoản |

#### Bảng 4: Kết quả Khảo sát (Metadata Table) - user_survey_responses
Bảng này lưu trữ các câu trả lời khảo sát để khởi tạo tham số thuật toán Seed Data.

| Tên Cột (Column) | Kiểu Dữ liệu (Data Type) | Ràng buộc | Mô tả |
|------------------|--------------------------|-----------|-------|
| `user_id` | UUID | FOREIGN KEY | Liên kết với bảng users |
| `question_id` | VARCHAR(50) | NOT NULL | Định danh câu hỏi (ví dụ: goal, health_issues, sleep_schedule) |
| `answer_key` | TEXT | NOT NULL | Giá trị câu trả lời hoặc JSON chứa thông tin chi tiết |

#### Bảng 5: Phiên Giấc ngủ (Hypertable) - sleep_sessions
Bảng này lưu trữ các khối thời gian giấc ngủ tổng quát. Nó đóng vai trò trực tiếp trong việc tính toán nợ giấc ngủ 14 ngày.

| Tên Cột (Column) | Kiểu Dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả & Chức năng |
|------------------|--------------------------|--------------------------|--------------------|
| `session_id` | UUID | UNIQUE | Mã định danh riêng biệt của từng phiên ngủ |
| `user_id` | UUID | FOREIGN KEY | Tham chiếu liên kết đến bảng users |
| `start_time` | TIMESTAMPTZ | NOT NULL | Thời điểm bắt đầu ngủ (timestamp index) |
| `end_time` | TIMESTAMPTZ | NOT NULL | Thời điểm thức dậy hoàn toàn |
| `duration_mins` | INTEGER | NOT NULL | Tổng thời lượng ngủ thực tế (phút), loại trừ các lần tỉnh giữa chừng |
| `session_type` | VARCHAR(20) | NOT NULL | Phân loại: nightly (ngủ đêm) hoặc nap (ngủ trưa) |

*Lệnh Tối ưu hóa TimescaleDB*: Bảng này sẽ được chuyển đổi thành Hypertable dựa trên mốc thời gian bắt đầu ngủ.

```sql
SELECT create_hypertable('sleep_sessions', 'start_time');
```
#### Bảng 5: Sinh trắc học Tần suất cao (Hypertable) - biometric_data
Bảng này tiếp nhận khối lượng dữ liệu khổng lồ sinh ra từ các cảm biến vi mô liên tục (nhịp tim, nồng độ oxy, trạng thái giấc ngủ sâu/nông mỗi vài phút).

| Tên Cột (Column) | Kiểu Dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả & Chức năng |
|------------------|--------------------------|--------------------------|--------------------|
| `user_id` | UUID | NOT NULL | Định danh người dùng. Không dùng khóa ngoại (FK constraint) để tối ưu hiệu suất ghi trên hypertable lớn |
| `time` | TIMESTAMPTZ | NOT NULL | Dấu thời gian ghi nhận dữ liệu tại một mili-giây cụ thể. Khóa phân vùng (partition key) |
| `metric_type` | VARCHAR(50) | NOT NULL | Cờ nhận diện loại dữ liệu: heart_rate_bpm, sleep_stage_rem, sleep_stage_deep |
| `value` | DOUBLE PRECISION | NOT NULL | Giá trị thực tế tại thời điểm đó (ví dụ: 65.5 bpm) |

*Lệnh Tối ưu hóa TimescaleDB*: Tốc độ phình to của bảng này yêu cầu cấu hình tự động phân vùng (chunking theo ngày) và nén dữ liệu kiểu cột mạnh mẽ, kết hợp với các truy vấn tập hợp liên tục (Continuous Aggregates) để làm mịn dữ liệu.

```sql
SELECT create_hypertable('biometric_data', 'time', chunk_time_interval => INTERVAL '1 day');
ALTER TABLE biometric_data SET (
  timescaledb.compress,
  timescaledb.compress_segmentby = 'user_id, metric_type',
  timescaledb.compress_orderby = 'time DESC'
);
SELECT add_compression_policy('biometric_data', INTERVAL '7 days');
```

### 4.3. Luồng Kiến trúc Hệ thống Tổng thể (Architecture Flow)
Hệ thống hoạt động theo một vòng tuần hoàn khép kín, từ thiết bị người dùng lên đám mây và phản hồi ngược trở lại. Mô tả luồng dữ liệu từ quá trình thu thập thụ động đến lúc render biểu đồ trên thiết bị Android:

#### a. Thu thập và Tích lũy Cục bộ (Data Generation & On-Device Aggregation):
Suốt đêm, smartwatch, fitness band hoặc cảm biến chuyển động trên smartphone sẽ thu thập các chỉ số nhịp tim và trạng thái bất động của người dùng. Các ứng dụng phần cứng nội bộ của bên thứ 3 ghi các dữ liệu này vào Cổng trung tâm `Android Health Connect APK`. Dữ liệu lúc này nằm ở bộ nhớ nội bộ an toàn của thiết bị (On-Device Storage).

#### b. Khởi tạo Đồng bộ Hậu cảnh (Background Data Extraction):
Vào mỗi buổi sáng, ngay cả trước khi người dùng mở màn hình ứng dụng, hệ thống `WorkManager` của Android (được tích hợp trong app) sẽ nhận diện sự kết thúc của một giấc ngủ dài. Service chạy nền sẽ sử dụng `HealthConnectClient` thực hiện lời gọi hàm `readRecords()` với bộ lọc thời gian `TimeRangeFilter` từ lần đồng bộ cuối cùng. Các bản ghi `SleepSessionRecord` được trích xuất, làm phẳng (flattened) và đóng gói thành một cấu trúc JSON thống nhất.

#### c. Tiếp nhận Đám mây và Giảm chấn (Cloud Ingestion & Buffering):
Gói JSON được truyền tải qua HTTPS TLS 1.3 đến API Gateway của hệ thống Backend viết bằng Go. Tại đây, Gateway làm nhiệm vụ kiểm tra bảo mật JWT (JSON Web Token) để xác thực người dùng, sau đó đẩy (publish) dữ liệu này vào luồng sự kiện (topic) của Apache Kafka. Hiệu năng của Go giúp quá trình này diễn ra gần như tức thời, ngăn ngừa tắc nghẽn máy chủ.

#### d. Xử lý và Lưu trữ Chuỗi thời gian (Storage Worker):
Các Consumer nodes (Worker viết bằng Go) liên tục đọc dữ liệu từ hàng đợi Kafka. Chúng tách các siêu dữ liệu và thực hiện thao tác INSERT batch thẳng vào các hypertables `sleep_sessions` và `biometric_data` bên trong PostgreSQL/TimescaleDB.

#### e. Công cụ Tính toán Toán học (Analytics & Algorithm Engine):
Sau khi dữ liệu được lưu thành công, hệ thống kích hoạt "Go Algorithm Engine". Engine này sử dụng thư viện `Gonum` để thực thi các phép tính vi phân cho Process S và Process C, xuất ra chuỗi tọa độ (x, y) cho 24 giờ tiếp theo.

#### f. Lưu trữ Bộ đệm và Hiển thị (Caching & UI Rendering):
Chuỗi tọa độ năng lượng vừa được tổng hợp sẽ được tuần tự hóa (serialized) và lưu trực tiếp vào Redis Cache, gán với Key là `user_id` và thời gian tồn tại (TTL) là 24 giờ. Cùng lúc đó, khi người dùng vừa pha xong cà phê và mở ứng dụng, màn hình Android Compose lập tức gọi API GET đến hệ thống. Backend lấy dữ liệu ngay lập tức từ Redis (độ trễ dưới 50ms) trả về. Khung UI Jetpack Compose tiếp nhận JSON, nội suy các đường thẳng thành đường cong spline mượt mà, phân chia các khối màu cho các vùng năng lượng và cập nhật bảng đồng hồ Nợ giấc ngủ.

### 4.4. Triển khai Logic Thuật toán Nhịp sinh học trên Hệ thống Go
Chất lượng của giải pháp nằm ở việc mã hóa chính xác các mô hình lý thuyết khoa học thành các hàm Go hiệu quả cao. Hệ thống Go Backend chạy chu trình tái tính toán (re-calibration) theo 4 bước tuần tự.

#### Bước 1: Tính toán Khối lượng Nợ giấc ngủ cấp tính (Sleep Debt Aggregation)
Hệ thống truy xuất dữ liệu từ hypertable `sleep_sessions` và bảng `user_survey_responses`. Thuật toán ưu tiên sử dụng dữ liệu thực nghiệm (Health Connect). Nếu dữ liệu lịch sử chưa đủ 14 ngày, hệ thống sử dụng kết quả khảo sát (Seed Data) để nội suy giá trị SNOP và pha sinh học khởi điểm. Sử dụng Goroutines để tính toán song song...

#### Bước 2: Xây dựng Đường cong Áp lực Nội môi (Process S Modeler)
Sử dụng hàm `math.Exp` của Go để tính toán sự tích tụ Adenosine:
$H(t) = 1 - (1 - H(t_0)) \cdot e^{\frac{-(t-t_0)}{18.2}}$

#### Bước 3: Xây dựng Đường cong Nhịp sinh học (Process C Modeler)
Triển khai phương trình 5 sóng hài bằng các hàm `math.Sin` và `math.Pi`. Việc sử dụng kiểu dữ liệu `float64` trong Go đảm bảo độ chính xác cần thiết cho các phép tính y sinh.

#### Bước 4: Hợp nhất và Thiết lập Hệ thống Cảnh báo (Synthesis & Nudge Scheduling)
Sử dụng các cấu trúc dữ liệu `Slice` hiệu quả của Go để lưu trữ mảng 1440 điểm năng lượng. Thuật toán tìm điểm cực trị được tối ưu hóa để chạy trong thời gian thực, đảm bảo phản hồi API siêu tốc.
