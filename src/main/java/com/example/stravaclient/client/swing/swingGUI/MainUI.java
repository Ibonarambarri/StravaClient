package com.example.stravaclient.client.swing.swingGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;

public class MainUI extends JFrame {
    private JTable tableChallengesAccepted;
    private JTable tableChallengesAvailable;
    private JTable tableSessions;
    private DefaultTableModel modelChallengesAccepted;
    private DefaultTableModel modelChallengesAvailable;
    private DefaultTableModel modelSessions;
    private JPanel panelHome;
    private JPanel panelSessions;
    private JPanel panelProfile;
    private JPanel panelContent;

    public MainUI() {
        setTitle("Strava Client");
        setSize(500, 800);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panelContent = new JPanel(new CardLayout());

        panelHome = createHomePanel();
        panelSessions = createSessionsPanel();
        panelProfile = createProfilePanel();

        panelContent.add(panelHome, "Home");
        panelContent.add(panelSessions, "Sessions");
        panelContent.add(panelProfile, "Profile");

        // Bottom navigation menu
        JPanel panelBottomMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnHome = new JButton("Home");
        JButton btnSessions = new JButton("Sessions");
        JButton btnProfile = new JButton("Profile");

        btnHome.addActionListener(e -> showPanel("Home"));
        btnSessions.addActionListener(e -> showPanel("Sessions"));
        btnProfile.addActionListener(e -> showPanel("Profile"));

        panelBottomMenu.add(btnHome);
        panelBottomMenu.add(btnSessions);
        panelBottomMenu.add(btnProfile);

        add(panelContent, BorderLayout.CENTER);
        add(panelBottomMenu, BorderLayout.SOUTH);

        showPanel("Home");
        setVisible(true);
    }

