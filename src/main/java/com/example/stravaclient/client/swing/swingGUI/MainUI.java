package com.example.stravaclient.client.swing.swingGUI;

import com.example.stravaclient.client.swing.SwingController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Calendar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.table.TableColumnModel;

public class MainUI extends JFrame {
    private JTable tableChallengesAccepted;
    private JTable tableChallengesAvailable;
    private DefaultTableModel modelChallengesAccepted;
    private DefaultTableModel modelChallengesAvailable;
    private DefaultTableModel modelSessions;
    private final JPanel panelContent;


    private final SwingController controller;

    public MainUI(SwingController controller) {
        setTitle("Strava Client");
        this.controller = controller;
        setSize(800, 800);

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
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        // Panel superior con título y botón
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título
        JLabel titleLabel = new JLabel("My Challenges");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 66, 90));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Botón de crear nuevo reto
        JButton btnAddChallenge = createStyledButton("Create New Challenge");
        btnAddChallenge.setPreferredSize(new Dimension(200, 45));
        btnAddChallenge.addActionListener(e -> showAddChallengeDialog());

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.add(btnAddChallenge);
        headerPanel.add(buttonContainer, BorderLayout.EAST);

        // Panel central que contendrá las dos tablas
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Tabla de retos aceptados
        modelChallengesAccepted = new DefaultTableModel(
                new String[]{"Challenge Info", "Sport", "Goal", "Dates", "Progress"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChallengesAccepted = new JTable(modelChallengesAccepted);
        setupTable(tableChallengesAccepted, true);

        // Panel para la tabla de retos aceptados
        JPanel acceptedPanel = createTablePanel(tableChallengesAccepted, "Active Challenges");

        // Configurar el renderer para la barra de progreso
        tableChallengesAccepted.getColumnModel().getColumn(4).setCellRenderer(new ModernProgressRenderer());

        // Tabla de retos disponibles
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
        setupTable(tableChallengesAvailable, false);

        // Panel para la tabla de retos disponibles
        JPanel availablePanel = createTablePanel(tableChallengesAvailable, "Available Challenges");

        // Configurar el renderer y editor para los botones de aceptar
        tableChallengesAvailable.getColumn("Action").setCellRenderer(new ModernButtonRenderer());
        tableChallengesAvailable.getColumn("Action").setCellEditor(new ModernButtonEditor(new JCheckBox()));

        // Menú contextual para eliminar retos
        JPopupMenu popupMenu = createContextMenu();
        setupContextMenu(tableChallengesAccepted, popupMenu);

        // Añadir datos de ejemplo
        addExampleData();

        // Añadir los paneles al contenedor principal
        contentPanel.add(acceptedPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(availablePanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTablePanel(JTable table, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Título del panel
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 66, 90));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // ScrollPane mejorado
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupTable(JTable table, boolean isAcceptedTable) {
        table.setRowHeight(80);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(240, 245, 255));
        table.setSelectionForeground(new Color(34, 66, 90));
        table.setFocusable(false);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Estilo del header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(34, 66, 90));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // Ajustar el ancho de las columnas
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); // Challenge Info
        columnModel.getColumn(1).setPreferredWidth(100); // Sport
        columnModel.getColumn(2).setPreferredWidth(100); // Goal
        columnModel.getColumn(3).setPreferredWidth(150); // Dates
        columnModel.getColumn(4).setPreferredWidth(isAcceptedTable ? 150 : 100); // Progress/Action
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Remove Challenge");
        deleteItem.setFont(new Font("SansSerif", Font.PLAIN, 14));

        deleteItem.addActionListener(e -> {
            int row = tableChallengesAccepted.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to remove this challenge?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    modelChallengesAccepted.removeRow(row);
                }
            }
        });

        popupMenu.add(deleteItem);
        return popupMenu;
    }

    private void setupContextMenu(JTable table, JPopupMenu popup) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void addExampleData() {
        // Reto aceptado de ejemplo
        String challenge1 = formatChallengeInfo("Summer Running Challenge", "Distance challenge");
        String goal1 = formatGoalInfo("Distance", "100", "km");
        String dates1 = formatDatesInfo("2024-01-01", "2024-02-01");
        modelChallengesAccepted.addRow(new Object[]{
                challenge1, "Running", goal1, dates1, 0.25
        });

        // Reto disponible de ejemplo
        String challenge2 = formatChallengeInfo("Mountain Biking Week", "Time challenge");
        String goal2 = formatGoalInfo("Time", "20", "hours");
        String dates2 = formatDatesInfo("2024-02-01", "2024-02-28");
        modelChallengesAvailable.addRow(new Object[]{
                challenge2, "Cycling", goal2, dates2, "Accept"
        });
    }

    private String formatChallengeInfo(String title, String subtitle) {
        return String.format("<html><div style='padding: 5px;'>" +
                "<span style='font-family: SansSerif; font-size: 14px; font-weight: bold; color: rgb(34, 66, 90);'>%s</span><br/>" +
                "<span style='font-family: SansSerif; font-size: 12px; color: rgb(128, 128, 128);'>%s</span>" +
                "</div></html>", title, subtitle);
    }

    private String formatGoalInfo(String type, String value, String unit) {
        return String.format("<html><div style='padding: 5px;'>" +
                "<span style='font-family: SansSerif; font-size: 12px; color: rgb(128, 128, 128);'>%s</span><br/>" +
                "<span style='font-family: SansSerif; font-size: 14px; font-weight: bold; color: rgb(34, 66, 90);'>%s %s</span>" +
                "</div></html>", type, value, unit);
    }

    private String formatDatesInfo(String start, String end) {
        return String.format("<html><div style='padding: 5px;'>" +
                "<span style='font-family: SansSerif; font-size: 12px; color: rgb(128, 128, 128);'>Start: %s</span><br/>" +
                "<span style='font-family: SansSerif; font-size: 12px; color: rgb(128, 128, 128);'>End: %s</span>" +
                "</div></html>", start, end);
    }

    static class ModernProgressRenderer extends JProgressBar implements TableCellRenderer {
        private final Color backgroundColor = new Color(240, 245, 255);
        private final Color progressColor = new Color(34, 66, 90);
        private final Color textColor = new Color(95, 123, 144);
        private final Font textFont = new Font("SansSerif", Font.BOLD, 14);

        public ModernProgressRenderer() {
            super(0, 100);
            setStringPainted(true);
            setBackground(backgroundColor);
            setForeground(progressColor);
            setBorderPainted(false);
            setOpaque(true);
            // Aumentar altura de la barra
            setPreferredSize(new Dimension(150, 25));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof Double) {
                double progress = (Double) value;
                setValue((int) (progress * 100));

                // Formato personalizado del texto
                String progressText = String.format("%.0f%%", progress * 100);
                setString(progressText);
                setFont(textFont);
                setStringPainted(true);

                // Ajustar color del texto según el progreso
                if (progress > 0.5) {
                    setForeground(progressColor);
                    setBackground(backgroundColor);
                } else {
                    setForeground(progressColor);
                    setBackground(backgroundColor);
                }
            }

            // Añadir un borde redondeado personalizado
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar el fondo
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            // Dibujar la barra de progreso
            int width = (int) ((getWidth() - 4) * getPercentComplete());
            if (width > 0) {
                g2d.setColor(progressColor);
                g2d.fillRoundRect(2, 2, width, getHeight() - 4, 8, 8);
            }

            // Dibujar el texto
            if (isStringPainted()) {
                String progressText = getString();
                FontMetrics fm = g2d.getFontMetrics(textFont);
                int textWidth = fm.stringWidth(progressText);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - fm.getDescent();

                g2d.setFont(textFont);
                g2d.setColor(textColor);
                g2d.drawString(progressText, x, y);
            }

            g2d.dispose();
        }
    }

    static class ModernButtonRenderer extends JButton implements TableCellRenderer {
        public ModernButtonRenderer() {
            setOpaque(true);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(new Color(34, 66, 90));
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    class ModernButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ModernButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(34, 66, 90));
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(42, 82, 112));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(34, 66, 90));
                }
            });

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
                    Object[] rowData = new Object[5];
                    for (int i = 0; i < 4; i++) {
                        rowData[i] = modelChallengesAvailable.getValueAt(selectedRow, i);
                    }
                    rowData[4] = 0.0; // Progreso inicial

                    modelChallengesAccepted.addRow(rowData);
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
        JDialog dialog = new JDialog(this, "Create New Challenge", true);
        dialog.setLayout(new BorderLayout(0, 20));
        dialog.getContentPane().setBackground(Color.WHITE);

        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JLabel lblTitle = new JLabel("Create New Challenge");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 66, 90));
        titlePanel.add(lblTitle);

        // Panel de formulario
        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos del formulario
        JTextField nameField = createStyledTextField();
        addFormField(form, "Challenge Name:", nameField);

        String[] sports = {"Running", "Cycling", "Swimming", "Walking", "Hiking"};
        JComboBox<String> sportCombo = createStyledComboBox(sports);
        addFormField(form, "Sport:", sportCombo);

        String[] goalTypes = {"Distance", "Time"};
        JComboBox<String> goalTypeCombo = createStyledComboBox(goalTypes);
        addFormField(form, "Goal Type:", goalTypeCombo);

        JSpinner goalSpinner = createStyledSpinner(1, 1, 1000, 1);
        addFormField(form, "Goal Value:", goalSpinner);

        // Panel de fecha de inicio
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        startDatePanel.setBackground(Color.WHITE);

        JComboBox<Integer> startDayCombo = createStyledComboBox(generateNumbers(1, 31));
        JComboBox<Integer> startMonthCombo = createStyledComboBox(generateNumbers(1, 12));
        JComboBox<Integer> startYearCombo = createStyledComboBox(generateNumbers(2024, 2025));

        startDatePanel.add(startDayCombo);
        startDatePanel.add(createSeparatorLabel("/"));
        startDatePanel.add(startMonthCombo);
        startDatePanel.add(createSeparatorLabel("/"));
        startDatePanel.add(startYearCombo);

        addFormField(form, "Start Date:", startDatePanel);

        // Panel de fecha de fin
        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        endDatePanel.setBackground(Color.WHITE);

        JComboBox<Integer> endDayCombo = createStyledComboBox(generateNumbers(1, 31));
        JComboBox<Integer> endMonthCombo = createStyledComboBox(generateNumbers(1, 12));
        JComboBox<Integer> endYearCombo = createStyledComboBox(generateNumbers(2024, 2025));

        endDatePanel.add(endDayCombo);
        endDatePanel.add(createSeparatorLabel("/"));
        endDatePanel.add(endMonthCombo);
        endDatePanel.add(createSeparatorLabel("/"));
        endDatePanel.add(endYearCombo);

        addFormField(form, "End Date:", endDatePanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.setBackground(new Color(128, 128, 128));

        saveButton.addActionListener(e -> {
            if (validateChallengeForm(dialog, nameField, goalSpinner, startYearCombo, startMonthCombo,
                    startDayCombo, endYearCombo, endMonthCombo, endDayCombo)) {

                addNewChallenge(nameField.getText().trim(), Objects.requireNonNull(sportCombo.getSelectedItem()).toString(),
                        Objects.requireNonNull(goalTypeCombo.getSelectedItem()).toString(), goalSpinner.getValue(),
                        startYearCombo, startMonthCombo, startDayCombo,
                        endYearCombo, endMonthCombo, endDayCombo);
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean validateChallengeForm(JDialog dialog, JTextField nameField, JSpinner goalSpinner,
                                          JComboBox<Integer> startYearCombo, JComboBox<Integer> startMonthCombo,
                                          JComboBox<Integer> startDayCombo, JComboBox<Integer> endYearCombo,
                                          JComboBox<Integer> endMonthCombo, JComboBox<Integer> endDayCombo) {
        // Validar nombre
        if (nameField.getText().trim().isEmpty()) {
            showErrorDialog(dialog, "Challenge name cannot be empty");
            return false;
        }

        // Validar valor del objetivo
        if ((Integer)goalSpinner.getValue() <= 0) {
            showErrorDialog(dialog, "Goal value must be greater than 0");
            return false;
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

        if (startCal.before(today)) {
            showErrorDialog(dialog, "Start date cannot be before today");
            return false;
        }

        if (endCal.before(startCal)) {
            showErrorDialog(dialog, "End date cannot be before start date");
            return false;
        }

        return true;
    }

    private void showErrorDialog(JDialog parent, String message) {
        JOptionPane.showMessageDialog(parent,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void addNewChallenge(String name, String sport, String goalType, Object goalValue,
                                 JComboBox<Integer> startYearCombo, JComboBox<Integer> startMonthCombo,
                                 JComboBox<Integer> startDayCombo, JComboBox<Integer> endYearCombo,
                                 JComboBox<Integer> endMonthCombo, JComboBox<Integer> endDayCombo) {

        String startDate = String.format("%d-%02d-%02d",
                startYearCombo.getSelectedItem(),
                startMonthCombo.getSelectedItem(),
                startDayCombo.getSelectedItem());

        String endDate = String.format("%d-%02d-%02d",
                endYearCombo.getSelectedItem(),
                endMonthCombo.getSelectedItem(),
                endDayCombo.getSelectedItem());

        String challengeInfo = formatChallengeInfo(name, goalType + " challenge");
        String goalInfo = formatGoalInfo(goalType, goalValue.toString(),
                goalType.equals("Distance") ? "km" : "hours");
        String datesInfo = formatDatesInfo(startDate, endDate);

        modelChallengesAvailable.addRow(new Object[]{
                challengeInfo,
                sport,
                goalInfo,
                datesInfo,
                "Accept"
        });
    }

    private Integer[] generateNumbers(int start, int end) {
        Integer[] numbers = new Integer[end - start + 1];
        for (int i = 0; i <= end - start; i++) {
            numbers[i] = start + i;
        }
        return numbers;
    }

    // Sessions Panel - Nueva implementación
    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        // Panel superior con título y botón
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título
        JLabel titleLabel = new JLabel("Training Sessions");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 66, 90));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Botón de añadir sesión
        JButton btnAddSession = createStyledButton("Add New Session");
        btnAddSession.setPreferredSize(new Dimension(200, 45));
        btnAddSession.addActionListener(e -> showAddSessionDialog());

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.add(btnAddSession);
        headerPanel.add(buttonContainer, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de sesiones mejorada
        modelSessions = new DefaultTableModel(
                new String[]{"Session Info", "Sport", "Distance", "Date", "Time", "Duration"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableSessions = new JTable(modelSessions);
        tableSessions.setRowHeight(70);
        tableSessions.setShowGrid(false);
        tableSessions.setBackground(Color.WHITE);
        tableSessions.setSelectionBackground(new Color(240, 245, 255));
        tableSessions.setSelectionForeground(new Color(34, 66, 90));
        tableSessions.setFocusable(false);
        tableSessions.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Estilo del header de la tabla
        tableSessions.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tableSessions.getTableHeader().setBackground(new Color(34, 66, 90));
        tableSessions.getTableHeader().setForeground(Color.WHITE);
        tableSessions.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // ScrollPane mejorado
        JScrollPane scrollSessions = new JScrollPane(tableSessions);
        scrollSessions.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollSessions.getViewport().setBackground(Color.WHITE);

        panel.add(scrollSessions, BorderLayout.CENTER);

        // Añadir datos de ejemplo con formato HTML mejorado
        addExampleSessionData();

        return panel;
    }

    private void addExampleSessionData() {
        String session1 = String.format("<html>" +
                        "<div style='padding: 5px;'>" +
                        "<span style='font-family: SansSerif; font-size: 14px; font-weight: bold; color: rgb(34, 66, 90);'>%s</span><br/>" +
                        "<span style='font-family: SansSerif; font-size: 13px;'>%s km</span><br/>" +
                        "<span style='font-family: SansSerif; font-size: 12px; color: rgb(128, 128, 128);'>%s</span>" +
                        "</div></html>",
                "Morning Run", "5.2", "30:45");

        modelSessions.addRow(new Object[]{
                session1, "Running", "5.2", "2024-01-03", "07:30", "30:45"
        });
    }

    private void showAddSessionDialog() {
        JDialog dialog = new JDialog(this, "Add New Session", true);
        dialog.setLayout(new BorderLayout(0, 20));
        dialog.getContentPane().setBackground(Color.WHITE);

        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JLabel lblTitle = new JLabel("Add New Training Session");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 66, 90));
        titlePanel.add(lblTitle);

        // Panel del formulario
        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos del formulario con estilo mejorado
        addFormField(form, "Title:", createStyledTextField());

        String[] sports = {"Running", "Cycling", "Swimming", "Walking", "Hiking"};
        JComboBox<String> sportCombo = createStyledComboBox(sports);
        addFormField(form, "Sport:", sportCombo);

        JSpinner distanceSpinner = createStyledSpinner(0.0, 0.0, 1000.0, 0.1);
        addFormField(form, "Distance (km):", distanceSpinner);

        // Panel de fecha
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(Color.WHITE);

        JComboBox<Integer> dayCombo = createStyledComboBox(generateNumbers(1, 31));
        JComboBox<Integer> monthCombo = createStyledComboBox(generateNumbers(1, 12));
        JComboBox<Integer> yearCombo = createStyledComboBox(generateNumbers(2020, Calendar.getInstance().get(Calendar.YEAR)));

        datePanel.add(dayCombo);
        datePanel.add(createSeparatorLabel("/"));
        datePanel.add(monthCombo);
        datePanel.add(createSeparatorLabel("/"));
        datePanel.add(yearCombo);

        addFormField(form, "Date:", datePanel);

        // Panel de hora
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.setBackground(Color.WHITE);

        JComboBox<Integer> hourCombo = createStyledComboBox(generateNumbers(0, 23));
        JComboBox<Integer> minuteCombo = createStyledComboBox(generateNumbers(0, 59));

        timePanel.add(hourCombo);
        timePanel.add(createSeparatorLabel(":"));
        timePanel.add(minuteCombo);

        addFormField(form, "Start Time:", timePanel);

        // Panel de duración
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        durationPanel.setBackground(Color.WHITE);

        JSpinner hoursSpinner = createStyledSpinner(0, 0, 23, 1);
        JSpinner minutesSpinner = createStyledSpinner(0, 0, 59, 1);
        JSpinner secondsSpinner = createStyledSpinner(0, 0, 59, 1);

        durationPanel.add(hoursSpinner);
        durationPanel.add(createSeparatorLabel(":"));
        durationPanel.add(minutesSpinner);
        durationPanel.add(createSeparatorLabel(":"));
        durationPanel.add(secondsSpinner);

        addFormField(form, "Duration:", durationPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.setBackground(new Color(128, 128, 128));

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Añadir los paneles al diálogo
        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Configurar acciones de los botones
        saveButton.addActionListener(e -> {
            // Aquí va la lógica de validación y guardado existente
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(90, 35));
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        ((JComponent) comboBox.getRenderer()).setBorder(new EmptyBorder(0, 5, 0, 5));
        return comboBox;
    }

    private JSpinner createStyledSpinner(Number value, Comparable<?> minimum, Comparable<?> maximum, Number stepSize) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, minimum, maximum, stepSize));
        spinner.setPreferredSize(new Dimension(70, 35));
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return spinner;
    }

    private void addFormField(JPanel form, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(34, 66, 90));
        form.add(label);
        form.add(field);
    }

    private JLabel createSeparatorLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(128, 128, 128));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(new Color(34, 66, 90));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(42, 82, 112));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(34, 66, 90));
            }
        });

        return button;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

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

        // Panel central con título e información
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Color.WHITE);

        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Información básica
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new BoxLayout(basicInfoPanel, BoxLayout.Y_AXIS));
        basicInfoPanel.setBackground(Color.WHITE);
        basicInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel nameLabel = new JLabel("John Doe");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setForeground(new Color(34, 66, 90));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("john.doe@example.com");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(128, 128, 128));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        basicInfoPanel.add(nameLabel);
        basicInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        basicInfoPanel.add(emailLabel);

        // Panel de estadísticas con el mismo estilo que los campos de formulario
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 112, 165), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Crear los paneles de datos con el mismo estilo que los campos
        statsPanel.add(createDataPanel("Height", "180 cm"));
        statsPanel.add(createDataPanel("Weight", "75 kg"));
        statsPanel.add(createDataPanel("Max HR", "185 bpm"));
        statsPanel.add(createDataPanel("Rest HR", "60 bpm"));

        // Panel de fecha de nacimiento
        JPanel birthdatePanel = new JPanel();
        birthdatePanel.setLayout(new BoxLayout(birthdatePanel, BoxLayout.Y_AXIS));
        birthdatePanel.setBackground(Color.WHITE);
        birthdatePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        birthdatePanel.add(createDataPanel("Birthdate", "1990-01-01"));
        birthdatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir todos los paneles al panel de información
        infoPanel.add(basicInfoPanel);
        infoPanel.add(statsPanel);
        infoPanel.add(birthdatePanel);

        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Botón de logout con el mismo estilo que los otros botones
        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setPreferredSize(new Dimension(200, 45));
        btnLogout.setBackground(new Color(34, 66, 90));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            new Login(controller);
            dispose();
        });

        // Efecto hover en el botón
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(42, 82, 112));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(34, 66, 90));
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(btnLogout);

        // Añadir todos los paneles principales
        panel.add(logoPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDataPanel(String label, String value) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Etiqueta con el mismo estilo que los labels del formulario
        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(new Color(100, 100, 100));
        labelComponent.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Valor con el mismo estilo que los campos del formulario
        JLabel valueComponent = new JLabel(value);
        valueComponent.setForeground(new Color(34, 66, 90));
        valueComponent.setFont(new Font("SansSerif", Font.PLAIN, 14));
        valueComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(labelComponent);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(valueComponent);
        return panel;
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (panelContent.getLayout());
        cl.show(panelContent, panelName);
    }

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }*/
}