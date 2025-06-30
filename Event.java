import java.util.ArrayList;

public class Event {
    private String name, date, venue, type;
    private double registrationFee;
    private int capacity;
    private int eventId;
    private static int nextId = 1;
    private int studentCount = 0; 
    private int staffCount = 0;
    private int registeredCount = 0; // Total registered participants
    
    // Event types enumeration
    public static final String[] EVENT_TYPES = {
        "Seminar", "Workshop", "Cultural Event", "Sports Event"
    };
    
    // Constructor
    public Event() {
        this.eventId = nextId++;
    }
    
    public Event(String name, String date, String venue, String type, double registrationFee, int capacity) {
        this.eventId = nextId++;
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.type = type;
        this.registrationFee = registrationFee;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getEventId() { return eventId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getRegistrationFee() { return registrationFee; }
    public void setRegistrationFee(double registrationFee) { this.registrationFee = registrationFee; }
    
    // Keep old method for backward compatibility
    public double getPrice() { return registrationFee; }
    public void setPrice(double price) { this.registrationFee = price; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
    
    public int getStaffCount() { return staffCount; }
    public void setStaffCount(int staffCount) { this.staffCount = staffCount; }
    
    public int getRegisteredCount() { return registeredCount; }
    public void setRegisteredCount(int registeredCount) { this.registeredCount = registeredCount; }
    
    // Method to register a participant (returns true if successful, false if full)
    public boolean registerParticipant(String participantType) {
        if (registeredCount >= capacity) {
            return false; // Event is full
        }
        
        registeredCount++;
        if ("Student".equalsIgnoreCase(participantType)) {
            studentCount++;
        } else if ("Staff".equalsIgnoreCase(participantType)) {
            staffCount++;
        }
        return true;
    }
    
    // Method to check if event has available spots
    public boolean hasAvailableSpots() {
        return registeredCount < capacity;
    }
    
    // Method to get available spots
    public int getAvailableSpots() {
        return capacity - registeredCount;
    }
    
    @Override
    public String toString() {
        return String.format("#%d - %s (%s)", eventId, name, type);
    }
    
    public String getDetailedInfo() {
        return String.format("Event ID: %d\nName: %s\nDate: %s\nVenue: %s\nType: %s\nRegistration Fee: $%.2f\nCapacity: %d\nRegistered: %d\nAvailable Spots: %d\nStudents: %d\nStaff: %d", 
            eventId, name, date, venue, type, registrationFee, capacity, registeredCount, getAvailableSpots(), studentCount, staffCount);
    }
}