    // Home Panel - Mantenido igual que en tu código original
    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        modelChallengesAccepted = new DefaultTableModel(
                new String[]{"Challenge Name", "Description", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChallengesAccepted = new JTable(modelChallengesAccepted);
        JScrollPane scrollAccepted = new JScrollPane(tableChallengesAccepted);
        scrollAccepted.setBorder(BorderFactory.createTitledBorder("Accepted Challenges"));

        modelChallengesAccepted.addRow(new Object[]{"10K Run", "Complete a 10K run this month", "In Progress"});
        modelChallengesAccepted.addRow(new Object[]{"Climb Challenge", "Climb 1000m this week", "Started"});

        JButton btnAddChallenge = new JButton("Create New Challenge");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnAddChallenge);

        modelChallengesAvailable = new DefaultTableModel(
                new String[]{"Challenge Name", "Description", "Difficulty"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChallengesAvailable = new JTable(modelChallengesAvailable);
        JScrollPane scrollAvailable = new JScrollPane(tableChallengesAvailable);
        scrollAvailable.setBorder(BorderFactory.createTitledBorder("Available Challenges"));

        modelChallengesAvailable.addRow(new Object[]{"Marathon Prep", "Complete 4 runs of 15K each", "Hard"});
        modelChallengesAvailable.addRow(new Object[]{"Daily Cyclist", "Bike 5km daily for a month", "Medium"});

        panel.add(scrollAccepted);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollAvailable);

        return panel;
    }

    // Sessions Panel - Nueva implementación
    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add Session Button at the top
        JButton btnAddSession = new JButton("Add New Session");
        btnAddSession.addActionListener(e -> showAddSessionDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnAddSession);
        panel.add(buttonPanel, BorderLayout.NORTH);

        // Sessions Table with HTML formatting
        modelSessions = new DefaultTableModel(
                new String[]{"Session Info", "Sport", "Distance", "Date", "Time"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSessions = new JTable(modelSessions);
        tableSessions.setRowHeight(60); // Altura aumentada para contenido HTML
        tableSessions.setFocusable(false);
        tableSessions.setShowGrid(false);
        JScrollPane scrollSessions = new JScrollPane(tableSessions);

        // Añadir datos de ejemplo con formato HTML
        addExampleSessionData();

        panel.add(scrollSessions, BorderLayout.CENTER);

        return panel;
    }

    private void addExampleSessionData() {
        String session1 = String.format("<html><b>%s</b><br/>%s km<br/><span style='color:gray'>%s</span></html>",
                "Morning Run", "5.2", "30:45");

        modelSessions.addRow(new Object[]{
                session1, "Running", "5.2", "2024-01-03", "07:30", "30:45"
        });
    }

    private void showAddSessionDialog() {
        JDialog dialog = new JDialog(this, "Add New Session", true);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        form.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        form.add(titleField);

        // Sport type
        form.add(new JLabel("Sport:"));
        String[] sports = {"Running", "Cycling", "Swimming", "Walking", "Hiking"};
        JComboBox<String> sportCombo = new JComboBox<>(sports);
        form.add(sportCombo);

        // Distance
        form.add(new JLabel("Distance (km):"));
        JSpinner distanceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
        form.add(distanceSpinner);

        // Date
        form.add(new JLabel("Date:"));
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> dayCombo = new JComboBox<>(generateNumbers(1, 31));
        JComboBox<Integer> monthCombo = new JComboBox<>(generateNumbers(1, 12));
        JComboBox<Integer> yearCombo = new JComboBox<>(generateNumbers(2020, Calendar.getInstance().get(Calendar.YEAR)));
        datePanel.add(dayCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearCombo);
        form.add(datePanel);

        // Start Time
        form.add(new JLabel("Start Time:"));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> hourCombo = new JComboBox<>(generateNumbers(0, 23));
        JComboBox<Integer> minuteCombo = new JComboBox<>(generateNumbers(0, 59));
        timePanel.add(hourCombo);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteCombo);
        form.add(timePanel);

        // Duration
        form.add(new JLabel("Duration:"));
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner hoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner minutesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JSpinner secondsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        durationPanel.add(hoursSpinner);
        durationPanel.add(new JLabel(":"));
        durationPanel.add(minutesSpinner);
        durationPanel.add(new JLabel(":"));
        durationPanel.add(secondsSpinner);
        form.add(durationPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            // Format date and time
            String date = String.format("%d-%02d-%02d",
                    yearCombo.getSelectedItem(),
                    monthCombo.getSelectedItem(),
                    dayCombo.getSelectedItem());

            String time = String.format("%02d:%02d",
                    hourCombo.getSelectedItem(),
                    minuteCombo.getSelectedItem());

            String duration = String.format("%02d:%02d:%02d",
                    hoursSpinner.getValue(),
                    minutesSpinner.getValue(),
                    secondsSpinner.getValue());

            // Create HTML formatted cell
            String titleCell = String.format("<html><b>%s</b><br/>%s km<br/><span style='color:gray'>%s</span></html>",
                    titleField.getText(),
                    distanceSpinner.getValue(),
                    duration);

            // Add to table
            modelSessions.addRow(new Object[]{
                    titleCell,
                    sportCombo.getSelectedItem(),
                    distanceSpinner.getValue(),
                    date,
                    time,
                    duration
            });

            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private Integer[] generateNumbers(int start, int end) {
        Integer[] numbers = new Integer[end - start + 1];
        for (int i = 0; i <= end - start; i++) {
            numbers[i] = start + i;
        }
        return numbers;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con foto de perfil
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.WHITE);
        // Crear un JPanel para envolver la imagen y aplicar el fondo rojo
        JPanel imageContainer = new JPanel();
        imageContainer.setBackground(new Color(63, 73, 112)); // Fondo rojo
        imageContainer.setPreferredSize(new Dimension(150, 150)); // Tamaño del contenedor
        imageContainer.setLayout(new BorderLayout());

        // Crear el JLabel con la imagen
        ImageIcon logo = new ImageIcon("src/main/resources/img/usuario.png");
        Image logoEsc = logo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoEsc));

        // No es necesario poner el fondo del JLabel, solo del contenedor
        imageContainer.add(lblLogo, BorderLayout.CENTER);
        topPanel.add(imageContainer);

        // Panel central con información del usuario
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nombre y email en panel superior
        JPanel basicInfoPanel = new JPanel();

        basicInfoPanel.setLayout(new BoxLayout(basicInfoPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = createStyledLabel("John Doe", 16, true);
        JLabel emailLabel = createStyledLabel("john.doe@example.com", 14, false);
        emailLabel.setForeground(new Color(100, 100, 100));

        basicInfoPanel.add(nameLabel);
        basicInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        basicInfoPanel.add(emailLabel);
        basicInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(basicInfoPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel para datos físicos
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Physical Stats"
        ));

        // Añadir datos físicos con formato HTML para mejor presentación
        statsPanel.add(createDataPanel("Height", "180 cm"));
        statsPanel.add(createDataPanel("Weight", "75 kg"));
        statsPanel.add(createDataPanel("Max HR", "185 bpm"));
        statsPanel.add(createDataPanel("Rest HR", "60 bpm"));

        infoPanel.add(statsPanel);

        // Añadir fecha de nacimiento
        JPanel birthdatePanel = new JPanel();
        birthdatePanel.setLayout(new BoxLayout(birthdatePanel, BoxLayout.Y_AXIS));
        birthdatePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        birthdatePanel.add(createDataPanel("Birthdate", "1990-01-01"));
        birthdatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(birthdatePanel);

        // Botón de logout
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            new Login();
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnLogout);

        // Añadir todos los paneles al panel principal
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createStyledLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getFontName(),
                bold ? Font.BOLD : Font.PLAIN,
                size));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createDataPanel(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(new Color(100, 100, 100));
        labelComponent.setFont(new Font(labelComponent.getFont().getFontName(), Font.PLAIN, 12));
        labelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(valueComponent.getFont().getFontName(), Font.BOLD, 14));
        valueComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(labelComponent);
        panel.add(valueComponent);
        return panel;
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (panelContent.getLayout());
        cl.show(panelContent, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI());
    }
}