import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class org {
    private String organizerID = "";
    private String organizerName = "";
    private ArrayList<Event> events = new ArrayList<>();

    // Constructor - load events from file
    public org() {
        this.events = EventFileManager.loadEventsFromFile();
    }

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

    public void addEvent(Event event) {
        events.add(event);
        EventFileManager.saveEventsToFile(events, organizerID, organizerName); // Save to file when event is added
    }

    public void removeEvent(Event event) {
        events.remove(event);
        EventFileManager.saveEventsToFile(events, organizerID, organizerName); // Save to file when event is removed
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public JPanel createOrganizerPanel(CardLayout cardLayout, JPanel cardPanel) {
        OrganizerPanel panel = new OrganizerPanel(this);
        return panel.createOrganizerPanel(cardLayout, cardPanel);
    }
}
