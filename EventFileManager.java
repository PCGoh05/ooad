import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventFileManager {
    private static final String EVENTS_FILE = "events.txt";
    
    // Save events to file
    public static void saveEventsToFile(ArrayList<Event> events, String organizerID, String organizerName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EVENTS_FILE))) {
            for (Event event : events) {
                // Format: EventID|Name|Date|Venue|Type|Fee|Capacity|OrganizerID|OrganizerName|RegisteredCount
                writer.println(String.format("%d|%s|%s|%s|%s|%.2f|%d|%s|%s|%d", 
                    event.getEventId(),
                    event.getName(),
                    event.getDate(),
                    event.getVenue(), 
                    event.getType(),
                    event.getRegistrationFee(),
                    event.getCapacity(),
                    organizerID != null ? organizerID : "",
                    organizerName != null ? organizerName : "",
                    event.getRegisteredCount()));
            }
            System.out.println("Events saved to " + EVENTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
        }
    }
    
    // Load events from file
    public static ArrayList<Event> loadEventsFromFile() {
        ArrayList<Event> events = new ArrayList<>();
        File file = new File(EVENTS_FILE);
        
        if (!file.exists()) {
            System.out.println("Events file doesn't exist yet. Starting with empty list.");
            return events;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    try {
                        // Parse event data
                        String name = parts[1];
                        String date = parts[2];
                        String venue = parts[3];
                        String type = parts[4];
                        double fee = Double.parseDouble(parts[5]);
                        int capacity = Integer.parseInt(parts[6]);
                        
                        Event event = new Event(name, date, venue, type, fee, capacity);
                        
                        // Load additional data if available
                        if (parts.length >= 10) {
                            int registeredCount = Integer.parseInt(parts[9]);
                            event.setRegisteredCount(registeredCount);
                        }
                        
                        events.add(event);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing event data: " + line);
                    }
                }
            }
            System.out.println("Loaded " + events.size() + " events from " + EVENTS_FILE);
        } catch (IOException e) {
            System.err.println("Error loading events from file: " + e.getMessage());
        }
        
        return events;
    }
    
    // Overload for backward compatibility
    public static void saveEventsToFile(ArrayList<Event> events) {
        saveEventsToFile(events, "", "");
    }
    
    // Append a single event to file
    public static void appendEventToFile(Event event, String organizerID, String organizerName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EVENTS_FILE, true))) {
            writer.println(String.format("%d|%s|%s|%s|%s|%.2f|%d|%s|%s|%d", 
                event.getEventId(),
                event.getName(),
                event.getDate(),
                event.getVenue(),
                event.getType(),
                event.getRegistrationFee(),
                event.getCapacity(),
                organizerID != null ? organizerID : "",
                organizerName != null ? organizerName : "",
                event.getRegisteredCount()));
            System.out.println("Event '" + event.getName() + "' saved to file.");
        } catch (IOException e) {
            System.err.println("Error appending event to file: " + e.getMessage());
        }
    }
    
    // Overload for backward compatibility
    public static void appendEventToFile(Event event) {
        appendEventToFile(event, "", "");
    }
}
