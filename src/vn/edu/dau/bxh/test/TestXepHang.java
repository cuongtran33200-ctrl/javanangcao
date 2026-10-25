package vn.edu.dau.bxh.test;

import java.util.List;

import vn.edu.dau.bxh.core.BangXepHang;
import vn.edu.dau.bxh.exception.KetQuaKhongHopLeException;
import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Tran;

/**
 * Kiem thu tang du lieu va quy tac xep hang, khong can mang.
 * Chay: java vn.edu.dau.bxh.test.TestXepHang
 */
public final class TestXepHang {

    private static int soDung;
    private static int soLoi;

    private TestXepHang() {
    }

    public static void main(String[] args) {
        kiemThuBangCoBan();
        kiemThuPhaDongHangBangDoiDau();
        kiemThuLichSuDoiDau();
        kiemThuChanDuLieuVoLy();

        System.out.printf("%n[test-xep-hang] DUNG: %d, LOI: %d%n", soDung, soLoi);
        if (soLoi > 0) {
            System.exit(1);
        }
    }

    private static void kiemThuBangCoBan() {
        BangXepHang bang = new BangXepHang();
        bang.nhapKetQua(new Tran("An", "Binh", 2, 1));
        bang.nhapKetQua(new Tran("Cuong", "Dung", 3, 0));
        bang.nhapKetQua(new Tran("An", "Cuong", 0, 0));
        bang.nhapKetQua(new Tran("Binh", "Dung", 1, 1));

        List<DongXepHang> ds = bang.bangXepHang();
        inBang("bang co ban", ds);

        bangBang("Cuong nhat", "Cuong", ds.get(0).maDoi());
        bangBang("An nhi", "An", ds.get(1).maDoi());
        bangBang("Binh ba", "Binh", ds.get(2).maDoi());
        bangBang("Dung tu", "Dung", ds.get(3).maDoi());
        bangBang("An co 4 diem", 4, tim(ds, "An").diem());
        bangBang("An hieu so +1", 1, tim(ds, "An").hieuSo());
        bangBang("Cuong hieu so +3", 3, tim(ds, "Cuong").hieuSo());
        bangBang("Dung 1 ban thang", 1, tim(ds, "Dung").banThang());
        bangBang("so doi", 4, ds.size());
        bangBang("so tran", 4, bang.soTran());
    }

    private static void kiemThuPhaDongHangBangDoiDau() {
        // Ba doi cung 3 diem. Z dat dau nho hieu so cao hon.
        // X va Y dong ca ba chi so (diem, hieu so, ban thang) nhung X thang Y
        // trong tran doi dau -> X tren Y.
        BangXepHang bang = new BangXepHang();
        bang.nhapKetQua(new Tran("X", "Y", 2, 0));
        bang.nhapKetQua(new Tran("X", "Z", 0, 3));
        bang.nhapKetQua(new Tran("Y", "Z", 2, 1));

        List<DongXepHang> ds = bang.bangXepHang();
        inBang("pha dong hang X-Y", ds);

        bangBang("Z nhat nho hieu so", "Z", ds.get(0).maDoi());
        bangBang("X nhi nho thang doi dau", "X", ds.get(1).maDoi());
        bangBang("Y ba", "Y", ds.get(2).maDoi());
        bangBang("X va Y dong ba chi so", tim(ds, "X").chiSo(), tim(ds, "Y").chiSo());
    }

    private static void kiemThuLichSuDoiDau() {
        BangXepHang bang = new BangXepHang();
        bang.nhapKetQua(new Tran("An", "Binh", 1, 0));
        bang.nhapKetQua(new Tran("Binh", "An", 2, 2));
        bang.nhapKetQua(new Tran("An", "Cuong", 1, 1));

        bangBang("An-Binh gap 2 tran", 2, bang.doiDau("An", "Binh").size());
        bangBang("Binh-An gap 2 tran", 2, bang.doiDau("Binh", "An").size());
        bangBang("An-Cuong gap 1 tran", 1, bang.doiDau("An", "Cuong").size());
    }

    private static void kiemThuChanDuLieuVoLy() {
        thuLoi("ti so am", () -> new Tran("An", "Binh", -1, 0));
        thuLoi("cung mot doi", () -> new Tran("An", "An", 1, 0));
        thuLoi("ten doi co khoang trang", () -> new Tran("An Binh", "Cuong", 1, 0));
        thuLoi("thieu ten doi", () -> new Tran("", "Cuong", 1, 0));
    }

    // --- bo tro ---

    private interface PhepThu {
        void chay();
    }

    private static void thuLoi(String moTa, PhepThu hanhDong) {
        try {
            hanhDong.chay();
            soLoi++;
            System.out.println("[test-xep-hang] LOI: khong nem ngoai le cho: " + moTa);
        } catch (KetQuaKhongHopLeException e) {
            soDung++;
            System.out.println("[test-xep-hang] DUNG: chan " + moTa + " -> " + e.getMessage());
        }
    }

    private static DongXepHang tim(List<DongXepHang> ds, String ma) {
        for (DongXepHang d : ds) {
            if (d.maDoi().equalsIgnoreCase(ma)) {
                return d;
            }
        }
        throw new AssertionError("Khong tim thay doi " + ma);
    }

    private static void bangBang(String moTa, Object mongDoi, Object thucTe) {
        if (mongDoi.equals(thucTe)) {
            soDung++;
            System.out.println("[test-xep-hang] DUNG: " + moTa + " = " + thucTe);
        } else {
            soLoi++;
            System.out.println("[test-xep-hang] LOI: " + moTa + " mong " + mongDoi + " nhung duoc " + thucTe);
        }
    }

    private static void inBang(String ten, List<DongXepHang> ds) {
        System.out.println("--- " + ten + " ---");
        System.out.println(DongXepHang.tieuDeBang());
        int hang = 1;
        for (DongXepHang d : ds) {
            System.out.println(d.toDong(hang++));
        }
    }
}
