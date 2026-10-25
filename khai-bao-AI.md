# Phiếu khai báo sử dụng công cụ AI
## Đề tài AJP201 — Đ11: Bảng xếp hạng giải đấu

**Nhóm:** [Nhóm XX]
**Thành viên:**
- Trần Viết Cường — Lớp 24CT1 — MSSV: [..........]
- Nguyễn Xuân Công — Lớp 24CT1 — MSSV: [..........]

**Giảng viên hướng dẫn:** TS. Đỗ Phúc Hảo

---

### 1. Công cụ AI đã sử dụng

| Công cụ | Nguồn | Mục đích |
|---|---|---|
| XAI247 Code (GLM) | Trợ lý lập trình trong VS Code | Lên cấu trúc dự án, viết mã, viết báo cáo, giải thích |

Nhóm không sử dụng công cụ AI nào khác (không dùng ChatGPT, Copilot, Gemini...)
trong quá trình làm đồ án. Nếu có thay đổi, nhóm sẽ bổ sung vào phiếu này.

### 2. Pha sử dụng

| Pha | Dùng AI | Vai trò của AI | Vai trò của nhóm |
|---|---|---|---|
| Chọn đề và lập kế hoạch | Có | Gợi ý bước làm, ánh xạ lịch 12 tuần | Quyết định chọn Đ11, thống nhất phạm vi |
| Thiết kế | Có | Đề xuất bảng lệnh, sơ đồ lớp, giao thức | Xem xét, chỉnh sửa, chốt thiết kế |
| Viết mã nguồn | Có | Viết phần lớn mã theo chỉ dẫn | Đọc hiểu, chạy thử, sửa lỗi, bổ sung |
| Kiểm thử | Có | Viết các ca kiểm thử và chạy thử | Kiểm tra kết quả, xác nhận đúng/sai |
| Viết báo cáo LaTeX | Có | Soạn nội dung chương, vẽ sơ đồ TikZ | Điền thông tin nhóm, đối chiếu số liệu |
| Bảo vệ | Không | — | Tự chuẩn bị, không dùng AI lúc bảo vệ |

### 3. Phần tự viết và phần có hỗ trợ AI

**Phần nhóm tự viết và hiểu rõ, có thể giải thích ngay tại buổi bảo vệ:**

- Quy tắc xếp hạng và lý do chọn `thenComparing` điểm → hiệu số → bàn thắng.
- Logic phá đồng hạng bằng kết quả đối đầu (mini-table giữa các đội đồng chỉ số).
- Xác thực dữ liệu trong hàm tạo compact của record `Tran` (tỉ số âm, cùng đội,
  tên có khoảng trắng).
- Cách đặt `synchronized` ở mức thao tác nghiệp vụ (nhập một trận) thay vì mức
  từng trường.
- Lý do chọn `CopyOnWriteArrayList` cho danh sách khách xem và khoá riêng
  `khoaGui` khi phát broadcast.
- Cấu trúc gói và luồng giao thức: `WATCH`, `SUBMIT`, `STANDINGS`, `HISTORY`,
  `EXPORT`, `QUIT`.

**Phần có sự hỗ trợ của AI nhưng nhóm đã đọc, hiểu và chỉnh sửa:**

- Lớp `MayChuBXH` và `PhienKhach` (server đa luồng, broadcast).
- Lớp `LuuTru` (đọc/ghi CSV bằng `try-with-resources`).
- Hai lớp kiểm thử `TestXepHang`, `TestDongThoi`.
- Nội dung các chương báo cáo LaTeX.

### 4. Cam kết

Chúng tôi cam đoan hiểu toàn bộ mã nguồn đã nộp. Tại buổi bảo vệ, thành viên
được giảng viên bốc ngẫu nhiên sẽ giải thích được đoạn mã được yêu cầu và thực
hiện được sửa chi tiết tại chỗ (đổi tiêu chí sắp xếp, đổi số luồng, đổi cổng)
trong thời gian quy định. Nếu không giải thích được, điểm cá nhân của thành viên
đó tối đa 50% điểm nhóm, theo quy định của học phần.

**Ký tên:**

<br>

Trần Viết Cường .................................

<br>

Nguyễn Xuân Công .................................

*Ngày ____ tháng ____ năm 2026*
