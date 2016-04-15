package cornell.cs5412;

import com.facebook.Profile;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Pablo on 3/2/2016.
 * Class to represent events in a way that's easily translatable into JSON
 */
public class Event {
    private String id;
    private String title;
    private String owner;
    private String description;
    private String category;
    private String startTime;
    private String location;
    private double latitude;
    private double longitude;
    private int minRsvps;
    private int maxRsvps;
    private boolean friends_only;
    private EventStatus eventStatus;
    private List<String> rsvps;

    public Event(String id, String title, String owner, String description, String category,
                 String startTime, String location, double latitude, double longitude, int minRsvps,
                 int maxRsvps, boolean friends_only, EventStatus eventStatus, List<String> rsvps) {
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.description = description;
        this.category = category;
        this.startTime = startTime;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minRsvps = minRsvps;
        this.maxRsvps = maxRsvps;
        this.friends_only = friends_only;
        this.eventStatus = eventStatus;
        this.rsvps = rsvps;
    }

    public Event(String title, String description, String category, String location, double latitude,
                 double longitude, int minRsvps, int maxRsvps) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = formatter.format(new Date());
        this.id = Profile.getCurrentProfile().getId() + ":" + date;
        this.title = title;
        this.owner = Profile.getCurrentProfile().getId();
        this.description = description;
        this.category = category;
        this.startTime = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minRsvps = minRsvps;
        this.maxRsvps = maxRsvps;
        this.friends_only = false;
        this.eventStatus = EventStatus.PENDING;
        this.rsvps = null;
    }
}
