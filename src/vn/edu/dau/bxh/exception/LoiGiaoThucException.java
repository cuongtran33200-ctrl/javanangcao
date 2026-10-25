package vn.edu.dau.bxh.exception;

/**
 * Loi cu phap o tang giao thuc: lenh la, sai so tham so, tham so sai kieu.
 * La ngoai le kiem tra de buoc phien khach phai xu ly.
 */
public class LoiGiaoThucException extends Exception {

    private static final long serialVersionUID = 1L;

    public LoiGiaoThucException(String thongDiep) {
        super(thongDiep);
    }

    public LoiGiaoThucException(String thongDiep, Throwable nguyenNhan) {
        super(thongDiep, nguyenNhan);
    }
}
