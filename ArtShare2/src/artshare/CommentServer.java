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
import java.util.concurrent.*;

public class CommentServer {
    private static final int PORT = 9999;
    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("[SERVER] Menunggu koneksi di port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            Komentar komentar = (Komentar) in.readObject();
            System.out.println("[RECEIVED] " + komentar);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}