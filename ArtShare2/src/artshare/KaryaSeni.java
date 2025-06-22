/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package artshare;

/**
 *
 * @author ACER
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KaryaSeni implements Serializable {
    private int id;
    private String artist;
    private String judul;
    private String deskripsi;
    private byte[] gambar;
    private List<Komentar> komentarList;

    public KaryaSeni(String artist, String judul, String deskripsi, byte[] gambar) {
        this.artist = artist;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.komentarList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public byte[] getGambar() {
        return gambar;
    }

    public List<Komentar> getKomentarList() {
        return komentarList;
    }

    public void setKomentarList(List<Komentar> komentarList) {
        this.komentarList = komentarList;
    }

    public void tambahKomentar(Komentar komentar) {
        komentarList.add(komentar);
    }
    
    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }
    }