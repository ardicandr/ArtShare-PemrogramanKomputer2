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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.List; 
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Dashboard extends JFrame {
    private String username;
    private JPanel karyaContainer;
    private List<KaryaSeni> daftarKarya;
    private List<KaryaPanel> panelList;
    private ResourceBundle messages;
    private JComboBox<String> cbLang;
    private JLabel lblUser;

    public Dashboard(String username, Locale locale) {
        this.username = username;
        this.daftarKarya = new ArrayList<>();
        this.panelList = new ArrayList<>();
        Locale.setDefault(locale);
        this.messages = ResourceBundle.getBundle("artshare.MessagesBundle", locale);
        initUI();
        loadKaryaFromDatabase();

        new Timer(5000, e -> {
            cekDataBaru();
            refreshKomentar();
        }).start();
    }
    
    public Dashboard(String username) {
        this(username, Locale.getDefault());
    }

    private void initUI() {
        setTitle("Dashboard - ArtShare");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel atas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lblUser = new JLabel(messages.getString("label.loggedin") + " " + username);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnUpload = new JButton(messages.getString("button.upload"));
        styleButton(btnUpload);
        btnUpload.addActionListener(e -> openUploadForm());

        JButton btnLogout = new JButton(messages.getString("button.logout"));
        styleButton(btnLogout);
        btnLogout.addActionListener(e -> logout());

        cbLang = new JComboBox<>(new String[]{"English", "Indonesia"});
        cbLang.setSelectedItem(Locale.getDefault().getLanguage().equals("id") ? "Indonesia" : "English");
        cbLang.addActionListener(e -> changeLanguage());

        topPanel.add(lblUser);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnUpload);
        topPanel.add(btnLogout);
        topPanel.add(new JLabel(messages.getString("label.language")));
        topPanel.add(cbLang);

        add(topPanel, BorderLayout.NORTH);

        // Panel kontainer karya
        karyaContainer = new JPanel();
        karyaContainer.setLayout(new BoxLayout(karyaContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(karyaContainer);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(Color.decode("#4CAF50"));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(150, 30));
    }

    private void openUploadForm() {
        UploadForm form = new UploadForm(this, username, messages);
        form.setVisible(true);
        if (form.isUploaded()) {
            KaryaSeni karya = form.getKarya();
            try {
                KaryaDAO.insertKarya(karya);
                loadKaryaFromDatabase();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, messages.getString("upload.fail"));
                e.printStackTrace();
            }
        }
    }

    private void loadKaryaFromDatabase() {
        try {
            daftarKarya = KaryaDAO.getAllKarya();
            karyaContainer.removeAll();
            panelList.clear();

            for (KaryaSeni karya : daftarKarya) {
                KaryaPanel panel = new KaryaPanel(karya, username);
                panelList.add(panel);
                karyaContainer.add(panel);
            }
            karyaContainer.revalidate();
            karyaContainer.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, messages.getString("load.fail"));
            e.printStackTrace();
        }
    }

    private void refreshKomentar() {
        for (KaryaPanel panel : panelList) {
            try {
                int karyaId = panel.getKarya().getId();
                List<Komentar> updated = KomentarDAO.getKomentarByKaryaId(karyaId);
                panel.refreshKomentar(updated);
            } catch (SQLException e) {
                System.err.println("Gagal refresh komentar untuk karya id " + panel.getKarya().getId());
            }
        }
    }

     private void cekDataBaru() {
        try {
            List<KaryaSeni> dataBaru = KaryaDAO.getAllKarya();
            if (dataBaru.size() != daftarKarya.size()) {
                loadKaryaFromDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }

    private void changeLanguage() {
        String selected = cbLang.getSelectedItem().toString();
        Locale locale = selected.equals("Indonesia") ? new Locale("id", "ID") : Locale.ENGLISH;

        dispose(); // tutup dashboard sekarang
        SwingUtilities.invokeLater(() -> new Dashboard(username, locale).setVisible(true)); // buka dashboard baru dengan locale baru
    }
}