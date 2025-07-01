import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class OrganizerPanel {
    private org organizer;
    private JTextArea eventDisplayArea;
    private JList<Event> eventList;
    private DefaultListModel<Event> listModel;
    private Event selectedEvent = null;
    private JTextField eventName, eventDate, eventVenue, eventFee, eventCapacity;
    private JComboBox<String> eventType;
    private JTextField idField, nameField;

    public OrganizerPanel(org organizer) {
        this.organizer = organizer;
    }

    public JPanel createOrganizerPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Back button at the top
        JButton backButton = new JButton("← Back to Main");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        mainPanel.add(backButton, BorderLayout.NORTH);

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        // Left panel - Input form
        JPanel leftPanel = createInputPanel();
        splitPane.setLeftComponent(leftPanel);

        // Right panel - Event display
        JPanel rightPanel = createDisplayPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Event Management", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        // Organizer Info Panel
        JPanel orgPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        orgPanel.setBorder(BorderFactory.createTitledBorder("Organizer Information"));
        
        idField = new JTextField();
        nameField = new JTextField();
        
        orgPanel.add(new JLabel("Organizer ID:"));
        orgPanel.add(idField);
        orgPanel.add(new JLabel("Organizer Name:"));
        orgPanel.add(nameField);

        // Event Info Panel
        JPanel eventPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        eventPanel.setBorder(BorderFactory.createTitledBorder("Event Information"));

        eventName = new JTextField();
        eventDate = new JTextField();
        
        // Add focus listener to date field for real-time validation
        eventDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String dateText = eventDate.getText().trim();
                if (!dateText.isEmpty() && !DateValidator.isValidFutureDate(dateText)) {
                    eventDate.setBackground(new java.awt.Color(255, 200, 200)); // Light red
                    eventDate.setToolTipText(DateValidator.getDateErrorMessage(dateText));
                } else {
                    eventDate.setBackground(java.awt.Color.WHITE);
                    eventDate.setToolTipText("Enter date in DD/MM/YYYY format");
                }
            }
        });
        
        eventVenue = new JTextField();
        eventType = new JComboBox<>(Event.EVENT_TYPES);
        eventFee = new JTextField();
        eventCapacity = new JTextField();

        eventPanel.add(new JLabel("Event Name:"));
        eventPanel.add(eventName);
        eventPanel.add(new JLabel("<html>Date (DD/MM/YYYY):<br><small>Today: " + DateValidator.getTodayDate() + "</small></html>"));
        eventPanel.add(eventDate);
        eventPanel.add(new JLabel("Venue:"));
        eventPanel.add(eventVenue);
        eventPanel.add(new JLabel("Event Type:"));
        eventPanel.add(eventType);
        eventPanel.add(new JLabel("Registration Fee ($):"));
        eventPanel.add(eventFee);
        eventPanel.add(new JLabel("Capacity:"));
        eventPanel.add(eventCapacity);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Event");
        JButton updateButton = new JButton("Update Event");
        JButton deleteButton = new JButton("Delete Event");
        JButton clearButton = new JButton("Clear Fields");

        createButton.setBackground(new Color(46, 125, 50));
        createButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(25, 118, 210));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(158, 158, 158));
        clearButton.setForeground(Color.WHITE);

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Add components to input panel
        inputPanel.add(orgPanel, BorderLayout.NORTH);
        inputPanel.add(eventPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        createButton.addActionListener(e -> createEvent(inputPanel));
        updateButton.addActionListener(e -> updateEvent(inputPanel));
        deleteButton.addActionListener(e -> deleteEvent(inputPanel));
        clearButton.addActionListener(e -> clearFields());

        return inputPanel;
    }

    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Event Details", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        // Event list
        listModel = new DefaultListModel<>();
        
        // Load existing events from organizer
        for (Event event : organizer.getEvents()) {
            listModel.addElement(event);
        }
        
        eventList = new JList<>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setVisibleRowCount(8);
        eventList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedEvent = eventList.getSelectedValue();
                updateEventDisplay();
                populateFormFields(); // Add this to populate form when event is selected
            }
        });

        JScrollPane listScroll = new JScrollPane(eventList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Events List"));

        // Event details display
        eventDisplayArea = new JTextArea(15, 30);
        eventDisplayArea.setEditable(false);
        eventDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        eventDisplayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        eventDisplayArea.setText("Select an event from the list to view details...");

        JScrollPane displayScroll = new JScrollPane(eventDisplayArea);
        displayScroll.setBorder(BorderFactory.createTitledBorder("Event Information"));

        displayPanel.add(listScroll, BorderLayout.WEST);
        displayPanel.add(displayScroll, BorderLayout.CENTER);

        return displayPanel;
    }

    private void createEvent(JPanel parent) {
        try {
            // Validate organizer info
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please fill in organizer information.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate event info
            String eName = eventName.getText().trim();
            String eDate = eventDate.getText().trim();
            String eVenue = eventVenue.getText().trim();
            String eType = (String) eventType.getSelectedItem();
            
            if (eName.isEmpty() || eDate.isEmpty() || eVenue.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please fill in all event information.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate date format and ensure it's not in the past
            if (!DateValidator.isValidFutureDate(eDate)) {
                JOptionPane.showMessageDialog(parent, DateValidator.getDateErrorMessage(eDate), 
                    "Date Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double fee = 0.0;
            int capacity = 0;
            
            try {
                if (!eventFee.getText().trim().isEmpty()) {
                    fee = Double.parseDouble(eventFee.getText().trim());
                }
                capacity = Integer.parseInt(eventCapacity.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter valid numbers for fee and capacity.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set organizer info
            organizer.setOrganizerID(id);
            organizer.setOrganizerName(name);

            // Create event
            Event newEvent = new Event(eName, eDate, eVenue, eType, fee, capacity);
            organizer.addEvent(newEvent);
            listModel.addElement(newEvent);

            JOptionPane.showMessageDialog(parent, "Event created successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);

            // Select the newly created event
            eventList.setSelectedValue(newEvent, true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error creating event: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEvent(JPanel parent) {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(parent, "Please select an event to update.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate organizer info
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please fill in organizer information.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String eName = eventName.getText().trim();
            String eDate = eventDate.getText().trim();
            String eVenue = eventVenue.getText().trim();
            String eType = (String) eventType.getSelectedItem();
            
            if (eName.isEmpty() || eDate.isEmpty() || eVenue.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please fill in all event information.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate date format and ensure it's not in the past
            if (!DateValidator.isValidFutureDate(eDate)) {
                JOptionPane.showMessageDialog(parent, DateValidator.getDateErrorMessage(eDate), 
                    "Date Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double fee = 0.0;
            int capacity = 0;
            
            try {
                if (!eventFee.getText().trim().isEmpty()) {
                    fee = Double.parseDouble(eventFee.getText().trim());
                }
                capacity = Integer.parseInt(eventCapacity.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter valid numbers for fee and capacity.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set organizer info
            organizer.setOrganizerID(id);
            organizer.setOrganizerName(name);

            // Update event
            selectedEvent.setName(eName);
            selectedEvent.setDate(eDate);
            selectedEvent.setVenue(eVenue);
            selectedEvent.setType(eType);
            selectedEvent.setRegistrationFee(fee);
            selectedEvent.setCapacity(capacity);

            // Save the updated events to file
            EventFileManager.saveEventsToFile(organizer.getEvents(), organizer.getOrganizerID(), organizer.getOrganizerName());

            // Refresh display
            eventList.repaint();
            updateEventDisplay();

            JOptionPane.showMessageDialog(parent, "Event updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error updating event: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent(JPanel parent) {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(parent, "Please select an event to delete.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(parent, 
            "Are you sure you want to delete the event: " + selectedEvent.getName() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            organizer.removeEvent(selectedEvent);
            listModel.removeElement(selectedEvent);
            selectedEvent = null;
            eventDisplayArea.setText("Select an event from the list to view details...");
            
            JOptionPane.showMessageDialog(parent, "Event deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        eventName.setText("");
        eventDate.setText("");
        eventVenue.setText("");
        eventType.setSelectedIndex(0);
        eventFee.setText("");
        eventCapacity.setText("");
    }

    private void updateEventDisplay() {
        if (selectedEvent != null) {
            StringBuilder details = new StringBuilder();
            details.append("=== EVENT DETAILS ===\n\n");
            details.append(selectedEvent.getDetailedInfo());
            details.append("\n\n=== ORGANIZER INFO ===\n");
            details.append("Organizer ID: ").append(organizer.getOrganizerID()).append("\n");
            details.append("Organizer Name: ").append(organizer.getOrganizerName()).append("\n");
            
            eventDisplayArea.setText(details.toString());
        } else {
            eventDisplayArea.setText("Select an event from the list to view details...");
        }
    }

    private void populateFormFields() {
        if (selectedEvent != null) {
            // Populate organizer info if available
            if (!organizer.getOrganizerID().isEmpty()) {
                idField.setText(organizer.getOrganizerID());
            }
            if (!organizer.getOrganizerName().isEmpty()) {
                nameField.setText(organizer.getOrganizerName());
            }
            
            // Populate event info
            eventName.setText(selectedEvent.getName());
            eventDate.setText(selectedEvent.getDate());
            eventVenue.setText(selectedEvent.getVenue());
            eventType.setSelectedItem(selectedEvent.getType());
            eventFee.setText(String.valueOf(selectedEvent.getRegistrationFee()));
            eventCapacity.setText(String.valueOf(selectedEvent.getCapacity()));
        }
    }
}
