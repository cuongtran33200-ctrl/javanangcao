package vn.edu.dau.bxh.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Tran;

/**
 * Quy tac xep hang:
 * 1) diem giam dan, hieu so giam dan, ban thang giam dan -- ghep bang thenComparing;
 * 2) neu van dong hang thi pha bang ket qua doi dau (bong da dong hang).
 */
public final class QuyTacXepHang {

    /**
     * Ba tieu chi dau ghep bang thenComparing; cung ca ba chi so thi xep theo
     * ma doi de thu tu xac dinh, khong lay gia ngau nhien.
     */
    public static final Comparator<DongXepHang> THEO_CHI_SO =
            Comparator.comparingInt(DongXepHang::diem).reversed()
                    .thenComparing(Comparator.comparingInt(DongXepHang::hieuSo).reversed())
                    .thenComparing(Comparator.comparingInt(DongXepHang::banThang).reversed())
                    .thenComparing(DongXepHang::maDoi);

    private QuyTacXepHang() {
    }

    /**
     * Tinh thu tu xep hang. Tra ve danh sach moi bat bien, khong anh huong
     * vao danh sach dau vao.
     */
    public static List<DongXepHang> xepHang(Collection<DongXepHang> tatCa, List<Tran> lichSu) {
        List<DongXepHang> daSap = new ArrayList<>(tatCa);
        daSap.sort(THEO_CHI_SO);

        List<DongXepHang> ketQua = new ArrayList<>(daSap.size());
        int i = 0;
        while (i < daSap.size()) {
            int j = i + 1;
            while (j < daSap.size() && daSap.get(j).chiSo().equals(daSap.get(i).chiSo())) {
                j++;
            }
            List<DongXepHang> nhom = new ArrayList<>(daSap.subList(i, j));
            if (nhom.size() > 1) {
                nhom.sort(boSoSanhDoiDau(nhom, lichSu));
            }
            ketQua.addAll(nhom);
            i = j;
        }
        return List.copyOf(ketQua);
    }

    /**
     * Bong da dong hang: chi so sanh diem gianh duoc trong cac tran giua cac doi
     * trong cung nhom dong hang (mini-table), roi moi den ma doi.
     */
    private static Comparator<DongXepHang> boSoSanhDoiDau(List<DongXepHang> nhom, List<Tran> lichSu) {
        Set<String> thanhVien = new HashSet<>();
        for (DongXepHang d : nhom) {
            thanhVien.add(d.maDoi());
        }
        Map<String, Integer> diemDoiDau = new HashMap<>();
        for (Tran tran : lichSu) {
            if (!thanhVien.contains(tran.doiNha()) || !thanhVien.contains(tran.doiKhach())) {
                continue;
            }
            if (tran.doiNhaThang()) {
                diemDoiDau.merge(tran.doiNha(), 3, Integer::sum);
            } else if (tran.hoa()) {
                diemDoiDau.merge(tran.doiNha(), 1, Integer::sum);
                diemDoiDau.merge(tran.doiKhach(), 1, Integer::sum);
            } else {
                diemDoiDau.merge(tran.doiKhach(), 3, Integer::sum);
            }
        }
        return Comparator.comparingInt((DongXepHang d) -> diemDoiDau.getOrDefault(d.maDoi(), 0))
                .reversed()
                .thenComparing(DongXepHang::maDoi);
    }
}
