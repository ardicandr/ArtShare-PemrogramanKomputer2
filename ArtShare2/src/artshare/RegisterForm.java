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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class RegisterForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnRegister;
    private JLabel lblUsername, lblPassword, lblTitle;
    private ResourceBundle messages;

    public RegisterForm() {
        initComponents();
    }

    private void initComponents() {
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.decode("#f5f5f5"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblTitle = new JLabel();
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0;
        lblUsername = new JLabel();
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblUsername, gbc);

        tfUsername = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(tfUsername, gbc);

        gbc.weightx = 0;
        lblPassword = new JLabel();
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPassword, gbc);

        pfPassword = new JPasswordField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(pfPassword, gbc);

        btnRegister = new JButton();
        styleButton(btnRegister);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(btnRegister, gbc);

        btnRegister.addActionListener(e -> doRegister());

        messages = ResourceBundle.getBundle("artshare.MessagesBundle", Locale.getDefault());
        updateLanguage();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(Color.decode("#4CAF50"));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void updateLanguage() {
        setTitle(messages.getString("title"));
        lblTitle.setText(messages.getString("button.register"));
        lblUsername.setText(messages.getString("label.username"));
        lblPassword.setText(messages.getString("label.password"));
        btnRegister.setText(messages.getString("button.register"));
    }

    private void doRegister() {
        String username = tfUsername.getText().trim();
        String password = String.valueOf(pfPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua kolom.");
            return;
        }

        String hashed = CryptoUtil.hashPassword(password);

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)"
            );
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, messages.getString("register.success"));
            dispose();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, messages.getString("register.duplicate"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, messages.getString("register.fail"));
        }
    }
}
