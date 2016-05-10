package cornell.cs5412;

import java.util.List;

/**
 * Represents a Feed, which is simply an array of events plus fields returned by server
 * Created by Pablo on 3/7/2016.
 */
public class Feed {
    public Event[] events;
    public String id;
    public List<String> rsvps;

    public Feed(Event[] events) {
        this.events = events;
    }
}
