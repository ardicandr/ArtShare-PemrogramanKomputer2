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
import java.sql.*;
import java.util.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LoginForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JComboBox<String> cbLang;
    private JButton btnLogin, btnRegister;
    private JLabel lblUsername, lblPassword, lblTitle;
    private ResourceBundle messages;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ArtShare Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.decode("#f5f5f5"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblTitle = new JLabel("ArtShare");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        lblUsername = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblUsername, gbc);

        tfUsername = new JTextField();
        gbc.gridx = 1;
        add(tfUsername, gbc);

        lblPassword = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPassword, gbc);

        pfPassword = new JPasswordField();
        gbc.gridx = 1;
        add(pfPassword, gbc);

        cbLang = new JComboBox<>(new String[]{"English", "Indonesia"});
        gbc.gridx = 0; gbc.gridy = 3;
        add(cbLang, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout());
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        styleButton(btnLogin);
        styleButton(btnRegister);
        panelBtn.add(btnLogin);
        panelBtn.add(btnRegister);

        gbc.gridx = 1;
        add(panelBtn, gbc);

        cbLang.addActionListener(e -> changeLanguage());
        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> new RegisterForm().setVisible(true));

        // Default locale
        messages = ResourceBundle.getBundle("artshare.MessagesBundle", Locale.ENGLISH);
        updateLanguage();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(Color.decode("#4CAF50"));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void changeLanguage() {
        String selected = cbLang.getSelectedItem().toString();
        Locale locale = selected.equals("Indonesia") ? new Locale("id", "ID") : Locale.ENGLISH;
        messages = ResourceBundle.getBundle("artshare.MessagesBundle", locale);
        updateLanguage();
    }

    private void updateLanguage() {
        setTitle(messages.getString("title"));
        lblTitle.setText(messages.getString("title"));
        lblUsername.setText(messages.getString("label.username"));
        lblPassword.setText(messages.getString("label.password"));
        btnLogin.setText(messages.getString("button.login"));
        btnRegister.setText(messages.getString("button.register"));
    }

    private void doLogin() {
        String username = tfUsername.getText();
        String password = CryptoUtil.hashPassword(String.valueOf(pfPassword.getPassword()));

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, messages.getString("login.success"));
                new Dashboard(username, Locale.getDefault()).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, messages.getString("login.fail"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}