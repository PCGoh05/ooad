// StudentPanel.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class student extends User{
   private int registerFees;
   private JComboBox<String> eventSelector;
   private java.util.List<Event> events;

    public student(String id, String name) {
        super(id, name);
        
    }
    public student() {
        super("", ""); // Default constructor
    }
    
    class EarlyBirdDiscountStrategy implements DiscountStrategy {
        @Override
        public double getDiscount(Event event, double amount) {
            // Example: 10% discount if event date is at least 7 days from today
            try {
                LocalDate eventDate = LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (eventDate.isAfter(LocalDate.now().plusDays(6))) {
                    return amount * 0.10;
                }
            } catch (Exception e) {
                // Ignore parse errors, no discount
            }
            return 0.0;
        }
    }

    private void refreshEventSelector() {
        eventSelector.removeAllItems();
        events = EventManager.getInstance().getEvents();
        for (Event event : events) {
            eventSelector.addItem(event.getName());
        }
    }

    public JPanel createStudentStaffPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Student & Staff Panel", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(20));

        eventSelector = new JComboBox<>();
        refreshEventSelector();
        contentPanel.add(new JLabel("Select Event:"));
        contentPanel.add(eventSelector);

        JCheckBox cateringBox = new JCheckBox("Catering (+$10)");
        JCheckBox transportBox = new JCheckBox("Transportation (+$5)");
        contentPanel.add(cateringBox);
        contentPanel.add(transportBox);

        JButton registerButton = new JButton("Register");
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(registerButton);

        JTextArea billArea = new JTextArea(10, 40);
        billArea.setEditable(false);
        JScrollPane billScroll = new JScrollPane(billArea);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(billScroll);

        // Refresh eventSelector every time the panel is shown
        panel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0 && panel.isShowing()) {
                refreshEventSelector();
            }
        });

        registerButton.addActionListener(e -> {
            int idx = eventSelector.getSelectedIndex();
            if (idx < 0 || idx >= events.size()) {
                JOptionPane.showMessageDialog(panel, "Please select an event.");
                return;
            }
            Event selectedEvent = events.get(idx);
            boolean catering = cateringBox.isSelected();
            boolean transport = transportBox.isSelected();
            FeeCalculator calculator = new FeeCalculator(new EarlyBirdDiscountStrategy());
            double total = calculator.calculateTotal(selectedEvent, catering, transport);
            double baseFee = selectedEvent.getPrice();
            double cateringFee = catering ? 10.0 : 0.0;
            double transportFee = transport ? 5.0 : 0.0;
            double beforeDiscount = baseFee + cateringFee + transportFee;
            double discount = beforeDiscount - total;
            User user = new student("S001", "Demo Student");
            Registration reg = new Registration(user, selectedEvent, catering, transport, total);
            RegistrationManager.getInstance().addRegistration(reg);
            StringBuilder bill = new StringBuilder();
            bill.append("Event: ").append(selectedEvent.getName()).append("\n");
            bill.append("Base Fee: $").append(baseFee).append("\n");
            bill.append("Catering: $").append(cateringFee).append("\n");
            bill.append("Transportation: $").append(transportFee).append("\n");
            bill.append("Total Before Discount: $").append(beforeDiscount).append("\n");
            bill.append("Discount: -$").append(discount).append("\n");
            bill.append("Net Payable: $").append(total).append("\n");
            bill.append("\nRegistration recorded. Total registrations: ")
                .append(RegistrationManager.getInstance().getRegistrations().size());
            billArea.setText(bill.toString());
            JOptionPane.showMessageDialog(panel, "Registration successful!\nPlease see your bill below.");
        });

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    
}
