package com.example.stravaclient.client.swing.swingGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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

    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Accepted Challenges Table
        modelChallengesAccepted = new DefaultTableModel(
                new String[]{"Challenge Info", "Sport", "Goal", "Dates"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChallengesAccepted = new JTable(modelChallengesAccepted);
        tableChallengesAccepted.setRowHeight(50);
        tableChallengesAccepted.setFocusable(false);
        tableChallengesAccepted.setShowGrid(false);
        JScrollPane scrollAccepted = new JScrollPane(tableChallengesAccepted);
        scrollAccepted.setBorder(BorderFactory.createTitledBorder("Accepted Challenges"));

        // Ejemplo de datos formato HTML para Accepted Challenges
        String challenge1 = String.format("<html><b>%s</b></html>", "Summer Running Challenge");
        String goal1 = String.format("<html>Distance<br/><b>100 km</b></html>");
        String dates1 = String.format("<html>Start: %s<br/>End: %s</html>", "2024-01-01", "2024-02-01");
        modelChallengesAccepted.addRow(new Object[]{challenge1, "Running", goal1, dates1});

        // Add Challenge Button
        JButton btnAddChallenge = new JButton("Create New Challenge");
        btnAddChallenge.addActionListener(e -> showAddChallengeDialog());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnAddChallenge);

        // Available Challenges Table with Accept button
        modelChallengesAvailable = new DefaultTableModel(
                new String[]{"Challenge Info", "Sport", "Goal", "Dates", "Action"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna del botón es editable
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? JButton.class : Object.class;
            }
        };

        tableChallengesAvailable = new JTable(modelChallengesAvailable);
        tableChallengesAvailable.setRowHeight(50);
        tableChallengesAvailable.setFocusable(false);
        tableChallengesAvailable.setShowGrid(false);

        // Configurar el renderer para los botones
        tableChallengesAvailable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        tableChallengesAvailable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollAvailable = new JScrollPane(tableChallengesAvailable);
        scrollAvailable.setBorder(BorderFactory.createTitledBorder("Available Challenges"));

        // Ejemplo de datos formato HTML para Available Challenges
        String challenge2 = String.format("<html><b>%s</b></html>", "Mountain Biking Week");
        String goal2 = String.format("<html>Time<br/><b>20 hours</b></html>");
        String dates2 = String.format("<html>Start: %s<br/>End: %s</html>", "2024-02-01", "2024-02-28");
        JButton acceptButton = new JButton("Accept");
        modelChallengesAvailable.addRow(new Object[]{challenge2, "Cycling", goal2, dates2, "Accept"});

        panel.add(scrollAccepted);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollAvailable);

        return panel;
    }

    // Clase para renderizar los botones en la tabla
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    // Clase para manejar los clics en los botones
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Aquí manejamos la acción del botón
                int selectedRow = tableChallengesAvailable.getSelectedRow();
                if (selectedRow != -1) {
                    // Obtener los datos de la fila seleccionada
                    Object[] rowData = new Object[4];
                    for (int i = 0; i < 4; i++) {
                        rowData[i] = modelChallengesAvailable.getValueAt(selectedRow, i);
                    }

                    // Añadir a challenges aceptados
                    modelChallengesAccepted.addRow(rowData);

                    // Eliminar de challenges disponibles
                    modelChallengesAvailable.removeRow(selectedRow);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private void showAddChallengeDialog() {
        JDialog dialog = new JDialog(this, "Add New Challenge", true);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Name
        form.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        form.add(nameField);

        // Sport type
        form.add(new JLabel("Sport:"));
        String[] sports = {"Running", "Cycling", "Swimming", "Walking", "Hiking"};
        JComboBox<String> sportCombo = new JComboBox<>(sports);
        form.add(sportCombo);

        // Goal Type
        form.add(new JLabel("Goal Type:"));
        String[] goalTypes = {"distance", "time"};
        JComboBox<String> goalTypeCombo = new JComboBox<>(goalTypes);
        form.add(goalTypeCombo);

        // Goal Value
        form.add(new JLabel("Goal Value:"));
        JSpinner goalSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        form.add(goalSpinner);

        // Start Date
        form.add(new JLabel("Start Date:"));
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> startDayCombo = new JComboBox<>(generateNumbers(1, 31));
        JComboBox<Integer> startMonthCombo = new JComboBox<>(generateNumbers(1, 12));
        JComboBox<Integer> startYearCombo = new JComboBox<>(generateNumbers(2024, 2025));
        startDatePanel.add(startDayCombo);
        startDatePanel.add(new JLabel("/"));
        startDatePanel.add(startMonthCombo);
        startDatePanel.add(new JLabel("/"));
        startDatePanel.add(startYearCombo);
        form.add(startDatePanel);

        // End Date
        form.add(new JLabel("End Date:"));
        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> endDayCombo = new JComboBox<>(generateNumbers(1, 31));
        JComboBox<Integer> endMonthCombo = new JComboBox<>(generateNumbers(1, 12));
        JComboBox<Integer> endYearCombo = new JComboBox<>(generateNumbers(2024, 2025));
        endDatePanel.add(endDayCombo);
        endDatePanel.add(new JLabel("/"));
        endDatePanel.add(endMonthCombo);
        endDatePanel.add(new JLabel("/"));
        endDatePanel.add(endYearCombo);
        form.add(endDatePanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            // Format dates
            String startDate = String.format("%d-%02d-%02d",
                    startYearCombo.getSelectedItem(),
                    startMonthCombo.getSelectedItem(),
                    startDayCombo.getSelectedItem());

            String endDate = String.format("%d-%02d-%02d",
                    endYearCombo.getSelectedItem(),
                    endMonthCombo.getSelectedItem(),
                    endDayCombo.getSelectedItem());

            // Create HTML formatted cells
            String challengeInfo = String.format("<html><b>%s</b></html>",
                    nameField.getText());

            String goalInfo = String.format("<html>%s<br/><b>%d %s</b></html>",
                    goalTypeCombo.getSelectedItem(),
                    goalSpinner.getValue(),
                    goalTypeCombo.getSelectedItem().equals("distance") ? "km" : "hours");

            String datesInfo = String.format("<html>Start: %s<br/>End: %s</html>",
                    startDate, endDate);

            // Add to available challenges table
            modelChallengesAvailable.addRow(new Object[]{
                    challengeInfo,
                    sportCombo.getSelectedItem(),
                    goalInfo,
                    datesInfo
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