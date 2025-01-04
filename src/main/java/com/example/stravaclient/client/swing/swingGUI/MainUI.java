package com.example.stravaclient.client.swing.swingGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Calendar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class MainUI extends JFrame {
    private JTable tableChallengesAccepted;
    private JTable tableChallengesAvailable;
    private DefaultTableModel modelChallengesAccepted;
    private DefaultTableModel modelChallengesAvailable;
    private DefaultTableModel modelSessions;
    private final JPanel panelContent;

    public MainUI() {
        setTitle("Strava Client");
        setSize(500, 800);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panelContent = new JPanel(new CardLayout());

        JPanel panelHome = createHomePanel();
        JPanel panelSessions = createSessionsPanel();
        JPanel panelProfile = createProfilePanel();

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

        // Accepted Challenges Table con barra de progreso
        modelChallengesAccepted = new DefaultTableModel(
                new String[]{"Challenge Info", "Sport", "Goal", "Dates", "Progress"}, 0
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

        // Configurar el renderer para la barra de progreso
        tableChallengesAccepted.getColumnModel().getColumn(4).setCellRenderer(new ProgressRenderer());

        JScrollPane scrollAccepted = new JScrollPane(tableChallengesAccepted);
        scrollAccepted.setBorder(BorderFactory.createTitledBorder("Accepted Challenges"));

        // Ejemplo de datos con barra de progreso
        String challenge1 = String.format("<html><b>%s</b></html>", "Summer Running Challenge");
        String goal1 = "<html>Distance<br/><b>100 km</b></html>";
        String dates1 = String.format("<html>Start: %s<br/>End: %s</html>", "2024-01-01", "2024-02-01");
        modelChallengesAccepted.addRow(new Object[]{challenge1, "Running", goal1, dates1, 0.75}); // 75% de progreso

        // Add Challenge Button
        JButton btnAddChallenge = new JButton("Create New Challenge");
        btnAddChallenge.addActionListener(e -> showAddChallengeDialog());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnAddChallenge);

        // Añadir al método createHomePanel()
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Eliminar Reto");
        deleteItem.addActionListener(e -> {
            int row = tableChallengesAccepted.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "¿Estás seguro de que quieres eliminar este reto?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    modelChallengesAccepted.removeRow(row);
                }
            }
        });
        popupMenu.add(deleteItem);

        tableChallengesAccepted.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tableChallengesAccepted.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < tableChallengesAccepted.getRowCount()) {
                        tableChallengesAccepted.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        // Available Challenges Table with Accept button
        modelChallengesAvailable = new DefaultTableModel(
                new String[]{"Challenge Info", "Sport", "Goal", "Dates", "Action"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
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

        tableChallengesAvailable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        tableChallengesAvailable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollAvailable = new JScrollPane(tableChallengesAvailable);
        scrollAvailable.setBorder(BorderFactory.createTitledBorder("Available Challenges"));

        // Ejemplo de datos
        String challenge2 = String.format("<html><b>%s</b></html>", "Mountain Biking Week");
        String goal2 = "<html>Time<br/><b>20 hours</b></html>";
        String dates2 = String.format("<html>Start: %s<br/>End: %s</html>", "2024-02-01", "2024-02-28");
        modelChallengesAvailable.addRow(new Object[]{challenge2, "Cycling", goal2, dates2, "Accept"});

        panel.add(scrollAccepted);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollAvailable);

        return panel;
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
            // Validar nombre
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "El nombre del reto no puede estar vacío",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar valor del objetivo
            if ((Integer)goalSpinner.getValue() <= 0) {
                JOptionPane.showMessageDialog(dialog,
                        "El objetivo debe ser mayor que 0",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar fechas
            Calendar startCal = Calendar.getInstance();
            startCal.set(
                    (Integer)startYearCombo.getSelectedItem(),
                    (Integer)startMonthCombo.getSelectedItem() - 1,
                    (Integer)startDayCombo.getSelectedItem()
            );

            Calendar endCal = Calendar.getInstance();
            endCal.set(
                    (Integer)endYearCombo.getSelectedItem(),
                    (Integer)endMonthCombo.getSelectedItem() - 1,
                    (Integer)endDayCombo.getSelectedItem()
            );

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            // Validar que la fecha de inicio no sea anterior a hoy
            if (startCal.before(today)) {
                JOptionPane.showMessageDialog(dialog,
                        "La fecha de inicio no puede ser anterior a hoy",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que la fecha de fin no sea anterior a la de inicio
            if (endCal.before(startCal)) {
                JOptionPane.showMessageDialog(dialog,
                        "La fecha de fin no puede ser anterior a la fecha de inicio",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
                    nameField.getText().trim());

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
                    datesInfo,
                    "Accept"
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

    // Renderer para la barra de progreso
    static class ProgressRenderer extends JProgressBar implements TableCellRenderer {
        public ProgressRenderer() {
            super(0, 100);
            setStringPainted(true);
            setBackground(Color.WHITE);
            setForeground(new Color(46, 139, 87)); // Verde oscuro
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof Double) {
                double progress = (Double) value;
                setValue((int) (progress * 100));
                setString(String.format("%.0f%%", progress * 100));
            }
            return this;
        }
    }

    // Renderer para los botones
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    // Editor para los botones con manejo correcto de datos
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
                int selectedRow = tableChallengesAvailable.getSelectedRow();
                if (selectedRow != -1) {
                    // Crear array con los datos necesarios y la barra de progreso inicial
                    Object[] rowData = new Object[5];
                    for (int i = 0; i < 4; i++) {
                        rowData[i] = modelChallengesAvailable.getValueAt(selectedRow, i);
                    }
                    rowData[4] = 0.0; // Progreso inicial 0%

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
                new String[]{"Session Info", "Sport", "Distance", "Date", "Time", "Duration"}, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableSessions = new JTable(modelSessions);
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
            // Validar título
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "El título no puede estar vacío",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar distancia
            if ((Double)distanceSpinner.getValue() <= 0) {
                JOptionPane.showMessageDialog(dialog,
                        "La distancia debe ser mayor que 0",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
            String sessionInfo = String.format("<html><b>%s</b><br/>%s km<br/><span style='color:gray'>%s</span></html>",
                    titleField.getText().trim(),
                    distanceSpinner.getValue(),
                    duration);

            // Add to table
            modelSessions.addRow(new Object[]{
                    sessionInfo,
                    sportCombo.getSelectedItem(),
                    distanceSpinner.getValue(),
                    date,
                    time
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
        SwingUtilities.invokeLater(MainUI::new);
    }
}