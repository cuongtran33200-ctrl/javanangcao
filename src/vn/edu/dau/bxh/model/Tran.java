package vn.edu.dau.bxh.model;

import vn.edu.dau.bxh.exception.KetQuaKhongHopLeException;

/**
 * Mot tran dau da ket thuc. Record bat bien nen dung duoc lam khoa tra cuu
 * trong Map va ghi ra CSV ma khong lo bi sua khi dang doc.
 */
public record Tran(String doiNha, String doiKhach, int banNha, int banKhach) {

    public Tran {
        doiNha = chuanHoa(doiNha, "doi nha");
        doiKhach = chuanHoa(doiKhach, "doi khach");
        if (doiNha.equalsIgnoreCase(doiKhach)) {
            throw new KetQuaKhongHopLeException("Mot doi khong the dau voi chinh minh: " + doiNha);
        }
        if (banNha < 0 || banKhach < 0) {
            throw new KetQuaKhongHopLeException("Ti so khong duoc am: " + banNha + "-" + banKhach);
        }
    }

    private static String chuanHoa(String ten, String nhan) {
        String s = ten == null ? "" : ten.trim();
        if (s.isEmpty()) {
            throw new KetQuaKhongHopLeException("Thieu ten " + nhan);
        }
        if (s.chars().anyMatch(Character::isWhitespace)) {
            throw new KetQuaKhongHopLeException("Ten doi khong duoc chua khoang trang: " + s);
        }
        return s;
    }

    public boolean doiNhaThang() {
        return banNha > banKhach;
    }

    public boolean doiKhachThang() {
        return banKhach > banNha;
    }

    public boolean hoa() {
        return banNha == banKhach;
    }

    public boolean coDoi(String doi) {
        return doi != null && (doi.equalsIgnoreCase(doiNha) || doi.equalsIgnoreCase(doiKhach));
    }

    public String toCsv() {
        return doiNha + "," + doiKhach + "," + banNha + "," + banKhach;
    }

    public static Tran tuCsv(String dong) {
        if (dong == null) {
            throw new KetQuaKhongHopLeException("Dong CSV rong");
        }
        String[] phan = dong.trim().split(",");
        if (phan.length != 4) {
            throw new KetQuaKhongHopLeException("Dong CSV sai dinh dang: " + dong);
        }
        try {
            return new Tran(phan[0], phan[1],
                    Integer.parseInt(phan[2].trim()), Integer.parseInt(phan[3].trim()));
        } catch (NumberFormatException e) {
            throw new KetQuaKhongHopLeException("Ti so khong phai so nguyen: " + dong);
        }
    }

    @Override
    public String toString() {
        return doiNha + " " + banNha + "-" + banKhach + " " + doiKhach;
    }
}
