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
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class KaryaPanel extends JPanel {
    private KaryaSeni karya;
    private DefaultListModel<String> komentarModel;
    private JList<String> komentarList;
    private ResourceBundle messages;

    public KaryaPanel(KaryaSeni karya, String currentUser) {
        this.karya = karya;
        this.messages = ResourceBundle.getBundle("artshare.MessagesBundle", Locale.getDefault());

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder(karya.getJudul()));
        setBackground(Color.WHITE);

        // Gambar
        ImageIcon icon = new ImageIcon(karya.getGambar());
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
        imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(imgLabel, BorderLayout.WEST);

        // Panel info dan komentar
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setBackground(Color.WHITE);

        JTextArea info = new JTextArea("Artist: " + karya.getArtist() + "\n" + karya.getDeskripsi());
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setOpaque(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(info, BorderLayout.NORTH);

        komentarModel = new DefaultListModel<>();
        for (Komentar k : karya.getKomentarList()) {
            komentarModel.addElement(k.getPengirim() + ": " + k.getIsi());
        }

        komentarList = new JList<>(komentarModel);
        komentarList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane komentarScroll = new JScrollPane(komentarList);
        komentarScroll.setPreferredSize(new Dimension(300, 100));
        infoPanel.add(komentarScroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        JButton btnKomentar = new JButton(messages.getString("button.comment"));
        btnKomentar.setBackground(Color.decode("#2196F3"));
        btnKomentar.setForeground(Color.white);
        btnKomentar.setFocusPainted(false);
        btnKomentar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnKomentar.addActionListener(e -> {
            String isi = JOptionPane.showInputDialog(this, messages.getString("prompt.comment"));
            if (isi != null && !isi.trim().isEmpty()) {
                Komentar k = new Komentar(currentUser, isi);
                karya.tambahKomentar(k);
                komentarModel.addElement(k.getPengirim() + ": " + k.getIsi());
                try {
                    KomentarDAO.insertKomentar(k, karya.getId());
                    CommentClient.send(k);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, messages.getString("comment.fail"));
                    ex.printStackTrace();
                }
            }
        });

        btnPanel.add(btnKomentar);

        // Jika pemilik karya, tampilkan tombol edit dan delete
        if (karya.getArtist().equals(currentUser)) {
            JButton btnEdit = new JButton(messages.getString("button.edit"));
            JButton btnDelete = new JButton(messages.getString("button.delete"));

            btnEdit.setBackground(Color.decode("#FFC107"));
            btnDelete.setBackground(Color.decode("#F44336"));
            btnEdit.setForeground(Color.white);
            btnDelete.setForeground(Color.white);

            btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            btnEdit.addActionListener(e -> {
                UploadForm form = new UploadForm((JFrame) SwingUtilities.getWindowAncestor(this), currentUser, karya, messages);
                form.setVisible(true);
                if (form.isUploaded()) {
                    KaryaSeni updated = form.getKarya();
                    updated.setId(karya.getId());
                    try {
                        KaryaDAO.updateKarya(updated);
                        JOptionPane.showMessageDialog(this, messages.getString("edit.success"));
                        SwingUtilities.getWindowAncestor(this).dispose();
                        new Dashboard(currentUser, Locale.getDefault()).setVisible(true);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, messages.getString("edit.fail"));
                        ex.printStackTrace();
                    }
                }
            });

            btnDelete.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(this, messages.getString("confirm.delete"), "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    KaryaDAO.deleteKarya(karya.getId());
                    JOptionPane.showMessageDialog(this, messages.getString("delete.success"));

                    // Hapus panel dari container
                    Container parent = this.getParent();
                    if (parent != null) {
                        parent.remove(this);
                        parent.revalidate();
                        parent.repaint();
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, messages.getString("delete.fail"));
                    ex.printStackTrace();
                }
            }
        });

            btnPanel.add(btnEdit);
            btnPanel.add(btnDelete);
        }

        infoPanel.add(btnPanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    public KaryaSeni getKarya() {
        return karya;
    }

    public void refreshKomentar(List<Komentar> komentarBaru) {
        komentarModel.clear();
        for (Komentar k : komentarBaru) {
            komentarModel.addElement(k.getPengirim() + ": " + k.getIsi());
        }
    }
}
