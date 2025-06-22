/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package artshare;

/**
 *
 * @author ACER
 */
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class UploadWorker extends SwingWorker<Void, Void> {
    private File file;
    private JLabel label;

    public UploadWorker(File file, JLabel label) {
        this.file = file;
        this.label = label;
    }

    @Override
    protected Void doInBackground() throws Exception {
        BufferedImage img = ImageIO.read(file);
        label.setIcon(new ImageIcon(img.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));
        return null;
    }
}
