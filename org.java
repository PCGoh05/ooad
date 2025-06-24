import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class org {
    private String organizerID = "";
    private String organizerName = "";
    private ArrayList<Event> events = new ArrayList<>();

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public JPanel createOrganizerPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button
        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        panel.add(backButton, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Event Information"));

        // Declare input fields
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField eventName = new JTextField();
        JTextField eventPrice = new JTextField();
        JTextField eventDate = new JTextField();
        JTextField eventVenue = new JTextField();
        JTextField eventType = new JTextField();
        JTextField eventCapacity = new JTextField();

        // Add labels and fields to inputPanel
        inputPanel.add(new JLabel("Organizer ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Organizer Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Event Name:"));
        inputPanel.add(eventName);

        inputPanel.add(new JLabel("Event Price:"));
        inputPanel.add(eventPrice);

        inputPanel.add(new JLabel("Event Date:"));
        inputPanel.add(eventDate);

        inputPanel.add(new JLabel("Event Venue:"));
        inputPanel.add(eventVenue);

        inputPanel.add(new JLabel("Event Type:"));
        inputPanel.add(eventType);

        inputPanel.add(new JLabel("Event Capacity:"));
        inputPanel.add(eventCapacity);

        panel.add(inputPanel, BorderLayout.CENTER);

        // Buttons
        JButton createButton = new JButton("Create");
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");
        JButton showButton = new JButton("Show Events");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(showButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // CREATE Button Logic
        createButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            // Get event data
            String eventNameVal = eventName.getText().trim();
            String eventDateVal = eventDate.getText().trim();
            String eventVenueVal = eventVenue.getText().trim();
            String eventTypeVal = eventType.getText().trim();

            double eventPriceVal;
            int eventCapacityVal;

            try {
                eventPriceVal = Double.parseDouble(eventPrice.getText().trim());
                eventCapacityVal = Integer.parseInt(eventCapacity.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid number for price or capacity.");
                return;
            }

            if (id.isEmpty() || name.isEmpty() || eventNameVal.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all required fields.");
                return;
            }

            // Set organizer info
            setOrganizerID(id);
            setOrganizerName(name);

            // Create new event
            Event newEvent = new Event();
            newEvent.setName(eventNameVal);
            newEvent.setPrice(eventPriceVal);
            newEvent.setDate(eventDateVal);
            newEvent.setVenue(eventVenueVal);
            newEvent.setType(eventTypeVal);
            newEvent.setCapacity(eventCapacityVal);

            events.add(newEvent);

            JOptionPane.showMessageDialog(panel, "Event created successfully!");
        });

        // UPDATE Button Logic (for demo, it updates first event if any)
        updateButton.addActionListener(e -> {
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No events to update.");
                return;
            }

            Event eventToUpdate = events.get(0); // Update first for now

            eventToUpdate.setName(eventName.getText().trim());
            try {
                eventToUpdate.setPrice(Double.parseDouble(eventPrice.getText().trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid price.");
                return;
            }

            eventToUpdate.setDate(eventDate.getText().trim());
            eventToUpdate.setVenue(eventVenue.getText().trim());
            eventToUpdate.setType(eventType.getText().trim());
            try {
                eventToUpdate.setCapacity(Integer.parseInt(eventCapacity.getText().trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid capacity.");
                return;
            }

            JOptionPane.showMessageDialog(panel, "Event updated.");
        });

        // CANCEL Button Logic
        cancelButton.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
            eventName.setText("");
            eventPrice.setText("");
            eventDate.setText("");
            eventVenue.setText("");
            eventType.setText("");
            eventCapacity.setText("");
        });

        showButton.addActionListener(e -> {
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No events created yet.");
                return;
            }

            StringBuilder eventList = new StringBuilder("Events:\n");
            for (Event event : events) {
                eventList.append("Name: ").append(event.getName())
                        .append(", Price: ").append(event.getPrice())
                        .append(", Date: ").append(event.getDate())
                        .append(", Venue: ").append(event.getVenue())
                        .append(", Type: ").append(event.getType())
                        .append(", Capacity: ").append(event.getCapacity())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(panel, eventList.toString());
        });

        return panel;
    }
}
