package vn.edu.dau.bxh.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import vn.edu.dau.bxh.exception.KetQuaKhongHopLeException;
import vn.edu.dau.bxh.exception.LoiGiaoThucException;
import vn.edu.dau.bxh.model.Tran;
import vn.edu.dau.bxh.model.YeuCau;
import vn.edu.dau.bxh.protocol.GiaoThuc;

/**
 * Phuc vu mot ket noi trong mot luong rieng. Doc lenh, phan tich, xu ly,
 * va bao loi bang dong "ERR <ly do>" khi giao thuc hoac du lieu vo ly.
 */
final class PhienKhach implements Runnable {

    private final Socket socket;
    private final MayChuBXH mayChu;
    private PrintWriter ra;

    PhienKhach(Socket socket, MayChuBXH mayChu) {
        this.socket = socket;
        this.mayChu = mayChu;
    }

    @Override
    public void run() {
        try (Socket s = socket;
             BufferedReader vao = new BufferedReader(
                     new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter w = new PrintWriter(
                     new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {
            this.ra = w;
            String dong;
            while ((dong = vao.readLine()) != null) {
                if (!xuLy(dong)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[phien] ket noi loi: " + e.getMessage());
        } finally {
            if (this.ra != null) {
                mayChu.xoaKhachXem(this.ra);
            }
        }
    }

    /** Xu ly mot dong lenh. Tra ve false de ket thuc phien. */
    private boolean xuLy(String dong) {
        try {
            YeuCau yeuCau = GiaoThuc.phanTich(dong);
            switch (yeuCau.lenh()) {
                case WATCH -> {
                    mayChu.themKhachXem(ra);
                    ra.println(GiaoThuc.OK + " " + mayChu.soKhachXem());
                    ra.print(mayChu.goiBangXepHang());
                    ra.flush();
                }
                case SUBMIT -> {
                    Tran tran = new Tran(yeuCau.thamSo(0), yeuCau.thamSo(1),
                            yeuCau.thamSoNguyen(2), yeuCau.thamSoNguyen(3));
                    mayChu.nhapKetQuaVaPhanPhat(tran);
                    ra.println(GiaoThuc.OK + " " + tran);
                }
                case STANDINGS -> {
                    ra.print(mayChu.goiBangXepHang());
                    ra.flush();
                }
                case HISTORY -> {
                    ra.print(mayChu.goiLichSuDoiDau(yeuCau.thamSo(0), yeuCau.thamSo(1)));
                    ra.flush();
                }
                case EXPORT -> {
                    ra.println(GiaoThuc.OK + " " + mayChu.xuat());
                }
                case SCHEDULE -> {
                    ra.print(mayChu.goiLichThiDau(yeuCau.thamSo()));
                    ra.flush();
                }
                case QUIT -> {
                    ra.println(GiaoThuc.BYE);
                    return false;
                }
                default -> ra.println(GiaoThuc.ERR + " lenh chua ho tro: " + yeuCau.lenh());
            }
        } catch (LoiGiaoThucException e) {
            ra.println(GiaoThuc.ERR + " " + e.getMessage());
        } catch (KetQuaKhongHopLeException e) {
            ra.println(GiaoThuc.ERR + " " + e.getMessage());
        } catch (IOException e) {
            ra.println(GiaoThuc.ERR + " loi ghi tep: " + e.getMessage());
        }
        return true;
    }
}
