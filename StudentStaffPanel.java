import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentStaffPanel {
    private StudentStaff studentStaff;
    private org organizer; // Reference to organizer to get events
    private JList<Event> eventList;
    private DefaultListModel<Event> listModel;
    private Event selectedEvent = null;
    private JTextArea eventDetailsArea;
    
    // Input fields
    private JTextField studentIdField, studentNameField, numberOfRegistrationsField;
    private JComboBox<String> participantTypeCombo;
    private JButton registerButton, cancelButton, calculateFeeButton;
    
    // Additional services
    private List<AdditionalService> availableServices;
    private List<JCheckBox> serviceCheckboxes;
    private JCheckBox earlyBirdCheckbox;
    private FeeCalculator feeCalculator;

    public StudentStaffPanel(StudentStaff studentStaff) {
        this(studentStaff, null);
    }
    
    public StudentStaffPanel(StudentStaff studentStaff, org organizer) {
        this.studentStaff = studentStaff;
        this.organizer = organizer != null ? organizer : new org(); // Use provided organizer or create new one
        
        // Initialize additional services
        initializeServices();
        
        // Initialize fee calculator with combined discount strategy
        this.feeCalculator = new FeeCalculator(new CombinedDiscountStrategy());
    }
    
    private void initializeServices() {
        availableServices = new ArrayList<>();
        availableServices.add(new AdditionalService("Catering Package", 25.00));
        availableServices.add(new AdditionalService("Transportation", 15.00));
        availableServices.add(new AdditionalService("Event Materials Kit", 10.00));
        availableServices.add(new AdditionalService("Certificate of Participation", 5.00));
        
        serviceCheckboxes = new ArrayList<>();
    }

    public JPanel createStudentStaffPanel(CardLayout cardLayout, JPanel cardPanel) {
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

        // Right panel - Event selection
        JPanel rightPanel = createEventSelectionPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add component listener to refresh events when panel becomes visible
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshEventList();
            }
        });

        return mainPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Registration Form", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        // Main form panel
        JPanel mainFormPanel = new JPanel(new BorderLayout());

        // Basic registration form
        JPanel basicFormPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        basicFormPanel.setBorder(BorderFactory.createTitledBorder("Basic Information"));

        // Input fields
        studentIdField = new JTextField(15);
        studentNameField = new JTextField(15);
        participantTypeCombo = new JComboBox<>(new String[]{"Student", "Staff"});
        numberOfRegistrationsField = new JTextField("1", 15); // Default to 1
        
        // Add input validation for number of registrations
        numberOfRegistrationsField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String numText = numberOfRegistrationsField.getText().trim();
                try {
                    int num = Integer.parseInt(numText);
                    if (num <= 0) {
                        numberOfRegistrationsField.setBackground(new java.awt.Color(255, 200, 200));
                        numberOfRegistrationsField.setToolTipText("Number must be greater than 0");
                    } else if (selectedEvent != null && num > selectedEvent.getAvailableSpots()) {
                        numberOfRegistrationsField.setBackground(new java.awt.Color(255, 200, 200));
                        numberOfRegistrationsField.setToolTipText("Not enough spots available (" + selectedEvent.getAvailableSpots() + " remaining)");
                    } else {
                        numberOfRegistrationsField.setBackground(java.awt.Color.WHITE);
                        numberOfRegistrationsField.setToolTipText("Number of people to register");
                    }
                } catch (NumberFormatException e) {
                    numberOfRegistrationsField.setBackground(new java.awt.Color(255, 200, 200));
                    numberOfRegistrationsField.setToolTipText("Please enter a valid number");
                }
            }
        });

        basicFormPanel.add(new JLabel("ID:"));
        basicFormPanel.add(studentIdField);
        basicFormPanel.add(new JLabel("Name:"));
        basicFormPanel.add(studentNameField);
        basicFormPanel.add(new JLabel("Type:"));
        basicFormPanel.add(participantTypeCombo);
        basicFormPanel.add(new JLabel("Number of Registrations:"));
        basicFormPanel.add(numberOfRegistrationsField);

        // Additional services panel
        JPanel servicesPanel = new JPanel();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        servicesPanel.setBorder(BorderFactory.createTitledBorder("Additional Services"));

        serviceCheckboxes.clear();
        for (AdditionalService service : availableServices) {
            JCheckBox checkbox = new JCheckBox(service.toString());
            serviceCheckboxes.add(checkbox);
            servicesPanel.add(checkbox);
        }

        // Discount panel
        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discountPanel.setBorder(BorderFactory.createTitledBorder("Discounts"));
        earlyBirdCheckbox = new JCheckBox("Early Bird Registration (15% off)");
        discountPanel.add(earlyBirdCheckbox);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Register for Event");
        cancelButton = new JButton("Clear Form");
        calculateFeeButton = new JButton("Calculate Fee");

        registerButton.setBackground(new Color(46, 125, 50));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        cancelButton.setBackground(new Color(158, 158, 158));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));

        calculateFeeButton.setBackground(new Color(25, 118, 210));
        calculateFeeButton.setForeground(Color.WHITE);
        calculateFeeButton.setFont(new Font("Arial", Font.BOLD, 12));

        buttonPanel.add(calculateFeeButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        // Add components to main form panel
        mainFormPanel.add(basicFormPanel, BorderLayout.NORTH);
        mainFormPanel.add(servicesPanel, BorderLayout.CENTER);
        mainFormPanel.add(discountPanel, BorderLayout.SOUTH);

        // Add components to input panel
        inputPanel.add(mainFormPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        registerButton.addActionListener(e -> registerForEvent());
        cancelButton.addActionListener(e -> clearForm());
        calculateFeeButton.addActionListener(e -> showFeeCalculation());

        return inputPanel;
    }

    private JPanel createEventSelectionPanel() {
        JPanel selectionPanel = new JPanel(new BorderLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Available Events", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        // Event list
        listModel = new DefaultListModel<>();
        
        eventList = new JList<>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setVisibleRowCount(10);
        
        // Load events from organizer after eventList is initialized
        refreshEventList();
        
        eventList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedEvent = eventList.getSelectedValue();
                updateEventDetails();
                
                // Enable/disable register button based on availability
                if (selectedEvent != null) {
                    registerButton.setEnabled(selectedEvent.hasAvailableSpots());
                    if (!selectedEvent.hasAvailableSpots()) {
                        registerButton.setText("Event Full");
                    } else {
                        registerButton.setText("Register for Event");
                    }
                } else {
                    registerButton.setEnabled(false);
                    registerButton.setText("Register for Event");
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(eventList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Events List"));
        listScroll.setPreferredSize(new Dimension(300, 200));

        // Add refresh button
        JPanel listPanel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("🔄 Refresh Events");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 11));
        refreshButton.addActionListener(e -> refreshEventList());
        
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshPanel.add(refreshButton);
        
        listPanel.add(refreshPanel, BorderLayout.NORTH);
        listPanel.add(listScroll, BorderLayout.CENTER);

        // Event details display
        eventDetailsArea = new JTextArea(10, 25);
        eventDetailsArea.setEditable(false);
        eventDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        eventDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        eventDetailsArea.setText("Select an event from the list to view details...");

        JScrollPane detailsScroll = new JScrollPane(eventDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Event Details"));

        // Add components to selection panel
        selectionPanel.add(listPanel, BorderLayout.NORTH);
        selectionPanel.add(detailsScroll, BorderLayout.CENTER);

        return selectionPanel;
    }

    private void refreshEventList() {
        // Clear the current list
        listModel.clear();
        
        // Use the shared organizer instance instead of creating a fresh one
        // This ensures we see the same events that were created in the organizer panel
        
        // Add all events to the list model
        for (Event event : organizer.getEvents()) {
            listModel.addElement(event);
        }
        
        // Clear selection (only if eventList is initialized)
        if (eventList != null) {
            eventList.clearSelection();
        }
        selectedEvent = null;
        
        // Update event details (only if eventDetailsArea is initialized)
        if (eventDetailsArea != null) {
            eventDetailsArea.setText("Select an event from the list to view details...");
        }
        
        // Update register button state (only if registerButton is initialized)
        if (registerButton != null) {
            registerButton.setEnabled(false);
            registerButton.setText("Register for Event");
        }
        
        // Show status message (only if eventDetailsArea is initialized)
        if (listModel.getSize() == 0 && eventDetailsArea != null) {
            eventDetailsArea.setText("No events available. Events will appear here once created by organizers.");
        }
    }

    private void registerForEvent() {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(null, "Please select an event first.", 
                "No Event Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();
        String type = (String) participantTypeCombo.getSelectedItem();
        String numRegStr = numberOfRegistrationsField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || numRegStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", 
                "Incomplete Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numberOfRegistrations;
        try {
            numberOfRegistrations = Integer.parseInt(numRegStr);
            if (numberOfRegistrations <= 0) {
                JOptionPane.showMessageDialog(null, "Number of registrations must be greater than 0.", 
                    "Invalid Number", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for registrations.", 
                "Invalid Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if event has enough available spots
        if (selectedEvent.getAvailableSpots() < numberOfRegistrations) {
            JOptionPane.showMessageDialog(null, 
                "Sorry, not enough spots available!\n\n" +
                "Event: " + selectedEvent.getName() + 
                "\nRequested: " + numberOfRegistrations + " registrations" +
                "\nAvailable: " + selectedEvent.getAvailableSpots() + " spots",
                "Insufficient Spots", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate total cost using fee calculator
        for (int i = 0; i < availableServices.size(); i++) {
            availableServices.get(i).setSelected(serviceCheckboxes.get(i).isSelected());
        }

        FeeBreakdown breakdown = feeCalculator.calculateFee(
            selectedEvent.getRegistrationFee(),
            numberOfRegistrations,
            availableServices,
            earlyBirdCheckbox.isSelected()
        );

        // Show confirmation dialog with fee breakdown
        String message = String.format(
            "Register for event: %s\n\nParticipant Details:\nID: %s\nName: %s\nType: %s\nNumber of Registrations: %d\n\n" +
            "Base Fee: $%.2f\nServices: $%.2f\nTotal Before Discount: $%.2f\nDiscount: -$%.2f\nNET AMOUNT: $%.2f\n\nAvailable Spots: %d",
            selectedEvent.getName(), id, name, type, numberOfRegistrations, 
            breakdown.getBaseAmount(), breakdown.getServicesTotal(), 
            breakdown.getTotalBeforeDiscount(), breakdown.getDiscountAmount(),
            breakdown.getNetAmount(), selectedEvent.getAvailableSpots());

        int result = JOptionPane.showConfirmDialog(null, message, 
            "Confirm Registration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // Attempt to register multiple participants
            boolean success = true;
            int registeredCount = 0;
            
            for (int i = 0; i < numberOfRegistrations; i++) {
                if (selectedEvent.registerParticipant(type)) {
                    registeredCount++;
                } else {
                    success = false;
                    break;
                }
            }

            if (success && registeredCount == numberOfRegistrations) {
                // Save the updated event data to file
                organizer.removeEvent(selectedEvent); // Remove old version
                organizer.addEvent(selectedEvent);    // Add updated version
                
                // Update the display
                updateEventDetails();
                
                JOptionPane.showMessageDialog(null, 
                    "Registration successful!\n\n" + 
                    registeredCount + " " + type.toLowerCase() + "(s) have been registered for " + selectedEvent.getName() + 
                    "\nRegistrant: " + name + " (ID: " + id + ")" +
                    "\nNet Amount Paid: $" + String.format("%.2f", breakdown.getNetAmount()) +
                    "\n\nRemaining spots: " + selectedEvent.getAvailableSpots(),
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
                
                clearForm();
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Registration partially failed!\n\nSuccessfully registered: " + registeredCount + 
                    "\nRequested: " + numberOfRegistrations + 
                    "\n\nThe event may have become full during registration.",
                    "Partial Registration", JOptionPane.WARNING_MESSAGE);
                
                // Update display to show current state
                updateEventDetails();
            }
        }
    }

    private void showFeeCalculation() {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(null, "Please select an event first.", 
                "No Event Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numRegStr = numberOfRegistrationsField.getText().trim();
        if (numRegStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the number of registrations.", 
                "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numberOfRegistrations;
        try {
            numberOfRegistrations = Integer.parseInt(numRegStr);
            if (numberOfRegistrations <= 0) {
                JOptionPane.showMessageDialog(null, "Number of registrations must be greater than 0.", 
                    "Invalid Number", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for registrations.", 
                "Invalid Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Set selected services
        for (int i = 0; i < availableServices.size(); i++) {
            availableServices.get(i).setSelected(serviceCheckboxes.get(i).isSelected());
        }

        // Calculate fee breakdown
        FeeBreakdown breakdown = feeCalculator.calculateFee(
            selectedEvent.getRegistrationFee(),
            numberOfRegistrations,
            availableServices,
            earlyBirdCheckbox.isSelected()
        );

        // Show detailed bill
        JTextArea billArea = new JTextArea(breakdown.getDetailedBill());
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scrollPane, 
            "Fee Calculation - " + selectedEvent.getName(), 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        studentIdField.setText("");
        studentNameField.setText("");
        participantTypeCombo.setSelectedIndex(0);
        numberOfRegistrationsField.setText("1");
        
        // Clear service checkboxes
        for (JCheckBox checkbox : serviceCheckboxes) {
            checkbox.setSelected(false);
        }
        
        // Clear discount checkbox
        earlyBirdCheckbox.setSelected(false);
        
        // Reset service selections
        for (AdditionalService service : availableServices) {
            service.setSelected(false);
        }
    }

    private void updateEventDetails() {
        if (selectedEvent != null) {
            StringBuilder details = new StringBuilder();
            details.append("=== EVENT DETAILS ===\n\n");
            details.append(selectedEvent.getDetailedInfo());
            details.append("\n\n=== REGISTRATION STATUS ===\n");
            details.append("Registration Fee: $").append(String.format("%.2f", selectedEvent.getRegistrationFee()));
            details.append("\nTotal Capacity: ").append(selectedEvent.getCapacity());
            details.append("\nRegistered: ").append(selectedEvent.getRegisteredCount());
            details.append("\nAvailable Spots: ").append(selectedEvent.getAvailableSpots());
            
            if (selectedEvent.hasAvailableSpots()) {
                details.append("\n\n✅ Registration Available");
                details.append("\nClick 'Register for Event' after filling the form to register.");
            } else {
                details.append("\n\n❌ Event is FULL");
                details.append("\nNo more registrations accepted.");
            }
            
            eventDetailsArea.setText(details.toString());
        } else {
            eventDetailsArea.setText("Select an event from the list to view details...");
        }
    }
}
