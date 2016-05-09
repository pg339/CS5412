package cornell.cs5412;

import java.util.List;

public interface IEvent {
    String getEventId();
    void setEventId(String id);
    String getTitle();
    void setTitle(String title);
    String getOwner();
    void setOwner(String owner);
    String getDescription();
    void setDescription(String description);
    String getStartTime();
    void setStartTime(String startTime);
    String getLocation();
    void setLocation(String location);
    double getLatitude();
    void setLatitude(double lat);
    double getLongitude();
    void setLongitude(double lon);
    Integer getMinRsvps();
    void setMinRsvps(int min);
    Integer getMaxRsvps();
    void setMaxRsvps(int max);
    EventStatus getEventStatus();
    void setEventStatus(EventStatus status);
    List<String> getRsvps();
    void setRsvps(List<String> rsvps);
}
