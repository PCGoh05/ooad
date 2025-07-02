// Observer Pattern - Interface for observing event changes
public interface EventObserver {
    void onEventAdded(Event event);
    void onEventUpdated(Event event);
    void onEventRemoved(Event event);
}
