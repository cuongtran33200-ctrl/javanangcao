package vn.edu.dau.bxh.model;

import vn.edu.dau.bxh.exception.LoiGiaoThucException;

/**
 * Cac lenh cua giao thuc. Moi lenh tu khai bao so tham so de kiem tra cu phap
 * ngay khi tach lenh, thay vi phai if-else tung truong hop. Gia tri -1 nghia
 * la so tham so bien thien (it nhat 2), dung cho lenh SCHEDULE.
 */
public enum Lenh {

    WATCH(0),
    SUBMIT(4),
    STANDINGS(0),
    HISTORY(2),
    EXPORT(0),
    SCHEDULE(-1),
    QUIT(0);

    private final int soThamSo;

    Lenh(int soThamSo) {
        this.soThamSo = soThamSo;
    }

    public int soThamSo() {
        return soThamSo;
    }

    public static Lenh tu(String ten) throws LoiGiaoThucException {
        for (Lenh lenh : values()) {
            if (lenh.name().equalsIgnoreCase(ten)) {
                return lenh;
            }
        }
        throw new LoiGiaoThucException("Lenh khong hop le: " + ten);
    }
}
