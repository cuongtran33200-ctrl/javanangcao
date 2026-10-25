package vn.edu.dau.bxh.protocol;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import vn.edu.dau.bxh.exception.LoiGiaoThucException;
import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Lenh;
import vn.edu.dau.bxh.model.Tran;
import vn.edu.dau.bxh.model.TranDangLich;
import vn.edu.dau.bxh.model.YeuCau;

/**
 * Giao thuc van ban, moi lenh mot dong phan tach bang khoang trang.
 * Cau tra loi nhieu dong duoc khung bang mot dong tieu de co so dong:
 *   "STANDINGS <n>" hoac "HISTORY <n>".
 */
public final class GiaoThuc {

    public static final int CONG_MAC_DINH = 5000;

    public static final String OK = "OK";
    public static final String ERR = "ERR";
    public static final String BYE = "BYE";
    public static final String STANDINGS = "STANDINGS";
    public static final String HISTORY = "HISTORY";
    public static final String SCHEDULE = "SCHEDULE";

    private GiaoThuc() {
    }

    /** Tach mot dong lenh thanh YeuCau, kiem tra cu phap va so tham so. */
    public static YeuCau phanTich(String dong) throws LoiGiaoThucException {
        String s = dong == null ? "" : dong.trim();
        if (s.isEmpty()) {
            throw new LoiGiaoThucException("Dong lenh rong");
        }
        String[] phan = s.split("\\s+");
        Lenh lenh = Lenh.tu(phan[0].toUpperCase(Locale.ROOT));
        List<String> thamSo = List.of(Arrays.copyOfRange(phan, 1, phan.length));
        if (lenh.soThamSo() >= 0) {
            if (thamSo.size() != lenh.soThamSo()) {
                throw new LoiGiaoThucException("Lenh " + lenh + " can " + lenh.soThamSo()
                        + " tham so, nhan duoc " + thamSo.size());
            }
        } else if (thamSo.size() < 2) {
            throw new LoiGiaoThucException("Lenh " + lenh + " can it nhat 2 tham so");
        }
        return new YeuCau(lenh, thamSo);
    }

    public static String goiBangXepHang(List<DongXepHang> ds) {
        StringBuilder sb = new StringBuilder();
        sb.append(STANDINGS).append(' ').append(ds.size()).append('\n');
        sb.append(DongXepHang.tieuDeBang()).append('\n');
        int hang = 1;
        for (DongXepHang d : ds) {
            sb.append(d.toDong(hang++)).append('\n');
        }
        return sb.toString();
    }

    public static String goiLichSuDoiDau(String doiA, String doiB, List<Tran> ds) {
        StringBuilder sb = new StringBuilder();
        sb.append(HISTORY).append(' ').append(ds.size()).append('\n');
        for (Tran t : ds) {
            sb.append(t).append('\n');
        }
        return sb.toString();
    }

    public static String goiLichThiDau(List<TranDangLich> lich) {
        StringBuilder sb = new StringBuilder();
        sb.append(SCHEDULE).append(' ').append(lich.size()).append('\n');
        for (TranDangLich t : lich) {
            sb.append(t.toDong()).append('\n');
        }
        return sb.toString();
    }
}
