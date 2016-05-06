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
    String getCategory();
    void setCategory(String category);
    String getStartTime();
    void setStartTime(String startTime);
    String getLocation();
    void setLocation(String location);
    double getLatitude();
    void setLatitude(double lat);
    double getLongitude();
    void setLongitude(double lon);
    int getMinRsvps();
    void setMinRsvps(int min);
    int getMaxRsvps();
    void setMaxRsvps(int max);
    boolean isFriends_only();
    void setFriends_only(boolean v);
    EventStatus getEventStatus();
    void setEventStatus(EventStatus status);
    List getRsvps();
    void setRsvps(List<String> rsvps);
}
