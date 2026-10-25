package vn.edu.dau.bxh.model;

/** Một trận trong lịch thi đấu sinh ra, chưa có tỉ số. */
public record TranDangLich(int vong, String doiNha, String doiKhach) {

    public String toDong() {
        return vong + " " + doiNha + " " + doiKhach;
    }

    @Override
    public String toString() {
        return "V" + vong + ": " + doiNha + " - " + doiKhach;
    }
}
