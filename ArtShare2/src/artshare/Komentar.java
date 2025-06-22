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
import java.time.LocalDateTime;

public class Komentar implements Serializable {
    private String pengirim;
    private String isi;
    private LocalDateTime waktu;

    public Komentar(String pengirim, String isi) {
        this.pengirim = pengirim;
        this.isi = isi;
        this.waktu = LocalDateTime.now();
    }

    public String getPengirim() {
        return pengirim;
    }

    public String getIsi() {
        return isi;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    @Override
    public String toString() {
        return "[" + waktu + "] " + pengirim + ": " + isi;
    }
} 