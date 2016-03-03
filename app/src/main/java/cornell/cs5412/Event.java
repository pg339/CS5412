package cornell.cs5412;

import java.util.List;

/**
 * Created by Pablo on 3/2/2016.
 * Class to represent events in a way that's easily translatable into JSON
 */
public class Event {
    public String event_id;
    public String title;
    public String description;
    public String category;
    public String start_time;
    public String location;
    public String creator_id;
    public List rsvps;
    public float[] coordinates;
    public int min_rsvps;
    public int max_rsvps;
    public boolean open;

    public Event(String event_id, String title, String description, String category,
                 String start_time, String location, String creator_id, List rsvps,
                 float[] coordinates, int min_rsvps, int max_rsvps, boolean open) {
        this.event_id = event_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.start_time = start_time;
        this.location = location;
        this.creator_id = creator_id;
        this.rsvps = rsvps;
        this.coordinates = coordinates;
        this.min_rsvps = min_rsvps;
        this.max_rsvps = max_rsvps;
        this.open = open;
    }
}
