package vn.edu.dau.bxh.exception;

/**
 * Loi nghiep vu: tran dau vo ly ve mat du lieu (ti so am, doi trung ten, ...).
 * La ngoai le khong kiem tra de co the nem ngay trong ham tao cua record Tran.
 */
public class KetQuaKhongHopLeException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public KetQuaKhongHopLeException(String thongDiep) {
        super(thongDiep);
    }

    public KetQuaKhongHopLeException(String thongDiep, Throwable nguyenNhan) {
        super(thongDiep, nguyenNhan);
    }
}
