# Bảng xếp hạng giải đấu (Đ11 — AJP201)

Ứng dụng mạng nhiều máy khách: máy chủ TCP đa luồng nhận kết quả từ các trọng tài
và phát bảng xếp hạng cập nhật tới mọi khách đang xem. Trạng thái dùng chung được
đồng bộ đúng để nhiều trọng tài có thể gửi kết quả cùng lúc.

## Yêu cầu

- Java JDK 17 trở lên (đã thử nghiệm trên JDK 21). Chỉ dùng thư viện chuẩn
  (`java.util`, `java.util.concurrent`, `java.net`, `java.io`, `java.nio`, `java.time`).
- PowerShell (Windows) hoặc shell bất kỳ để biên dịch/chạy.

Nếu `javac`/`java` chưa có trên PATH, đặt biến `$jdk` trỏ tới thư mục JRE chứa
`bin/javac.exe`, ví dụ trên máy này:

```powershell
$jdk = "C:\java\eclipse-jee-2026-03-R-win32-x86_64\eclipse\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.10.v20260205-0638\jre"
```

## Cấu trúc

```
src/vn/edu/dau/bxh/
  model/        Tran (record), Doi, DongXepHang (record), Lenh (enum), YeuCau (record)
  exception/    LoiGiaoThucException, KetQuaKhongHopLeException
  core/         BangXepHang, QuyTacXepHang (Comparator thenComparing)
  protocol/     GiaoThuc (phân tích lệnh, đóng khung câu trả lời)
  luutru/       LuuTru (đọc/ghi CSV bằng try-with-resources)
  server/       MayChuBXH, PhienKhach
  client/       KhachXem, TrongTai
  test/         TestXepHang, TestDongThoi
```

## Biên dịch

```powershell
& "$jdk\bin\javac.exe" -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
```

Nếu `javac` đã có trên PATH:

```bash
javac -encoding UTF-8 -d out $(find src -name '*.java')
```

## Chạy

Mở 3 terminal.

**1) Máy chủ** (mặc định cổng 5000, số luồng = 0 nghĩa là cached):

```powershell
& "$jdk\bin\java.exe" -cp out vn.edu.dau.bxh.server.MayChuBXH 5000 0
```

**2) Khách xem** (nhận cập nhật bảng xếp hạng trực tuyến):

```powershell
& "$jdk\bin\java.exe" -cp out vn.edu.dau.bxh.client.KhachXem 127.0.0.1 5000
```

**3) Trọng tài** (gõ lệnh nhập kết quả):

```powershell
& "$jdk\bin\java.exe" -cp out vn.edu.dau.bxh.client.TrongTai 127.0.0.1 5000
```

Lệnh ví dụ trong client trọng tài:

```
SUBMIT HaNoi HaiPhong 2 1
SUBMIT DaNang Hue 0 0
STANDINGS
HISTORY HaNoi HaiPhong
EXPORT
QUIT
```

## Giao thức

Mỗi lệnh một dòng, phân tách bằng khoảng trắng. Tên đội **không chứa khoảng trắng**
(dùng `_` hoặc viết liền). Câu trả lời nhiều dòng được đóng khung bằng dòng tiêu đề
chứa số dòng kế tiếp.

| Lệnh | Tham số | Phản hồi |
|------|---------|----------|
| `WATCH` | — | `OK <số khách xem>` rồi gửi bảng xếp hạng hiện tại; sau đó tự nhận mỗi lần cập nhật |
| `SUBMIT` | `<độiNhà> <độiKhách> <bànNhà> <bànKhách>` | `OK <trận>`; phát bảng mới cho mọi khách đang xem |
| `STANDINGS` | — | `STANDINGS <n>` + 1 dòng tiêu đề + `n` dòng xếp hạng |
| `HISTORY` | `<độiA> <độiB>` | `HISTORY <n>` + `n` dòng trận đã gặp |
| `EXPORT` | — | `OK data/bang-xep-hang.csv` (cũng ghi `data/lich-su.csv`) |
| `QUIT` | — | `BYE` rồi đóng kết nối |

Lỗi giao thức / dữ liệu vô lý trả về `ERR <lý do>`.

## Tiêu chí xếp hạng

1. Điểm giảm dần (thắng 3, hòa 1).
2. Hiệu số giảm dần.
3. Bàn thắng giảm dần.
4. Phá đồng hạng bằng kết quả đối đầu (bảng nhỏ giữa các đội đồng ba chỉ số).
5. Cùng tất cả thì theo mã đội.

## Kiểm thử (không cần mạng)

```powershell
& "$jdk\bin\java.exe" -cp out vn.edu.dau.bxh.test.TestXepHang
& "$jdk\bin\java.exe" -cp out vn.edu.dau.bxh.test.TestDongThoi 8 200
```

- `TestXepHang`: kiểm tra thứ tự, phá đồng hạng bằng đối đầu, lịch sử đối đầu, chặn dữ liệu vô lý.
- `TestDongThoi`: n luồng trọng tài gửi đồng thời, kiểm tra không mất trận và các tổng cân đối.

## Kỹ thuật bắt buộc và nơi xuất hiện

- `record` trận: `model/Tran.java`, `model/DongXepHang.java`, `model/YeuCau.java`.
- `enum` lệnh: `model/Lenh.java` (tự khai báo số tham số).
- Ngoại lệ tự định nghĩa: `exception/LoiGiaoThucException.java`, `exception/KetQuaKhongHopLeException.java`.
- `Comparator.thenComparing`: `core/QuyTacXepHang.java` (điểm → hiệu số → bàn thắng → mã đội).
- `Map` đội: `core/BangXepHang.java` (`TreeMap` không phân biệt hoa thường).
- `CopyOnWriteArrayList` khách xem: `server/MayChuBXH.java`.
- `synchronized`: `core/BangXepHang.java` (trạng thái), `server/MayChuBXH.java` (khóa phát broadcast).
- Socket đa luồng + `ExecutorService`: `server/MayChuBXH.java`, `server/PhienKhach.java`.
- `try-with-resources` khi mở tệp/socket: `luutru/LuuTru.java`, `server/PhienKhach.java`, `client/*`.

## Ghi chú

- Máy chủ lưu/khôi phục `data/lich-su.csv` khi khởi động và khi tắt (Ctrl+C).
- Nếu tên đội có dấu tiếng Việt, chạy `chcp 65001` trong terminal trước khi `java`.
- Mở rộng (chưa làm): sinh lịch thi đấu vòng tròn tự động từ danh sách đội.

## Đóng gói jar

```powershell
& "$jdk\bin\jar.exe" --create --file bxh.jar --main-class vn.edu.dau.bxh.server.MayChuBXH -C out .
& "$jdk\bin\java.exe" -jar bxh.jar 5000 0
```
