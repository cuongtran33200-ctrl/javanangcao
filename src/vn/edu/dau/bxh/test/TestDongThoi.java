package vn.edu.dau.bxh.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import vn.edu.dau.bxh.core.BangXepHang;
import vn.edu.dau.bxh.model.DongXepHang;

/**
 * Do bang xep hang khi nhieu trong tai gui ket qua cung luc.
 * In so do: so tran, so doi, thoi gian, va kiem tra cac dong thuc can bang.
 *
 * Cach dung: java vn.edu.dau.bxh.test.TestDongThoi [soTrongTai] [soTranMoiNguoi]
 */
public final class TestDongThoi {

    private TestDongThoi() {
    }

    public static void main(String[] args) throws InterruptedException {
        int soTrongTai = args.length > 0 ? Integer.parseInt(args[0]) : 8;
        int soTranMoiNguoi = args.length > 1 ? Integer.parseInt(args[1]) : 200;
        String[] doi = {"A", "B", "C", "D", "E", "F"};

        BangXepHang bang = new BangXepHang();
        CountDownLatch xuatPhat = new CountDownLatch(1);
        CountDownLatch xong = new CountDownLatch(soTrongTai);
        ExecutorService hangDoi = Executors.newFixedThreadPool(soTrongTai);

        long batDau = System.nanoTime();
        for (int t = 0; t < soTrongTai; t++) {
            final int chiSoTrongTai = t;
            hangDoi.submit(() -> {
                try {
                    xuatPhat.await();
                    for (int i = 0; i < soTranMoiNguoi; i++) {
                        String nha = doi[(chiSoTrongTai + i) % doi.length];
                        String khach = doi[(chiSoTrongTai + i + 1) % doi.length];
                        bang.nhapKetQua(new vn.edu.dau.bxh.model.Tran(
                                nha, khach, i % 4, (i + 1) % 3));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    xong.countDown();
                }
            });
        }
        xuatPhat.countDown();
        xong.await();
        hangDoi.shutdown();
        hangDoi.awaitTermination(5, TimeUnit.SECONDS);
        long ketThuc = System.nanoTime();

        int mongDoi = soTrongTai * soTranMoiNguoi;
        List<DongXepHang> ds = bang.bangXepHang();
        int tongTran = ds.stream().mapToInt(DongXepHang::soTran).sum();
        int tongThang = ds.stream().mapToInt(DongXepHang::soThang).sum();
        int tongHoa = ds.stream().mapToInt(DongXepHang::soHoa).sum();
        int tongThua = ds.stream().mapToInt(DongXepHang::soThua).sum();
        int tongBanThang = ds.stream().mapToInt(DongXepHang::banThang).sum();
        int tongBanThua = ds.stream().mapToInt(DongXepHang::banThua).sum();

        System.out.printf("[test-dong-thoi] %d trong tai x %d tran = %d tran%n",
                soTrongTai, soTranMoiNguoi, mongDoi);
        System.out.printf("[test-dong-thoi] lich su ghi nhan: %d tran, %d doi%n",
                bang.soTran(), ds.size());
        System.out.printf("[test-dong-thoi] tong thang/hoa/thua: %d / %d / %d%n",
                tongThang, tongHoa, tongThua);
        System.out.printf("[test-dong-thoi] tong ban thang = ban thua: %d = %d%n",
                tongBanThang, tongBanThua);
        System.out.printf("[test-dong-thoi] thoi gian: %.1f ms%n",
                (ketThuc - batDau) / 1_000_000.0);

        boolean dat = bang.soTran() == mongDoi
                && tongTran == 2 * mongDoi
                && tongThang == tongThua
                && tongHoa % 2 == 0
                && tongBanThang == tongBanThua;
        System.out.println(dat
                ? "[test-dong-thoi] DAT: khong mat tran, so lieu can doi"
                : "[test-dong-thoi] LOI: so lieu lech, bang xep hang khong an toan luong");
        if (!dat) {
            System.exit(1);
        }
    }
}
