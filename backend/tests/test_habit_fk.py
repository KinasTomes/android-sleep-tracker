import requests
import datetime

# URL của hệ thống FastAPI đang chạy trên Docker
BASE_URL = "http://localhost:8000/api/v1"

def test_habit_foreign_key_bug():
    print("🚀 Đang chạy Test kiểm tra lỗi Foreign Key của bảng HabitLog...")
    print("--------------------------------------------------------------")
    
    # Payload mô phỏng Celery Worker hoặc App cố gắng tạo một thông báo nhắc nhở "power_nap"
    payload = {
        "habit_id": "power_nap", 
        "scheduled_at": datetime.datetime.now(datetime.timezone.utc).isoformat(),
        "status": "pending"
    }

    try:
        # Gọi thẳng vào endpoint tạo HabitLog của API
        response = requests.post(f"{BASE_URL}/habits/log", json=payload)
    except requests.exceptions.ConnectionError:
        print("❌ Lỗi kết nối! Hãy chắc chắn rằng Docker (lumi-api) đang chạy ở port 8000.")
        return

    # Phân tích kết quả trả về
    if response.status_code == 200:
        print("✅ TEST PASSED (ĐÃ FIX LỖI):")
        print("Dữ liệu đã được lưu thành công vào Database!")
        print("Điều này chứng tỏ bảng `habits` ĐÃ ĐƯỢC SEED tự động lúc khởi động (món 'power_nap' đã có trên Menu).")
        print("Response:", response.json())
        
    elif response.status_code == 500:
        print("❌ TEST FAILED (CHƯA FIX - TÁI HIỆN LỖI):")
        print("Nhận được mã lỗi HTTP 500 Internal Server Error từ FastAPI.")
        print("Hệ thống đã bị crash vì lỗi Foreign Key Violation! Database từ chối insert do bảng `habits` đang trống.")
        print("Chi tiết lỗi:", response.text)
        
    else:
        print(f"⚠️ Có kết quả không mong đợi. HTTP Status: {response.status_code}")
        print("Chi tiết:", response.text)

if __name__ == "__main__":
    test_habit_foreign_key_bug()