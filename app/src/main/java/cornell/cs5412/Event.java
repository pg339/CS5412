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
public class Event implements IEvent{
    //TODO: Review constructors
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

    //For testing previews
    public Event(String title, String owner, String description, String startTime, String location) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = formatter.format(new Date());
        this.id = Profile.getCurrentProfile().getId() + ":" + date;
        this.title = title;
        this.owner = owner;
        this.description = description;
        this.category = "";
        this.startTime = startTime;
        this.location = location;
        this.latitude = 0;
        this.longitude = 0;
        this.minRsvps = 0;
        this.maxRsvps = 0;
        this.friends_only = false;
        this.eventStatus = EventStatus.PENDING;
        this.rsvps = null;
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

    @Override
    public String getEventId() {
        return id;
    }

    @Override
    public void setEventId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double lat) {
        latitude = lat;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    @Override
    public int getMinRsvps() {
        return minRsvps;
    }

    @Override
    public void setMinRsvps(int min) {
        minRsvps = min;
    }

    @Override
    public int getMaxRsvps() {
        return maxRsvps;
    }

    @Override
    public void setMaxRsvps(int max) {
        this.maxRsvps = max;
    }

    @Override
    public boolean isFriends_only() {
        return friends_only;
    }

    @Override
    public void setFriends_only(boolean v) {
        friends_only = v;
    }

    @Override
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    @Override
    public void setEventStatus(EventStatus status) {
        eventStatus = status;
    }

    @Override
    public List getRsvps() {
        return rsvps;
    }

    @Override
    public void setRsvps(List<String> rsvps) {
        this.rsvps = rsvps;
    }
}
