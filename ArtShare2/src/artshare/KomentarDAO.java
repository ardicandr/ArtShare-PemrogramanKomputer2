/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package artshare;

/**
 *
 * @author ACER
 */
import java.sql.*;
import java.util.*;

public class KomentarDAO {

    public static void insertKomentar(Komentar komentar, int karyaId) throws SQLException {
        String sql = "INSERT INTO komentar (karya_id, pengirim, isi) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, karyaId);
            ps.setString(2, komentar.getPengirim());
            ps.setString(3, komentar.getIsi());
            ps.executeUpdate();
        }
    }

    public static List<Komentar> getKomentarByKaryaId(int karyaId) throws SQLException {
        List<Komentar> list = new ArrayList<>();
        String sql = "SELECT * FROM komentar WHERE karya_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, karyaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String pengirim = rs.getString("pengirim");
                String isi = rs.getString("isi");
                list.add(new Komentar(pengirim, isi));
            }
        }
        return list;
    }
}