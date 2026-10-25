package vn.edu.dau.bxh.model;

/**
 * Thong ke tich luy cua mot doi. Bien doi duoc giu trong BangXepHang
 * nen chi ghi nhan khi dang giu khoa cua bang do.
 */
public final class Doi {

    private final String maDoi;
    private final String tenDoi;
    private int soThang;
    private int soHoa;
    private int soThua;
    private int banThang;
    private int banThua;

    public Doi(String maDoi, String tenDoi) {
        this.maDoi = maDoi;
        this.tenDoi = tenDoi;
    }

    /** Cap nhat thong ke cua doi nay theo ket qua mot tran. */
    public void ghiNhan(Tran tran, boolean laDoiNha) {
        int banTa = laDoiNha ? tran.banNha() : tran.banKhach();
        int banDoiPhuong = laDoiNha ? tran.banKhach() : tran.banNha();
        banThang += banTa;
        banThua += banDoiPhuong;
        if (banTa > banDoiPhuong) {
            soThang++;
        } else if (banTa == banDoiPhuong) {
            soHoa++;
        } else {
            soThua++;
        }
    }

    public int diem() {
        return soThang * 3 + soHoa;
    }

    public int hieuSo() {
        return banThang - banThua;
    }

    public int soTran() {
        return soThang + soHoa + soThua;
    }

    public DongXepHang dongXepHang() {
        return new DongXepHang(maDoi, tenDoi, soTran(), soThang, soHoa, soThua,
                banThang, banThua, diem(), hieuSo());
    }

    @Override
    public String toString() {
        return maDoi + ":" + diem() + "d";
    }
}
