package vn.edu.dau.bxh.model;

/**
 * Mot dong cua bang xep hang. Record bat bien de co the gui cho khach
 * ma khong lo bi sua tiep trong luc dang tinh lai.
 */
public record DongXepHang(String maDoi, String tenDoi, int soTran, int soThang, int soHoa, int soThua,
                          int banThang, int banThua, int diem, int hieuSo) {

    /** Bo ba chi so dung de xac dinh nhom dong hang khi can pha bang doi dau. */
    public record ChiSo(int diem, int hieuSo, int banThang) { }

    public ChiSo chiSo() {
        return new ChiSo(diem, hieuSo, banThang);
    }

    public String toDong(int hang) {
        return String.format("%2d  %-16s %4d %3d %3d %3d %4d %4d %4d %4d",
                hang, maDoi, soTran, soThang, soHoa, soThua, banThang, banThua, hieuSo, diem);
    }

    public String toCsv() {
        return maDoi + "," + soTran + "," + soThang + "," + soHoa + "," + soThua + ","
                + banThang + "," + banThua + "," + hieuSo + "," + diem;
    }

    public static String tieuDeCsv() {
        return "doi,tran,thang,hoa,thua,banThang,banThua,hieuSo,diem";
    }

    public static String tieuDeBang() {
        return String.format("%2s  %-16s %4s %3s %3s %3s %4s %4s %4s %4s",
                "#", "doi", "tran", "T", "H", "B", "BT", "BB", "HS", "DIEM");
    }
}
