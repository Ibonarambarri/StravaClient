package com.example.stravaclient.client.swing.swingGUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    public Login() {
        setTitle("Strava");
        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Configurar la tecla ESC para cerrar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel del logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel logoContainer = new JPanel();
        logoContainer.setBackground(new Color(63, 73, 112));
        logoContainer.setPreferredSize(new Dimension(150, 150));
        logoContainer.setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon("src/main/resources/img/usuario.png");
        Image logoEsc = logo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoEsc));
        logoContainer.add(lblLogo, BorderLayout.CENTER);

        logoPanel.add(logoContainer);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Panel central con título y formulario
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 66, 90));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 5));
        formPanel.setBackground(Color.WHITE);

        // Campo de usuario
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setBackground(Color.WHITE);

        JTextField txtUsuario = createStyledTextField();

        userPanel.add(txtUsuario, BorderLayout.CENTER);

        addFormField("Username:", userPanel, formPanel);

        // Campo de contraseña
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setBackground(Color.WHITE);

        JPasswordField txtPassw = createStyledPasswordField();


        passPanel.add(txtPassw, BorderLayout.CENTER);

        addFormField("Password:", passPanel, formPanel);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        optionsPanel.setBackground(Color.WHITE);

        JCheckBox cbRememberMe = new JCheckBox("Remember me");
        cbRememberMe.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbRememberMe.setBackground(Color.WHITE);
        cbRememberMe.setForeground(new Color(100, 100, 100));

        JLabel lblForgotPassword = new JLabel("Forgot Password?");
        lblForgotPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblForgotPassword.setForeground(new Color(42, 112, 165));
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optionsPanel.add(cbRememberMe);
        optionsPanel.add(lblForgotPassword);
        formPanel.add(optionsPanel);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel inferior con botón de login y redes sociales
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(Color.WHITE);
        southPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Botón de login
        JButton bIniciarSesion = new JButton("LOGIN");
        bIniciarSesion.setPreferredSize(new Dimension(200, 45));
        bIniciarSesion.setBackground(new Color(34, 66, 90));
        bIniciarSesion.setForeground(Color.WHITE);
        bIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 16));
        bIniciarSesion.setOpaque(true);
        bIniciarSesion.setBorderPainted(false);
        bIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bIniciarSesion.addActionListener(e -> {
            new MainUI();
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(bIniciarSesion);
        southPanel.add(buttonPanel);

        // Panel de redes sociales
        JLabel lblRegister = new JLabel("Register with:", SwingConstants.CENTER);
        lblRegister.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRegister.setForeground(new Color(34, 66, 90));
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel socialButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        socialButtonsPanel.setBackground(Color.WHITE);

        // Botones de redes sociales
        JButton googleButton = createSocialButton("src/main/resources/img/google.png", new RegisterGoogle());
        JButton facebookButton = createSocialButton("src/main/resources/img/facebook.png", new RegisterFacebook());

        socialButtonsPanel.add(googleButton);
        socialButtonsPanel.add(facebookButton);

        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new BoxLayout(socialPanel, BoxLayout.Y_AXIS));
        socialPanel.setBackground(Color.WHITE);
        socialPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        socialPanel.add(lblRegister);
        socialPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        socialPanel.add(socialButtonsPanel);

        southPanel.add(socialPanel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void addFormField(String labelText, JComponent field, JPanel formPanel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(label);
        formPanel.add(field);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(0, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 1),
                new EmptyBorder(5, 10, 5, 10)));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(0, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 1),
                new EmptyBorder(5, 10, 5, 10)));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return field;
    }

    private JButton createSocialButton(String iconPath, Object registerClass) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaledIcon));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            if (registerClass instanceof RegisterGoogle) {
                new RegisterGoogle();
            } else if (registerClass instanceof RegisterFacebook) {
                new RegisterFacebook();
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}