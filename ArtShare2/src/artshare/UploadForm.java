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
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ResourceBundle;

public class UploadForm extends JDialog {
    private JTextField tfJudul;
    private JTextArea taDeskripsi;
    private JLabel lblPreview;
    private JButton btnBrowse, btnUpload;
    private byte[] imageBytes;
    private boolean uploaded = false;
    private KaryaSeni karya;
    private boolean isEditMode = false;
    private ResourceBundle messages;

    // Constructor untuk Upload
    public UploadForm(JFrame parent, String artist, ResourceBundle messages) {
        this(parent, artist, null, messages);
    }

    // Constructor untuk Edit
    public UploadForm(JFrame parent, String artist, KaryaSeni karyaEdit, ResourceBundle messages) {
        super(parent, karyaEdit == null ? messages.getString("button.upload") : messages.getString("button.edit"), true);
        this.messages = messages;
        this.isEditMode = karyaEdit != null;

        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblJudul = new JLabel(messages.getString("label.judul"));
        tfJudul = new JTextField();
        JLabel lblDeskripsi = new JLabel(messages.getString("label.deskripsi"));
        taDeskripsi = new JTextArea(3, 20);
        taDeskripsi.setLineWrap(true);
        taDeskripsi.setWrapStyleWord(true);
        JScrollPane scrollDeskripsi = new JScrollPane(taDeskripsi);
        lblPreview = new JLabel(messages.getString("image.none"), SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(300, 200));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnBrowse = new JButton(messages.getString("button.browse"));
        btnBrowse.setBackground(new Color(33, 150, 243));
        btnBrowse.setForeground(Color.WHITE);
        btnBrowse.setFocusPainted(false);
        btnBrowse.addActionListener(e -> browseImage());

        btnUpload = new JButton(isEditMode ? messages.getString("button.edit") : messages.getString("button.upload"));
        btnUpload.setBackground(new Color(76, 175, 80));
        btnUpload.setForeground(Color.WHITE);
        btnUpload.setFocusPainted(false);
        btnUpload.setPreferredSize(new Dimension(150, 35));
        btnUpload.addActionListener(e -> upload(artist));

        formPanel.add(lblJudul, gbc);
        gbc.gridy++;
        formPanel.add(tfJudul, gbc);
        gbc.gridy++;
        formPanel.add(lblDeskripsi, gbc);
        gbc.gridy++;
        formPanel.add(scrollDeskripsi, gbc);
        gbc.gridy++;
        formPanel.add(btnBrowse, gbc);
        gbc.gridy++;
        formPanel.add(lblPreview, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnUpload);
        add(bottomPanel, BorderLayout.SOUTH);

        if (isEditMode) {
            this.karya = karyaEdit;
            tfJudul.setText(karya.getJudul());
            taDeskripsi.setText(karya.getDeskripsi());
            imageBytes = karya.getGambar();

            if (imageBytes != null) {
                ImageIcon icon = new ImageIcon(imageBytes);
                Image scaled = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(scaled));
                lblPreview.setText("");
            }
        }
    }

    private void browseImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file)) {
                imageBytes = fis.readAllBytes();
                ImageIcon icon = new ImageIcon(imageBytes);
                Image scaled = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(scaled));
                lblPreview.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, messages.getString("image.read.fail"));
            }
        }
    }

    private void upload(String artist) {
        String judul = tfJudul.getText().trim();
        String deskripsi = taDeskripsi.getText().trim();

        if (judul.isEmpty() || deskripsi.isEmpty() || imageBytes == null) {
            JOptionPane.showMessageDialog(this, messages.getString("upload.fail"));
            return;
        }

        if (isEditMode) {
            karya.setJudul(judul);
            karya.setDeskripsi(deskripsi);
            karya.setGambar(imageBytes);
        } else {
            karya = new KaryaSeni(artist, judul, deskripsi, imageBytes);
        }

        uploaded = true;
        dispose();
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public KaryaSeni getKarya() {
        return karya;
    }
}