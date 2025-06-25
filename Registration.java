public class Registration {
    private User user;
    private Event event;
    private boolean catering;
    private boolean transportation;
    private double totalFee;

    public Registration(User user, Event event, boolean catering, boolean transportation, double totalFee) {
        this.user = user;
        this.event = event;
        this.catering = catering;
        this.transportation = transportation;
        this.totalFee = totalFee;
    }

    public User getUser() { return user; }
    public Event getEvent() { return event; }
    public boolean hasCatering() { return catering; }
    public boolean hasTransportation() { return transportation; }
    public double getTotalFee() { return totalFee; }
}
