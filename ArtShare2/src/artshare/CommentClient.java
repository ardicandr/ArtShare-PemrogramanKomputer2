/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package artshare;

/**
 *
 * @author ACER
 */
import java.io.*;
import java.net.*;

public class CommentClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void send(Komentar komentar) {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                out.writeObject(komentar);
                out.flush();
                System.out.println("[CLIENT] Komentar terkirim: " + komentar);
            } catch (IOException e) {
                System.err.println("[CLIENT ERROR] Gagal mengirim komentar: " + e.getMessage());
            }
        }).start();
    }
}