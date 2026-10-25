package vn.edu.dau.bxh.model;

import java.util.List;

import vn.edu.dau.bxh.exception.LoiGiaoThucException;

/** Mot dong lenh da tach thanh lenh va danh sach tham so. */
public record YeuCau(Lenh lenh, List<String> thamSo) {

    public YeuCau {
        thamSo = List.copyOf(thamSo);
    }

    public String thamSo(int viTri) {
        return thamSo.get(viTri);
    }

    public int thamSoNguyen(int viTri) throws LoiGiaoThucException {
        String giaTri = thamSo.get(viTri);
        try {
            return Integer.parseInt(giaTri);
        } catch (NumberFormatException e) {
            throw new LoiGiaoThucException(
                    "Tham so thu " + (viTri + 1) + " khong phai so nguyen: " + giaTri);
        }
    }
}
