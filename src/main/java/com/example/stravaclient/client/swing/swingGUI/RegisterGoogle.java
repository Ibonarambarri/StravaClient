package com.example.stravaclient.client.swing.swingGUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Calendar;

public class RegisterGoogle extends JFrame {
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtWeight;
    private JTextField txtHeight;
    private JTextField txtMaxHeartRate;
    private JTextField txtRestHeartRate;
    private JComboBox<Integer> dayCombo;
    private JComboBox<Integer> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JButton btnRegister;

    public RegisterGoogle() {
        setTitle("Register - Strava");
        setSize(500, 800); // Increased height for more fields
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Configurar la tecla ESC para cerrar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Logo Panel (mantener el mismo código del logo)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel logoContainer = new JPanel();
        logoContainer.setBackground(new Color(63, 73, 112));
        logoContainer.setPreferredSize(new Dimension(80, 80));
        logoContainer.setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon("src/main/resources/img/google.png");
        Image logoScaled = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoScaled));
        logoContainer.add(lblLogo, BorderLayout.CENTER);

        logoPanel.add(logoContainer);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Title and Form Panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Create Account", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 66, 90));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Panel - Ajustado para más campos
        formPanel = new JPanel(new GridLayout(15, 1, 0, 5));
        formPanel.setBackground(Color.WHITE);

        // Email field
        addFormField("Email:", txtEmail = createStyledTextField());

        // Name field
        addFormField("Name:", txtName = createStyledTextField());

        // Birthdate fields
        JPanel birthdatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        birthdatePanel.setBackground(Color.WHITE);

        // Initialize ComboBoxes

        dayCombo = new JComboBox<>(generateNumbers(1, 31));
        monthCombo = new JComboBox<>(generateNumbers(1,12));
        yearCombo = new JComboBox<>(generateNumbers(1940, Calendar.getInstance().get(Calendar.YEAR)));

        // Style ComboBoxes
        styleComboBox(dayCombo);
        styleComboBox(monthCombo);
        styleComboBox(yearCombo);

        birthdatePanel.add(dayCombo);
        birthdatePanel.add(monthCombo);
        birthdatePanel.add(yearCombo);

        addFormField("Birthdate:", birthdatePanel);

        // Weight field
        addFormField("Weight (kg):", txtWeight = createStyledTextField());

        // Height field
        addFormField("Height (cm):", txtHeight = createStyledTextField());

        // Max Heart Rate field
        addFormField("Max Heart Rate:", txtMaxHeartRate = createStyledTextField());

        // Rest Heart Rate field
        addFormField("Rest Heart Rate:", txtRestHeartRate = createStyledTextField());

        contentPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Register button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        btnRegister = new JButton("REGISTER");
        btnRegister.setPreferredSize(new Dimension(200, 45));
        btnRegister.setBackground(new Color(34, 66, 90));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
            if (validateForm()) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
            }
        });

        buttonPanel.add(btnRegister);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void addFormField(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(label);
        formPanel.add(field);
    }

    private Integer[] generateNumbers(int start, int end) {
        Integer[] numbers = new Integer[end - start + 1];
        for (int i = 0; i <= end - start; i++) {
            numbers[i] = start + i;
        }
        return numbers;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(90, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ((JComponent) comboBox.getRenderer()).setBorder(new EmptyBorder(0, 5, 0, 0));
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 1),
                new EmptyBorder(5, 10, 5, 10)));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return textField;
    }

    private boolean validateForm() {
        if (txtEmail.getText().trim().isEmpty() ||
                txtName.getText().trim().isEmpty() ||
                txtWeight.getText().trim().isEmpty() ||
                txtHeight.getText().trim().isEmpty() ||
                txtMaxHeartRate.getText().trim().isEmpty() ||
                txtRestHeartRate.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(txtWeight.getText());
            Integer.parseInt(txtHeight.getText());
            Integer.parseInt(txtMaxHeartRate.getText());
            Integer.parseInt(txtRestHeartRate.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for weight, height, and heart rates",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        new RegisterGoogle();
    }
}