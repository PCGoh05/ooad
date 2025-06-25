import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class org {
    private String organizerID = "";
    private String organizerName = "";

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

        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Event Information"));

        JTextField idField = new JTextField();
        idField.setToolTipText("e.g. O001 (String)");
        JTextField nameField = new JTextField();
        nameField.setToolTipText("e.g. John Doe (String)");
        JTextField eventName = new JTextField();
        eventName.setToolTipText("e.g. Seminar 2025 (String)");
        JTextField eventPrice = new JTextField();
        eventPrice.setToolTipText("e.g. 100.0 (Double)");
        JTextField eventDate = new JTextField();
        eventDate.setToolTipText("e.g. 2025-07-01 (String, yyyy-MM-dd)");
        JTextField eventVenue = new JTextField();
        eventVenue.setToolTipText("e.g. Hall A (String)");
        JTextField eventType = new JTextField();
        eventType.setToolTipText("e.g. Seminar (String)");
        JTextField eventCapacity = new JTextField();
        eventCapacity.setToolTipText("e.g. 100 (Integer)");

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

        JButton createButton = new JButton("Create");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton showButton = new JButton("Show Events");

        JComboBox<String> eventSelector = new JComboBox<>();
        refreshEventSelector(eventSelector);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10));
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(showButton);
        buttonPanel.add(new JLabel("Select Event:"));
        buttonPanel.add(eventSelector);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
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
            setOrganizerID(id);
            setOrganizerName(name);
            Organizer organizer = new Organizer(id, name);
            Event newEvent = new EventBuilder()
                    .setName(eventNameVal)
                    .setPrice(eventPriceVal)
                    .setDate(eventDateVal)
                    .setVenue(eventVenueVal)
                    .setType(eventTypeVal)
                    .setCapacity(eventCapacityVal)
                    .setOrganizer(organizer)
                    .build();
            EventManager.getInstance().addEvent(newEvent);
            refreshEventSelector(eventSelector);
            JOptionPane.showMessageDialog(panel, "Event created successfully!");
        });

        updateButton.addActionListener(e -> {
            int idx = eventSelector.getSelectedIndex();
            List<Event> events = EventManager.getInstance().getEvents();
            if (idx < 0 || idx >= events.size()) {
                JOptionPane.showMessageDialog(panel, "Please select an event to update.");
                return;
            }
            Event eventToUpdate = events.get(idx);
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
            refreshEventSelector(eventSelector);
            JOptionPane.showMessageDialog(panel, "Event updated.");
        });

        deleteButton.addActionListener(e -> {
            int idx = eventSelector.getSelectedIndex();
            List<Event> events = EventManager.getInstance().getEvents();
            if (idx < 0 || idx >= events.size()) {
                JOptionPane.showMessageDialog(panel, "Please select an event to delete.");
                return;
            }
            EventManager.getInstance().removeEvent(events.get(idx));
            refreshEventSelector(eventSelector);
            JOptionPane.showMessageDialog(panel, "Event deleted.");
        });

        showButton.addActionListener(e -> {
            List<Event> events = EventManager.getInstance().getEvents();
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No events created yet.");
                return;
            }
            StringBuilder eventList = new StringBuilder("Events:\n");
            for (Event event : events) {
                Organizer org = event.getOrganizer();
                eventList.append("Organizer ID: ").append(org != null ? org.getId() : "").append(", ");
                eventList.append("Organizer Name: ").append(org != null ? org.getName() : "").append(", ");
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

        clearButton.setToolTipText("Clear all input fields");
        clearButton.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
            eventName.setText("");
            eventPrice.setText("");
            eventDate.setText("");
            eventVenue.setText("");
            eventType.setText("");
            eventCapacity.setText("");
            eventSelector.setSelectedIndex(-1);
        });

        eventSelector.addActionListener(e -> {
            int idx = eventSelector.getSelectedIndex();
            List<Event> events = EventManager.getInstance().getEvents();
            if (idx >= 0 && idx < events.size()) {
                Event selected = events.get(idx);
                eventName.setText(selected.getName());
                eventPrice.setText(String.valueOf(selected.getPrice()));
                eventDate.setText(selected.getDate());
                eventVenue.setText(selected.getVenue());
                eventType.setText(selected.getType());
                eventCapacity.setText(String.valueOf(selected.getCapacity()));
            }
        });

        return panel;
    }

    private void refreshEventSelector(JComboBox<String> eventSelector) {
        eventSelector.removeAllItems();
        List<Event> events = EventManager.getInstance().getEvents();
        for (Event event : events) {
            eventSelector.addItem(event.getName());
        }
    }
}
