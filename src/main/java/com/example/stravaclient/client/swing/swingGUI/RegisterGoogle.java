package com.example.stravaclient.client.swing.swingGUI;
import com.example.stravaclient.client.data.User;
import com.example.stravaclient.client.swing.SwingController;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Calendar;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.*;

public class RegisterGoogle extends JFrame {
    private JPanel formPanel;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtWeight;
    private JTextField txtHeight;
    private JTextField txtMaxHeartRate;
    private JTextField txtRestHeartRate;
    private final SwingController controller;

    public RegisterGoogle(SwingController controller)
    {
        this.controller = new SwingController();
        setTitle("Register - Strava");
        setSize(500, 800); // Increased height for more fields
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Configurar la tecla ESC para cerrar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new Login(controller);
                dispose();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
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
        JTextField txtpassword;
        addFormField("Password:", txtpassword = createStyledTextField());

        // Birthdate fields
        JPanel birthdatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        birthdatePanel.setBackground(Color.WHITE);

        // Initialize ComboBoxes

        JComboBox<Integer> dayCombo = new JComboBox<>(generateNumbers(1, 31));
        JComboBox<Integer> monthCombo = new JComboBox<>(generateNumbers(1, 12));
        JComboBox<Integer> yearCombo = new JComboBox<>(generateNumbers(1940, Calendar.getInstance().get(Calendar.YEAR)));

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

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setPreferredSize(new Dimension(200, 45));
        btnRegister.setBackground(new Color(34, 66, 90));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
            if (validateForm()) {
                int day = (int) dayCombo.getSelectedItem();
                int month = (int) monthCombo.getSelectedItem();
                int year = (int) yearCombo.getSelectedItem();
                String birthday = String.format("%04d/%02d/%02d", year, month, day);
                User user = new User(
                        0,
                        txtEmail.getText(),
                        txtName.getText(),
                        birthday,
                        parseDouble(txtWeight.getText()),
                        parseDouble(txtHeight.getText()),
                        parseInt(txtMaxHeartRate.getText()),
                        parseInt(txtRestHeartRate.getText())
                );
                controller.register(user, "google", txtpassword.getText());
                JOptionPane.showMessageDialog(this, "Registration successful!");
                new Login(controller);
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
            parseDouble(txtWeight.getText());
            parseInt(txtHeight.getText());
            parseInt(txtMaxHeartRate.getText());
            parseInt(txtRestHeartRate.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for weight, height, and heart rates",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

   /* public static void main(String[] args) {
        new RegisterGoogle();
    }*/
}