import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class org {
    private String organizerID = "";
    private String organizerName = "";
    private ArrayList<Event> events = new ArrayList<>();
    private List<EventObserver> observers = new ArrayList<>(); // Observer list

    // Constructor - load events from file
    public org() {
        this.events = EventFileManager.loadEventsFromFile();
    }

    // Observer pattern methods
    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    private void notifyEventAdded(Event event) {
        for (EventObserver observer : observers) {
            observer.onEventAdded(event);
        }
    }

    private void notifyEventUpdated(Event event) {
        for (EventObserver observer : observers) {
            observer.onEventUpdated(event);
        }
    }

    private void notifyEventRemoved(Event event) {
        for (EventObserver observer : observers) {
            observer.onEventRemoved(event);
        }
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
        notifyEventAdded(event); // Notify observers
    }

    public void removeEvent(Event event) {
        events.remove(event);
        EventFileManager.saveEventsToFile(events, organizerID, organizerName); // Save to file when event is removed
        notifyEventRemoved(event); // Notify observers
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public JPanel createOrganizerPanel(CardLayout cardLayout, JPanel cardPanel) {
        OrganizerPanel panel = new OrganizerPanel(this);
        return panel.createOrganizerPanel(cardLayout, cardPanel);
    }
}
