package com.dsaProject.hotelbooking;

import com.dsaProject.dataStructures.RoomDataStore;
import com.dsaProject.dataStructures.RoomSearchService;
import com.dsaProject.dataStructures.RoomSortService;
import com.dsaProject.dataStructures.ServiceBooking;
import com.dsaProject.dataStructures.ServiceQueue;
import com.dsaProject.dataStructures.BookingHistory;
import com.dsaProject.dataStructures.RoomBooking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HotelBookingUI extends JFrame {
    private RoomDataStore dataStore;
    private JTable roomTable;
    private JComboBox<String> sortComboBox;
    private JTextField typeField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JButton searchButton;
    private JButton sortButton;
    private ServiceQueue spaQueue;
    private ServiceQueue restaurantQueue;
    private DefaultTableModel spaTableModel;
    private DefaultTableModel restaurantTableModel;
    private BookingHistory bookingHistory;
    private DefaultTableModel roomHistoryTableModel;
    private DefaultTableModel serviceHistoryTableModel;
    private JPanel mainPanel;
    private JComboBox<String> statusComboBox;

    public HotelBookingUI() {
        dataStore = new RoomDataStore("room.txt");
        spaQueue = new ServiceQueue("Spa");
        restaurantQueue = new ServiceQueue("Restaurant");
        bookingHistory = BookingHistory.getInstance(dataStore);
        
        // Initialize UI
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hotel Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with card layout
        mainPanel = new JPanel(new CardLayout());
        
        // Create menu panel
        JPanel menuPanel = new JPanel(new BorderLayout(0, 40));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 50));
        
        // Create title label
        JLabel titleLabel = new JLabel("Hotel Booking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 100, 180));
        menuPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        // Create buttons
        JButton roomBookingBtn = createMenuButton("Room Booking");
        JButton serviceBookingBtn = createMenuButton("Service Booking");
        JButton historyBtn = createMenuButton("Booking History");
        
        // Add action listeners
        roomBookingBtn.addActionListener(e -> showRoomBooking());
        serviceBookingBtn.addActionListener(e -> showServiceBooking());
        historyBtn.addActionListener(e -> showHistory());
        
        // Add buttons to panel with spacing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        
        buttonPanel.add(roomBookingBtn, gbc);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        buttonPanel.add(serviceBookingBtn, gbc);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        buttonPanel.add(historyBtn, gbc);
        
        menuPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add menu panel to card layout
        mainPanel.add(menuPanel, "MENU");
        
        // Add main panel to frame
        add(mainPanel);
        
        // Show the menu panel
        showPanel("MENU");
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(300, 60));
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 100, 150));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }
    
    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelName);
    }
    
    private void showRoomBooking() {
        if (!panelExists("ROOM_BOOKING")) {
            JPanel roomBookingPanel = createRoomBookingPanel();
            mainPanel.add(roomBookingPanel, "ROOM_BOOKING");
            // Update the room table after the panel is created
            updateRoomTable();
        }
        showPanel("ROOM_BOOKING");
    }
    
    private void showServiceBooking() {
        if (!panelExists("SERVICE_BOOKING")) {
            JPanel servicePanel = new JPanel(new BorderLayout());
            
            // Create main content panel
            JPanel contentPanel = new JPanel(new BorderLayout());
            JTabbedPane serviceTabbedPane = new JTabbedPane();
            
            // Create and add service panels
            JPanel spaPanel = createServicePanel("Spa");
            JPanel restaurantPanel = createServicePanel("Restaurant");
            
            serviceTabbedPane.addTab("Spa", spaPanel);
            serviceTabbedPane.addTab("Restaurant", restaurantPanel);
            
            contentPanel.add(serviceTabbedPane, BorderLayout.CENTER);
            
            // Add return button at the bottom
            JButton returnButton = new JButton("Return to Main Menu");
            returnButton.addActionListener(e -> showPanel("MENU"));
            returnButton.setPreferredSize(new Dimension(150, 30));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(returnButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            servicePanel.add(contentPanel, BorderLayout.CENTER);
            servicePanel.add(buttonPanel, BorderLayout.SOUTH);
            
            mainPanel.add(servicePanel, "SERVICE_BOOKING");
            
            // Add sample data after UI is set up
            addSampleQueueData();
        }
        showPanel("SERVICE_BOOKING");
    }
    
    private void showHistory() {
        if (!panelExists("HISTORY")) {
            JPanel historyPanel = new JPanel(new BorderLayout());
            JPanel historyContent = createHistoryPanel();
            
            // Add return button at the bottom
            JButton returnButton = new JButton("Return to Main Menu");
            returnButton.addActionListener(e -> showPanel("MENU"));
            returnButton.setPreferredSize(new Dimension(150, 30));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(returnButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            historyPanel.add(historyContent, BorderLayout.CENTER);
            historyPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            mainPanel.add(historyPanel, "HISTORY");
        }
        showPanel("HISTORY");
    }
    
    private boolean panelExists(String panelName) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(panelName)) {
                return true;
            }
        }
        return false;
    }

    private JPanel createRoomBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Main content panel that will hold everything except the return button
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Filter panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Room Type
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Room Type:"), gbc);
        
        typeField = new JTextField(10);
        typeField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateRoomTable(); }
            public void removeUpdate(DocumentEvent e) { updateRoomTable(); }
            public void insertUpdate(DocumentEvent e) { updateRoomTable(); }
        });
        gbc.gridx = 1;
        filterPanel.add(typeField, gbc);

        // Min Price
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Min Price:"), gbc);
        
        minPriceField = new JTextField(10);
        gbc.gridx = 1;
        filterPanel.add(minPriceField, gbc);

        // Max Price
        gbc.gridx = 0; gbc.gridy = 2;
        filterPanel.add(new JLabel("Max Price:"), gbc);
        
        maxPriceField = new JTextField(10);
        gbc.gridx = 1;
        filterPanel.add(maxPriceField, gbc);

        // Status filter
        gbc.gridx = 0; gbc.gridy++;
        filterPanel.add(new JLabel("Status:"), gbc);
        
        statusComboBox = new JComboBox<>(new String[]{"All", "Available", "Unavailable"});
        gbc.gridx = 1;
        filterPanel.add(statusComboBox, gbc);

        // Search Button
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> updateRoomTable());
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        filterPanel.add(searchButton, gbc);

        // Sort Combo
        gbc.gridx = 0; gbc.gridy++;
        filterPanel.add(new JLabel("Sort By:"), gbc);
        
        sortComboBox = new JComboBox<>(new String[]{"Room Number", "Price (Low to High)", "Price (High to Low)", "Rating"});
        gbc.gridx = 1;
        filterPanel.add(sortComboBox, gbc);

        // Sort Button
        sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortRooms());
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        filterPanel.add(sortButton, gbc);

        contentPanel.add(filterPanel, BorderLayout.WEST);

        // Room Table
        String[] columns = {"Room Number", "Type", "Price", "Rating", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the action column is editable
            }
        };
        
        roomTable = new JTable(model);
        roomTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        roomTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add content panel to main panel
        panel.add(contentPanel, BorderLayout.CENTER);

        // Add return button at the bottom
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(e -> showPanel("MENU"));
        returnButton.setPreferredSize(new Dimension(150, 30));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(returnButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createServicePanel(String serviceType) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Customer Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Customer Name:"), gbc);
        
        JTextField customerNameField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(customerNameField, gbc);

        // Room Number
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Room Number:"), gbc);
        
        JComboBox<String> roomCombo = new JComboBox<>();
        roomCombo.addItem(""); // Empty first item
        for (Room room : dataStore.getRoomList()) {
            roomCombo.addItem(room.getRoomNumber());
        }
        gbc.gridx = 1;
        inputPanel.add(roomCombo, gbc);

        // Add Button
        JButton addButton = new JButton("Add Booking");
        addButton.addActionListener(e -> {
            String customerName = customerNameField.getText().trim();
            String roomNumber = (String) roomCombo.getSelectedItem();
            
            // Validate inputs
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter customer name", "Error", JOptionPane.ERROR_MESSAGE);
                customerNameField.requestFocus();
                return;
            }
            
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a room number", "Error", JOptionPane.ERROR_MESSAGE);
                roomCombo.requestFocus();
                return;
            }
            
            // Create and add booking
            ServiceBooking booking = new ServiceBooking(customerName, roomNumber, serviceType);
            
            // Add to history
            bookingHistory.addServiceBooking(booking);
            
            // Add to the appropriate queue
            if ("Spa".equals(serviceType)) {
                spaQueue.addBooking(booking);
                updateServiceTable(spaTableModel, spaQueue);
            } else if ("Restaurant".equals(serviceType)) {
                restaurantQueue.addBooking(booking);
                updateServiceTable(restaurantTableModel, restaurantQueue);
            }
            
            // Show success message
            JOptionPane.showMessageDialog(panel, 
                serviceType + " booking added successfully!", 
                "Booking Added", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            customerNameField.setText("");
            roomCombo.setSelectedIndex(0);
            
            // Update history table
            updateHistoryTables();
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        // Process Next Button
        JButton processNextButton = new JButton("Process Next");
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(processNextButton, gbc);
        
        // Add action listener for Process Next button
        processNextButton.addActionListener(e -> {
            ServiceQueue queue = serviceType.equals("Spa") ? spaQueue : restaurantQueue;
            DefaultTableModel model = serviceType.equals("Spa") ? spaTableModel : restaurantTableModel;
            
            ServiceBooking nextBooking = queue.processNext();
            if (nextBooking != null) {
                nextBooking.setProcessed(true);
                JOptionPane.showMessageDialog(panel, 
                    "Processed booking for " + nextBooking.getCustomerName() + 
                    " (Room " + nextBooking.getRoomNumber() + ")", 
                    "Booking Processed", JOptionPane.INFORMATION_MESSAGE);
                updateServiceTable(model, queue);
            } else {
                JOptionPane.showMessageDialog(panel, 
                    "No bookings in the " + serviceType + " queue.", 
                    "Queue Empty", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add input panel to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);

        // Create table for service bookings
        String[] columns = {"#", "Room", "Customer", "Service", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Store the table model reference
        if ("Spa".equals(serviceType)) {
            spaTableModel = tableModel;
        } else {
            restaurantTableModel = tableModel;
        }

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create tabbed pane for different history types
        JTabbedPane historyTabs = new JTabbedPane();
        
        // Room Bookings Tab
        JPanel roomHistoryPanel = new JPanel(new BorderLayout());
        roomHistoryTableModel = new DefaultTableModel(
            new String[]{"#", "Room", "Customer", "Check-in", "Check-out", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable roomHistoryTable = new JTable(roomHistoryTableModel);
        roomHistoryPanel.add(new JScrollPane(roomHistoryTable), BorderLayout.CENTER);
        
        // Service Bookings Tab
        JPanel serviceHistoryPanel = new JPanel(new BorderLayout());
        serviceHistoryTableModel = new DefaultTableModel(
            new String[]{"#", "Room", "Customer", "Service", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable serviceHistoryTable = new JTable(serviceHistoryTableModel);
        serviceHistoryPanel.add(new JScrollPane(serviceHistoryTable), BorderLayout.CENTER);
        
        // Add tabs
        historyTabs.addTab("Room Bookings", roomHistoryPanel);
        historyTabs.addTab("Service Bookings", serviceHistoryPanel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Undo Button
        JButton undoButton = new JButton("Undo Last Action");
        undoButton.addActionListener(e -> undoLastAction());
        buttonPanel.add(undoButton);
        
        // Generate Bill Button
        JButton billButton = new JButton("Generate Bill");
        billButton.addActionListener(e -> generateBill());
        buttonPanel.add(billButton);
        
        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateHistoryTables());
        buttonPanel.add(refreshButton);
        
        // Add components to main panel
        panel.add(historyTabs, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial data load
        updateHistoryTables();
        
        return panel;
    }
    
    private void updateHistoryTables() {
        // Update room history
        updateRoomHistoryTable();
        
        // Update service history
        updateServiceHistoryTable();
    }
    
    private void updateRoomHistoryTable() {
        roomHistoryTableModel.setRowCount(0);
        int position = 1;
        for (RoomBooking booking : bookingHistory.getRoomHistory()) {
            roomHistoryTableModel.addRow(new Object[]{
                position++,
                booking.getRoomNumber(),
                booking.getCustomerName(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.isCancelled() ? "Cancelled" : "Active"
            });
        }
    }
    
    private void updateServiceHistoryTable() {
        serviceHistoryTableModel.setRowCount(0);
        int position = 1;
        for (ServiceBooking booking : bookingHistory.getServiceHistory()) {
            serviceHistoryTableModel.addRow(new Object[]{
                position++,
                booking.getRoomNumber(),
                booking.getCustomerName(),
                booking.getServiceType(),
                booking.isProcessed() ? "Processed" : "Pending"
            });
        }
    }
    
    private void undoLastAction() {
        // Try to undo last service booking first
        ServiceBooking lastService = bookingHistory.undoLastServiceBooking();
        if (lastService != null) {
            // Remove from the appropriate queue
            if ("Spa".equals(lastService.getServiceType())) {
                spaQueue.removeBooking(lastService);
                updateServiceTable(spaTableModel, spaQueue);
            } else {
                restaurantQueue.removeBooking(lastService);
                updateServiceTable(restaurantTableModel, restaurantQueue);
            }
            JOptionPane.showMessageDialog(this, 
                "Undo successful: Removed " + lastService.getServiceType() + " booking for " + 
                lastService.getCustomerName(), "Undo Successful", JOptionPane.INFORMATION_MESSAGE);
            updateHistoryTables();
            return;
        }
        
        // If no service booking to undo, try to undo room booking
        RoomBooking lastRoom = bookingHistory.undoLastRoomBooking();
        if (lastRoom != null) {
            // Mark room as available again
            dataStore.bookRoom(lastRoom.getRoomNumber());
            updateRoomTable();
            JOptionPane.showMessageDialog(this, 
                "Undo successful: Cancelled room booking for " + lastRoom.getRoomNumber(), 
                "Undo Successful", JOptionPane.INFORMATION_MESSAGE);
            updateHistoryTables();
            return;
        }
        
        // If nothing to undo
        JOptionPane.showMessageDialog(this, 
            "No actions to undo", "Undo Failed", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generateBill() {
        String roomNumber = JOptionPane.showInputDialog(this, 
            "Enter Room Number to generate bill:");
            
        if (roomNumber != null && !roomNumber.trim().isEmpty()) {
            String bill = bookingHistory.generateBill(roomNumber.trim());
            JTextArea textArea = new JTextArea(bill);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                "Bill for Room " + roomNumber, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateRoomTable() {
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        model.setRowCount(0);

        String type = typeField.getText().trim();
        if (type.isEmpty()) type = null;

        int minPrice = -1;
        try {
            if (!minPriceField.getText().isEmpty()) {
                minPrice = Integer.parseInt(minPriceField.getText());
            }
        } catch (NumberFormatException e) {
            // Keep default value
        }

        int maxPrice = -1;
        try {
            if (!maxPriceField.getText().isEmpty()) {
                maxPrice = Integer.parseInt(maxPriceField.getText());
            }
        } catch (NumberFormatException e) {
            // Keep default value
        }

        String status = statusComboBox.getSelectedItem().toString();
        status = "All".equals(status) ? null : status;

        List<Room> rooms = RoomSearchService.searchRooms(
            dataStore.getRoomList(), type, status, minPrice, maxPrice
        );

        for (Room room : rooms) {
            model.addRow(new Object[]{
                room.getRoomNumber(),
                room.getRoomType(),
                "$" + room.getPrice(),
                room.getRating() + "★",
                room.getStatus(),
                "Book Now"
            });
        }
    }

    private void sortRooms() {
        String sortBy = sortComboBox.getSelectedItem().toString();
        switch (sortBy) {
            case "Room Number":
                RoomSortService.sortByRoomNumber(dataStore.getRoomList(), true);
                break;
            case "Price (Low to High)":
                RoomSortService.sortByPrice(dataStore.getRoomList(), true);
                break;
            case "Price (High to Low)":
                RoomSortService.sortByPrice(dataStore.getRoomList(), false);
                break;
            case "Rating":
                RoomSortService.sortByRating(dataStore.getRoomList(), true);
                break;
        }
        updateRoomTable();
    }

    private void updateServiceTable(DefaultTableModel model, ServiceQueue queue) {
        model.setRowCount(0);
        int position = 1;
        
        for (ServiceBooking booking : queue.getAllBookings()) {
            model.addRow(new Object[]{
                position++,
                booking.getRoomNumber(),
                booking.getCustomerName(),
                booking.isProcessed() ? "Processed" : "Pending"
            });
        }
    }

    // Button Renderer and Editor for the Book Now button
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

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
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = roomTable.convertRowIndexToModel(roomTable.getEditingRow());
                String roomNumber = roomTable.getModel().getValueAt(row, 0).toString();
                
                if (dataStore.bookRoom(roomNumber)) {
                    // Add room booking to history
                    RoomBooking roomBooking = new RoomBooking(
                        roomNumber,
                        "Guest",  // Default customer name since it's not collected during room booking
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),  // Default 1 night stay
                        100.0  // Default room rate
                    );
                    bookingHistory.addRoomBooking(roomBooking);
                    
                    JOptionPane.showMessageDialog(button, "Room " + roomNumber + " booked successfully!");
                    updateRoomTable();
                } else {
                    JOptionPane.showMessageDialog(button, "Room " + roomNumber + " is not available!", 
                        "Booking Failed", JOptionPane.WARNING_MESSAGE);
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

    private void addSampleQueueData() {
        // Clear any existing data
        while (spaQueue.viewNext() != null) {
            spaQueue.processNext();
        }
        while (restaurantQueue.viewNext() != null) {
            restaurantQueue.processNext();
        }
        
        // Add sample data to Spa queue
        ServiceBooking spa1 = new ServiceBooking("John Doe", "101", "Spa");
        ServiceBooking spa2 = new ServiceBooking("Jane Smith", "102", "Spa");
        spaQueue.addBooking(spa1);
        spaQueue.addBooking(spa2);
        
        // Add sample data to Restaurant queue
        ServiceBooking rest1 = new ServiceBooking("Alice Johnson", "103", "Restaurant");
        ServiceBooking rest2 = new ServiceBooking("Bob Wilson", "104", "Restaurant");
        restaurantQueue.addBooking(rest1);
        restaurantQueue.addBooking(rest2);
        
        // Update the tables to show the sample data if models are initialized
        if (spaTableModel != null) {
            updateServiceTable(spaTableModel, spaQueue);
        }
        if (restaurantTableModel != null) {
            updateServiceTable(restaurantTableModel, restaurantQueue);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            HotelBookingUI ui = new HotelBookingUI();
            ui.setVisible(true);
        });
    }
}