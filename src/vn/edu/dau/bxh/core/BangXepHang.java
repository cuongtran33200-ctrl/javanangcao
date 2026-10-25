package vn.edu.dau.bxh.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vn.edu.dau.bxh.model.Doi;
import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Tran;

/**
 * Trang thai dung chung cua giai: danh sach doi va lich su tran.
 * Moi thao tac doc/ghi deu synchronized de nhieu trong tai co the
 * gui ket qua cung luc ma bang xep hang van dung va khong trung lap.
 */
public final class BangXepHang {

    // TreeMap khong phan biet hoa thuong de "HaNoi" va "hanoi" la cung mot doi.
    private final Map<String, Doi> dsDoi = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final List<Tran> lichSu = new ArrayList<>();

    /** Nhap mot tran moi, cap nhat thong ke hai doi trong cung mot khoa. */
    public synchronized void nhapKetQua(Tran tran) {
        dsDoi.computeIfAbsent(tran.doiNha(), ten -> new Doi(ten, ten)).ghiNhan(tran, true);
        dsDoi.computeIfAbsent(tran.doiKhach(), ten -> new Doi(ten, ten)).ghiNhan(tran, false);
        lichSu.add(tran);
    }

    /** Tra ve anh chup bang xep hang da pha dong hang, bat bien. */
    public synchronized List<DongXepHang> bangXepHang() {
        List<DongXepHang> ds = new ArrayList<>(dsDoi.size());
        for (Doi doi : dsDoi.values()) {
            ds.add(doi.dongXepHang());
        }
        return QuyTacXepHang.xepHang(ds, List.copyOf(lichSu));
    }

    public synchronized List<Tran> lichSu() {
        return List.copyOf(lichSu);
    }

    public synchronized int soTran() {
        return lichSu.size();
    }

    public synchronized int soDoi() {
        return dsDoi.size();
    }

    /** Cac tran giua hai doi bat ky, dung cho lenh HISTORY. */
    public synchronized List<Tran> doiDau(String doiA, String doiB) {
        List<Tran> ketQua = new ArrayList<>();
        for (Tran tran : lichSu) {
            if (tran.coDoi(doiA) && tran.coDoi(doiB)) {
                ketQua.add(tran);
            }
        }
        return List.copyOf(ketQua);
    }
}
