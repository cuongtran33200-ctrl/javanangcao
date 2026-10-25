package vn.edu.dau.bxh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import vn.edu.dau.bxh.protocol.GiaoThuc;

/**
 * Khach chi xem: gui WATCH roi in moi dong may chu phat ve (ke ca cap nhat truc tuyen).
 *
 * Cach dung: java vn.edu.dau.bxh.client.KhachXem [host] [cong]
 */
public final class KhachXem {

    private KhachXem() {
    }

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int cong = args.length > 1 ? Integer.parseInt(args[1]) : GiaoThuc.CONG_MAC_DINH;

        try (Socket socket = new Socket(host, cong);
             BufferedReader vao = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter ra = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
            ra.println("WATCH");
            String dong;
            while ((dong = vao.readLine()) != null) {
                System.out.println("[xem] " + dong);
            }
        }
        System.out.println("[xem] may chu da dong ket noi");
    }
}
