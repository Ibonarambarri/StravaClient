package com.example.stravaclient.client.swing.swingGUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JPanel pLogo;
    private JPanel pCentral;
    private JPanel pBotones;
    private JPanel pSocialMedia;

    private JTextField txtUsuario;
    private JPasswordField txtPassw;

    private JLabel lblLogo;
    private JCheckBox cbRememberMe;
    private JLabel lblForgotPassword;
    private JButton bIniciarSesion;

    public Login() {
        setTitle("Strava");
        setSize(420, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        //---------------------------- LOGO -----------------------------
        pLogo = new JPanel();
        pLogo.setBackground(Color.WHITE);
        pLogo.setBorder(new EmptyBorder(20, 0, 100, 0)); // Espacio adicional arriba

        // Imagen del usuario como logo


        JPanel pLogo = new JPanel();
        pLogo.setBackground(Color.WHITE);
        pLogo.setBorder(new EmptyBorder(20, 0, 50, 0));

        // Crear un JPanel para envolver la imagen y aplicar el fondo rojo
        JPanel imageContainer = new JPanel();
        imageContainer.setBackground(new Color(63, 73, 112)); // Fondo rojo
        imageContainer.setPreferredSize(new Dimension(150, 150)); // Tamaño del contenedor
        imageContainer.setLayout(new BorderLayout());

        // Crear el JLabel con la imagen
        ImageIcon logo = new ImageIcon("/Users/ibonarambarri/IdeaProjects/SD/sample_code/StravaClient/src/main/resources/img/usuario.png");
        Image logoEsc = logo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoEsc));

        // No es necesario poner el fondo del JLabel, solo del contenedor
        imageContainer.add(lblLogo, BorderLayout.CENTER);

        // Añadir el contenedor con la imagen al panel pLogo
        pLogo.add(imageContainer);
        mainPanel.add(pLogo, BorderLayout.NORTH);

        //---------------------------- PANEL CENTRAL ---------------------
        pCentral = new JPanel(new GridLayout(5, 1, 5, 5)); // Reducido el espaciado vertical
        pCentral.setBackground(Color.WHITE);

        // Panel de usuario
        JPanel pUsuario = new JPanel(new BorderLayout());
        pUsuario.setBackground(new Color(34, 66, 90));

        JLabel lblLogoU = new JLabel(new ImageIcon(new ImageIcon("/Users/ibonarambarri/IdeaProjects/SD/sample_code/StravaClient/src/main/resources/img/tarjeta-de-identificacion.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lblLogoU.setPreferredSize(new Dimension(45, 45));
        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 40));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 2),
                new EmptyBorder(5, 10, 5, 10)));
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtUsuario.setBackground(new Color(42, 112, 165));
        txtUsuario.setForeground(Color.WHITE);

        pUsuario.add(lblLogoU, BorderLayout.WEST);
        pUsuario.add(txtUsuario, BorderLayout.CENTER);

        // Panel de contraseña
        JPanel pPassword = new JPanel(new BorderLayout());
        pPassword.setBackground(new Color(34, 66, 90));

        JLabel lblLogoC = new JLabel(new ImageIcon(new ImageIcon("/Users/ibonarambarri/IdeaProjects/SD/sample_code/StravaClient/src/main/resources/img/candado.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lblLogoC.setPreferredSize(new Dimension(45, 45));
        txtPassw = new JPasswordField();
        txtPassw.setPreferredSize(new Dimension(200, 40));
        txtPassw.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 2),
                new EmptyBorder(5, 10, 5, 10)));
        txtPassw.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtPassw.setBackground(new Color(42, 112, 165));
        txtPassw.setForeground(Color.WHITE);

        pPassword.add(lblLogoC, BorderLayout.WEST);
        pPassword.add(txtPassw, BorderLayout.CENTER);

        // Añadir los JTextField al panel central
        pCentral.add(pUsuario);
        pCentral.add(pPassword);

        // Checkbox de "Remember me" y opción de "Forgot Password?"
        cbRememberMe = new JCheckBox("Remember me");
        cbRememberMe.setBackground(Color.WHITE);
        cbRememberMe.setForeground(Color.GRAY);
        lblForgotPassword = new JLabel("Forgot Password?");
        lblForgotPassword.setForeground(new Color(100, 149, 237));
        lblForgotPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.add(cbRememberMe);
        optionsPanel.add(lblForgotPassword);

        pCentral.add(optionsPanel);
        mainPanel.add(pCentral, BorderLayout.CENTER);

        //---------------------------- BOTÓN DE INICIAR SESIÓN ---------------------
        pBotones = new JPanel();
        pBotones.setBackground(Color.WHITE);

        // Configuración del botón de inicio de sesión
        bIniciarSesion = new JButton("LOGIN");
        bIniciarSesion.setPreferredSize(new Dimension(300, 50));
        bIniciarSesion.setBackground(new Color(34, 66, 90));
        bIniciarSesion.setForeground(Color.WHITE);
        bIniciarSesion.setOpaque(true); // Forzar el color de fondo
        bIniciarSesion.setBorderPainted(false); // Remover el borde
        bIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 18));
        bIniciarSesion.addActionListener(e -> {
            new Login();
            dispose();
        });
        pBotones.add(bIniciarSesion);


        //---------------------------- SOCIAL MEDIA PANEL ---------------------
        // Create a container panel for both the text and social media buttons
        JPanel pSocialContainer = new JPanel(new BorderLayout(0, 10));
        pSocialContainer.setBackground(Color.WHITE);

        // Add "Regístrate con:" text
        JLabel lblRegistrate = new JLabel("Register with:", SwingConstants.CENTER);
        lblRegistrate.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRegistrate.setForeground(new Color(34, 66, 90));
        pSocialContainer.add(lblRegistrate, BorderLayout.NORTH);

        // Create panel for social media icons
        pSocialMedia = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pSocialMedia.setBackground(Color.WHITE);

        // Google login button
        ImageIcon googleIcon = new ImageIcon("/Users/ibonarambarri/IdeaProjects/SD/sample_code/StravaClient/src/main/resources/img/google.png");
        Image googleScaled = googleIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btnGoogle = new JButton(new ImageIcon(googleScaled));
        btnGoogle.setBorderPainted(false);
        btnGoogle.setContentAreaFilled(false);
        btnGoogle.setFocusPainted(false);
        btnGoogle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGoogle.addActionListener(e -> {
            new RegisterGoogle();
        });

        // Facebook login button
        ImageIcon fbIcon = new ImageIcon("/Users/ibonarambarri/IdeaProjects/SD/sample_code/StravaClient/src/main/resources/img/facebook.png");
        Image fbScaled = fbIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btnFacebook = new JButton(new ImageIcon(fbScaled));
        btnFacebook.setBorderPainted(false);
        btnFacebook.setContentAreaFilled(false);
        btnFacebook.setFocusPainted(false);
        btnFacebook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFacebook.addActionListener(e -> {
            new RegisterFacebook();
        });

        // Add buttons to social media panel
        pSocialMedia.add(btnGoogle);
        pSocialMedia.add(btnFacebook);

        pSocialContainer.add(pSocialMedia, BorderLayout.CENTER);

        // Create a panel to hold both the login button and social media panels
        JPanel southPanel = new JPanel(new BorderLayout(0, 20));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(pBotones, BorderLayout.NORTH);
        southPanel.add(pSocialContainer, BorderLayout.CENTER);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        setVisible(true);

        // Añade esto justo después de setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Crear un KeyListener para cerrar con ESC
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose(); // o System.exit(0);
                }
            }
        });

// Asegurarse de que el frame puede recibir eventos de teclado
        setFocusable(true);

    }
    public static void main(String[] args) {
        Login v = new Login();
        v.setVisible(true);
    }
}




