package vn.edu.dau.bxh.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.dau.bxh.core.BangXepHang;
import vn.edu.dau.bxh.core.SinhLichVongTron;
import vn.edu.dau.bxh.luutru.LuuTru;
import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Tran;
import vn.edu.dau.bxh.protocol.GiaoThuc;

/**
 * May chu TCP da luong cua bang xep hang giai dau.
 *
 * Trang thai dung chung nam trong BangXepHang (synchronized).
 * Danh sach khach dang xem dung CopyOnWriteArrayList vi thao tac ghi
 * (phat bang xep hang) xuat hien nhieu hon thao tac them/xoa khach.
 */
public final class MayChuBXH implements AutoCloseable {

    private final ServerSocket serverSocket;
    private final ExecutorService hangDoi;
    private final int soLuong;
    private final BangXepHang bang = new BangXepHang();
    private final CopyOnWriteArrayList<PrintWriter> dsKhachXem = new CopyOnWriteArrayList<>();
    private final Object khoaGui = new Object();
    private final Path thuMucDuLieu;
    private volatile boolean dangChay = true;

    public MayChuBXH(int cong, int soLuong) throws IOException {
        this.serverSocket = new ServerSocket(cong);
        this.soLuong = soLuong;
        this.hangDoi = soLuong > 0
                ? Executors.newFixedThreadPool(soLuong)
                : Executors.newCachedThreadPool();
        this.thuMucDuLieu = Path.of("data");
        napLichSu();
    }

    private void napLichSu() {
        try {
            List<Tran> cu = LuuTru.docLichSu(thuMucDuLieu.resolve("lich-su.csv"));
            for (Tran tran : cu) {
                bang.nhapKetQua(tran);
            }
            System.out.printf("[may-chu] nap %d tran, %d doi tu tep%n", cu.size(), bang.soDoi());
        } catch (IOException e) {
            System.err.println("[may-chu] khong doc duoc lich su: " + e.getMessage());
        }
    }

    public void chay() throws IOException {
        String moTaLuong = soLuong > 0 ? ("co dinh " + soLuong) : "co dinh tuy theo tai";
        System.out.printf("[may-chu] nghe cong %d, bo luong: %s%n", serverSocket.getLocalPort(), moTaLuong);
        while (dangChay && !serverSocket.isClosed()) {
            try {
                Socket khach = serverSocket.accept();
                hangDoi.submit(new PhienKhach(khach, this));
            } catch (IOException e) {
                if (dangChay) {
                    System.err.println("[may-chu] loi nhan ket noi: " + e.getMessage());
                }
            }
        }
    }

    void themKhachXem(PrintWriter ra) {
        dsKhachXem.add(ra);
    }

    void xoaKhachXem(PrintWriter ra) {
        dsKhachXem.remove(ra);
    }

    int soKhachXem() {
        return dsKhachXem.size();
    }

    /** Nhap ket qua roi phat lai bang xep hang cho moi khach dang xem. */
    public void nhapKetQuaVaPhanPhat(Tran tran) {
        bang.nhapKetQua(tran);
        System.out.printf("[may-chu] tran moi: %s (tong %d tran, %d khach xem)%n",
                tran, bang.soTran(), dsKhachXem.size());
        phatBangXepHang();
    }

    void phatBangXepHang() {
        String goi = GiaoThuc.goiBangXepHang(bang.bangXepHang());
        // Khoa rieng cho viec gui: mot goi nhieu dong khong duoc xen vao goi khac.
        synchronized (khoaGui) {
            for (PrintWriter ra : dsKhachXem) {
                ra.print(goi);
                ra.flush();
                if (ra.checkError()) {
                    dsKhachXem.remove(ra);
                }
            }
        }
    }

    String goiBangXepHang() {
        return GiaoThuc.goiBangXepHang(bang.bangXepHang());
    }

    String goiLichSuDoiDau(String doiA, String doiB) {
        return GiaoThuc.goiLichSuDoiDau(doiA, doiB, bang.doiDau(doiA, doiB));
    }

    /** Sinh lịch vòng tròn một lượt từ danh sách đội client gửi lên. */
    String goiLichThiDau(List<String> doi) {
        return GiaoThuc.goiLichThiDau(SinhLichVongTron.sinhLich(doi));
    }

    /** Ghi ca lich su va bang xep hang ra thu muc data, tra ve tep bang xep hang. */
    public Path xuat() throws IOException {
        List<DongXepHang> ds = bang.bangXepHang();
        LuuTru.ghiLichSu(thuMucDuLieu.resolve("lich-su.csv"), bang.lichSu());
        Path tepBang = thuMucDuLieu.resolve("bang-xep-hang.csv");
        LuuTru.ghiBangXepHang(tepBang, ds);
        return tepBang;
    }

    @Override
    public void close() {
        dangChay = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("[may-chu] loi dong server socket: " + e.getMessage());
        }
        hangDoi.shutdownNow();
    }

    public static void main(String[] args) throws IOException {
        int cong = args.length > 0 ? Integer.parseInt(args[0]) : GiaoThuc.CONG_MAC_DINH;
        int soLuong = args.length > 1 ? Integer.parseInt(args[1]) : 0;
        MayChuBXH mayChu = new MayChuBXH(cong, soLuong);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                mayChu.xuat();
                System.out.println("[may-chu] da xuat du lieu ra data/");
            } catch (IOException e) {
                System.err.println("[may-chu] khong xuat duoc khi tat: " + e.getMessage());
            }
            mayChu.close();
        }, "dong-may-chu"));
        mayChu.chay();
    }
}
