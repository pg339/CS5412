package cornell.cs5412;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pablo on 3/2/2016.
 * Class to represent events in a way that's easily translatable into JSON
 */
public class Event implements IEvent, Parcelable {
    //TODO: Review constructors
    private String id;
    private String title;
    private String owner;
    private String description;
    private String startTime;
    private String location;
    private double latitude;
    private double longitude;
    private Integer minRsvps;
    private Integer maxRsvps;
    private EventStatus eventStatus;
    private List<String> rsvps;

    public static Parcelable.Creator<Event> CREATOR = new Creator();

    public Event() {
        String date = MiscHelpers.formatDateForNetwork(new Date());
        this.id = Profile.getCurrentProfile().getId() + ":" + date;
        this.owner = Profile.getCurrentProfile().getId();
        rsvps = new ArrayList<>();
        eventStatus = EventStatus.PENDING;
    }

    public Event(Parcel source) {
        id = source.readString();
        title = source.readString();
        owner = source.readString();
        description = source.readString();
        startTime = source.readString();
        location = source.readString();
        latitude = source.readDouble();
        longitude = source.readDouble();
        minRsvps = source.readInt();
        maxRsvps = source.readInt();
        eventStatus = (EventStatus) source.readSerializable();
        rsvps = new ArrayList<>();
        source.readStringList(rsvps);
    }

    public Event(String id, String title, String owner, String description,
                 String startTime, String location, double latitude, double longitude, int minRsvps,
                 int maxRsvps, EventStatus eventStatus, List<String> rsvps) {
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minRsvps = minRsvps;
        this.maxRsvps = maxRsvps;
        this.eventStatus = eventStatus;
        this.rsvps = rsvps;
    }

    //For testing previews
    public Event(String title, String owner, String description, String startTime, String location) {
        String date = MiscHelpers.formatDateForNetwork(new Date());
        this.id = Profile.getCurrentProfile().getId() + ":" + date;
        this.title = title;
        this.owner = owner;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
        this.latitude = 0;
        this.longitude = 0;
        this.minRsvps = 0;
        this.maxRsvps = 0;
        this.eventStatus = EventStatus.PENDING;
        this.rsvps = new ArrayList<>();
    }

    public Event(String title, String description, String location, double latitude,
                 double longitude, int minRsvps, int maxRsvps) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = formatter.format(new Date());
        this.id = Profile.getCurrentProfile().getId() + ":" + date;
        this.title = title;
        this.owner = Profile.getCurrentProfile().getId();
        this.description = description;
        this.startTime = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minRsvps = minRsvps;
        this.maxRsvps = maxRsvps;
        this.eventStatus = EventStatus.PENDING;
        this.rsvps = new ArrayList<>();
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
    public Integer getMinRsvps() {
        return minRsvps;
    }

    @Override
    public void setMinRsvps(Integer min) {
        minRsvps = min;
    }

    @Override
    public Integer getMaxRsvps() {
        return maxRsvps;
    }

    @Override
    public void setMaxRsvps(Integer max) {
        this.maxRsvps = max;
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
    public void updateEventStatus() {
        if (eventStatus != EventStatus.CANCELLED) {
            if (minRsvps == null) {
                eventStatus = EventStatus.HAPPENING;
            } else {
                eventStatus = rsvps.size() >= minRsvps ? EventStatus.HAPPENING : EventStatus.PENDING;
            }
        }
    }

    @Override
    public List<String> getRsvps() {
        return rsvps;
    }

    @Override
    public void setRsvps(List<String> rsvps) {
        this.rsvps = rsvps;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(owner);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(location);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(minRsvps);
        dest.writeInt(maxRsvps);
        dest.writeSerializable(eventStatus);
        dest.writeStringList(rsvps);
    }

    private static class Creator implements Parcelable.Creator<Event> {

        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    }
}
