package cornell.cs5412;

import java.util.List;

/**
 * User profile returned from server
 */
public class DisplayUser {
    String id;
    List<Event> rsvps;
    List<Event> events;

    public DisplayUser(String id, List<Event> rsvps, List<Event> events) {
        this.id = id;
        this.rsvps = rsvps;
        this.events = events;
    }
}
