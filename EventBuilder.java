public class EventBuilder {
    private Event event;

    public EventBuilder() {
        event = new Event();
    }

    public EventBuilder setName(String name) {
        event.setName(name);
        return this;
    }

    public EventBuilder setDate(String date) {
        event.setDate(date);
        return this;
    }

    public EventBuilder setVenue(String venue) {
        event.setVenue(venue);
        return this;
    }

    public EventBuilder setType(String type) {
        event.setType(type);
        return this;
    }

    public EventBuilder setPrice(double price) {
        event.setPrice(price);
        return this;
    }

    public EventBuilder setCapacity(int capacity) {
        event.setCapacity(capacity);
        return this;
    }

    public EventBuilder setOrganizer(Organizer organizer) {
        event.setOrganizer(organizer);
        return this;
    }

    public Event build() {
        return event;
    }
}
