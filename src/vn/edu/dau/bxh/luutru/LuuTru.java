package vn.edu.dau.bxh.luutru;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import vn.edu.dau.bxh.exception.KetQuaKhongHopLeException;
import vn.edu.dau.bxh.model.DongXepHang;
import vn.edu.dau.bxh.model.Tran;

/**
 * Doc/ghi du lieu dang CSV. Moi lan mo tep deu dung try-with-resources
 * de dam bao dong tep ngay ca khi co ngoai le.
 */
public final class LuuTru {

    private LuuTru() {
    }

    public static void ghiLichSu(Path tep, List<Tran> ds) throws IOException {
        moThuMucCha(tep);
        try (BufferedWriter ra = Files.newBufferedWriter(tep, StandardCharsets.UTF_8)) {
            ra.write("doiNha,doiKhach,banNha,banKhach");
            ra.newLine();
            for (Tran tran : ds) {
                ra.write(tran.toCsv());
                ra.newLine();
            }
        }
    }

    public static List<Tran> docLichSu(Path tep) throws IOException {
        List<Tran> ds = new ArrayList<>();
        if (!Files.exists(tep)) {
            return List.of();
        }
        try (BufferedReader vao = Files.newBufferedReader(tep, StandardCharsets.UTF_8)) {
            String dong;
            boolean dongDau = true;
            while ((dong = vao.readLine()) != null) {
                if (dongDau) {
                    dongDau = false;
                    if (dong.startsWith("doiNha")) {
                        continue;
                    }
                }
                if (dong.isBlank()) {
                    continue;
                }
                try {
                    ds.add(Tran.tuCsv(dong));
                } catch (KetQuaKhongHopLeException e) {
                    System.err.println("[luu-tru] bo qua dong loi: " + dong + " (" + e.getMessage() + ")");
                }
            }
        }
        return List.copyOf(ds);
    }

    public static void ghiBangXepHang(Path tep, List<DongXepHang> ds) throws IOException {
        moThuMucCha(tep);
        try (BufferedWriter ra = Files.newBufferedWriter(tep, StandardCharsets.UTF_8)) {
            ra.write(DongXepHang.tieuDeCsv());
            ra.newLine();
            for (DongXepHang d : ds) {
                ra.write(d.toCsv());
                ra.newLine();
            }
        }
    }

    private static void moThuMucCha(Path tep) throws IOException {
        Path cha = tep.getParent();
        if (cha != null) {
            Files.createDirectories(cha);
        }
    }
}
