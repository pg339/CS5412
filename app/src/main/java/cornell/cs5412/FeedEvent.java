package cornell.cs5412;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class FeedEvent implements IEvent{
    public IEvent event;
    public TextView titleBox;
    public TextView dtlBox;
    public TextView previewBox;
    public TextView rsvpBox;
    public TextView rsvpCountBox;
    public TextView creatorBox;

    public FeedEvent(Context context, IEvent event) {
        //TODO: Truncate text to good size
        this.event = event;
        titleBox = new TextView(context);
        titleBox.setId(View.generateViewId());
        titleBox.setText(event.getTitle());
        dtlBox = new TextView(context);
        dtlBox.setId(View.generateViewId());
        dtlBox.setText(event.getStartTime() + "\n" + event.getLocation());
        dtlBox.setGravity(Gravity.BOTTOM);
        previewBox = new TextView(context);
        previewBox.setId(View.generateViewId());
        previewBox.setText(event.getDescription());
        rsvpBox = new TextView(context);
        rsvpBox.setId(View.generateViewId());
        rsvpBox.setText("Attending:");
        rsvpCountBox = new TextView(context);
        rsvpCountBox.setId(View.generateViewId());
        rsvpCountBox.setText(event.getRsvps().toString());
        creatorBox = new TextView(context);
        creatorBox.setId(View.generateViewId());
        creatorBox.setText("By " + event.getOwner());
        creatorBox.setGravity(Gravity.BOTTOM);

        RelativeLayout.LayoutParams rsvpParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rsvpParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rsvpParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rsvpBox.setLayoutParams(rsvpParams);

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleParams.addRule(RelativeLayout.ALIGN_LEFT, rsvpBox.getId());
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleBox.setLayoutParams(titleParams);

        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        previewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        previewParams.addRule(RelativeLayout.ALIGN_LEFT, rsvpBox.getId());
        previewParams.addRule(RelativeLayout.BELOW, titleBox.getId());
        previewBox.setLayoutParams(previewParams);

        RelativeLayout.LayoutParams countParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        countParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        countParams.addRule(RelativeLayout.BELOW, rsvpBox.getId());
        rsvpCountBox.setLayoutParams(countParams);

        RelativeLayout.LayoutParams creatorParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        creatorParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        creatorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        creatorParams.addRule(RelativeLayout.BELOW, rsvpBox.getId());
        creatorBox.setLayoutParams(creatorParams);

        RelativeLayout.LayoutParams dtlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        dtlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        dtlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dtlParams.addRule(RelativeLayout.BELOW, previewBox.getId());
        titleParams.addRule(RelativeLayout.ALIGN_LEFT, creatorBox.getId());
        dtlBox.setLayoutParams(dtlParams);
    }

    @Override
    public String getEventId() {
        return event.getEventId();
    }

    @Override
    public void setEventId(String id) {
        event.setEventId(id);
    }

    @Override
    public String getTitle() {
        return event.getTitle();
    }

    @Override
    public void setTitle(String title) {
        event.setTitle(title);
    }

    @Override
    public String getOwner() {
        return event.getOwner();
    }

    @Override
    public void setOwner(String owner) {
        event.setOwner(owner);
    }

    @Override
    public String getDescription() {
        return event.getDescription();
    }

    @Override
    public void setDescription(String description) {
        event.setDescription(description);
    }

    @Override
    public String getCategory() {
        return event.getCategory();
    }

    @Override
    public void setCategory(String category) {
        event.setCategory(category);
    }

    @Override
    public String getStartTime() {
        return event.getStartTime();
    }

    @Override
    public void setStartTime(String startTime) {
        event.setStartTime(startTime);
    }

    @Override
    public String getLocation() {
        return event.getLocation();
    }

    @Override
    public void setLocation(String location) {
        event.setLocation(location);
    }

    @Override
    public double getLatitude() {
        return event.getLatitude();
    }

    @Override
    public void setLatitude(double lat) {
        event.setLatitude(lat);
    }

    @Override
    public double getLongitude() {
        return event.getLongitude();
    }

    @Override
    public void setLongitude(double lon) {
        event.setLongitude(lon);
    }

    @Override
    public int getMinRsvps() {
        return event.getMinRsvps();
    }

    @Override
    public void setMinRsvps(int min) {
        event.setMinRsvps(min);
    }

    @Override
    public int getMaxRsvps() {
        return event.getMaxRsvps();
    }

    @Override
    public void setMaxRsvps(int max) {
        event.setMaxRsvps(max);
    }

    @Override
    public boolean isFriends_only() {
        return event.isFriends_only();
    }

    @Override
    public void setFriends_only(boolean v) {
        event.setFriends_only(v);
    }

    @Override
    public EventStatus getEventStatus() {
        return event.getEventStatus();
    }

    @Override
    public void setEventStatus(EventStatus status) {
        event.setEventStatus(status);
    }

    @Override
    public List getRsvps() {
        return event.getRsvps();
    }

    @Override
    public void setRsvps(List<String> rsvps) {
        event.setRsvps(rsvps);
    }
}
