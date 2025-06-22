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

public class KaryaDAO {

    public static void insertKarya(KaryaSeni karya) throws SQLException {
        String sql = "INSERT INTO karya (artist, judul, deskripsi, gambar) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, karya.getArtist());
            ps.setString(2, karya.getJudul());
            ps.setString(3, karya.getDeskripsi());
            ps.setBytes(4, karya.getGambar());
            ps.executeUpdate();
        }
    }

    public static List<KaryaSeni> getAllKarya() throws SQLException {
        List<KaryaSeni> list = new ArrayList<>();
        String sql = "SELECT * FROM karya";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int karyaId = rs.getInt("id");
                String artist = rs.getString("artist");
                String judul = rs.getString("judul");
                String deskripsi = rs.getString("deskripsi");
                byte[] gambar = rs.getBytes("gambar");
                KaryaSeni karya = new KaryaSeni(artist, judul, deskripsi, gambar);
                karya.setId(karyaId);
                karya.setKomentarList(KomentarDAO.getKomentarByKaryaId(karyaId));
                list.add(karya);
            }
        }
        return list;
    }
    
    public static void updateKarya(KaryaSeni karya) throws SQLException {
        String sql = "UPDATE karya SET judul = ?, deskripsi = ?, gambar = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, karya.getJudul());
            ps.setString(2, karya.getDeskripsi());
            ps.setBytes(3, karya.getGambar());
            ps.setInt(4, karya.getId());
            ps.executeUpdate();
        }
    }

    public static void deleteKarya(int id) throws SQLException {
        String sql = "DELETE FROM karya WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}