package vn.edu.dau.bxh.core;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import vn.edu.dau.bxh.exception.KetQuaKhongHopLeException;
import vn.edu.dau.bxh.model.TranDangLich;

/**
 * Sinh lịch thi đấu vòng tròn một lượt bằng phương pháp vòng tròn
 * \en{(circle method)}: cố định đội đầu, xoay các đội còn lại mỗi vòng.
 * Số đội lẻ thì thêm một đội "BYE" để đội nghỉ trong vòng đó.
 */
public final class SinhLichVongTron {

    private static final String BYE = "BYE";

    private SinhLichVongTron() {
    }

    public static List<TranDangLich> sinhLich(List<String> doiVao) {
        if (doiVao == null || doiVao.size() < 2) {
            throw new KetQuaKhongHopLeException("Can it nhat 2 doi de sinh lich");
        }
        List<String> doi = chuanHoa(doiVao);
        if (doi.size() % 2 != 0) {
            doi.add(BYE);
        }
        int n = doi.size();
        int soVong = n - 1;
        List<TranDangLich> lich = new ArrayList<>();
        List<String> xep = new ArrayList<>(doi);
        for (int v = 0; v < soVong; v++) {
            for (int i = 0; i < n / 2; i++) {
                String a = xep.get(i);
                String b = xep.get(n - 1 - i);
                if (a.equals(BYE) || b.equals(BYE)) {
                    continue;
                }
                // Xen kẽ sân nhà theo vòng cho cân đối.
                String nha = (v % 2 == 0) ? a : b;
                String khach = (v % 2 == 0) ? b : a;
                lich.add(new TranDangLich(v + 1, nha, khach));
            }
            // Xoay: chuyển đội cuối của dải [1..n-1] lên vị trí 1.
            String cuoi = xep.remove(n - 1);
            xep.add(1, cuoi);
        }
        return List.copyOf(lich);
    }

    private static List<String> chuanHoa(List<String> doiVao) {
        List<String> doi = new ArrayList<>();
        TreeSet<String> daCo = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String d : doiVao) {
            String s = d == null ? "" : d.trim();
            if (s.isEmpty()) {
                throw new KetQuaKhongHopLeException("Ten doi rong");
            }
            if (s.chars().anyMatch(Character::isWhitespace)) {
                throw new KetQuaKhongHopLeException("Ten doi khong duoc chua khoang trang: " + s);
            }
            if (!daCo.add(s)) {
                throw new KetQuaKhongHopLeException("Ten doi bi trung: " + s);
            }
            doi.add(s);
        }
        return doi;
    }
}
