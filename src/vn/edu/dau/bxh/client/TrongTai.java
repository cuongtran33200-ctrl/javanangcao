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
 * Khach trong tai: doc lenh tu ban phim, gui len may chu, in phan hoi.
 *
 * Cach dung: java vn.edu.dau.bxh.client.TrongTai [host] [cong]
 * Lenh:  WATCH | SUBMIT <doiNha> <doiKhach> <banNha> <banKhach>
 *        | STANDINGS | HISTORY <doiA> <doiB> | EXPORT | QUIT
 */
public final class TrongTai {

    private TrongTai() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int cong = args.length > 1 ? Integer.parseInt(args[1]) : GiaoThuc.CONG_MAC_DINH;

        try (Socket socket = new Socket(host, cong);
             BufferedReader vao = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter ra = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            Thread luongDoc = new Thread(() -> {
                try {
                    String dong;
                    while ((dong = vao.readLine()) != null) {
                        System.out.println(dong);
                    }
                } catch (IOException e) {
                    System.out.println("[trong-tai] mat ket noi: " + e.getMessage());
                }
            }, "doc-phan-hoi");
            luongDoc.setDaemon(true);
            luongDoc.start();

            System.out.println("[trong-tai] lenh: WATCH | SUBMIT <doiNha> <doiKhach> <banNha> <banKhach>"
                    + " | STANDINGS | HISTORY <doiA> <doiB> | EXPORT | QUIT");

            BufferedReader banPhim = new BufferedReader(new InputStreamReader(System.in));
            String dong;
            while ((dong = banPhim.readLine()) != null) {
                ra.println(dong);
                if (dong.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
            luongDoc.join(500);
        }
        System.out.println("[trong-tai] da thoat");
    }
}
