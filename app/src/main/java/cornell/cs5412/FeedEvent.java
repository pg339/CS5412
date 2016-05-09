package cornell.cs5412;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
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
        titleBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.large_display_text_size));
        titleBox.setTextColor(Color.BLACK);
        titleBox.setSingleLine(true);
        titleBox.setEllipsize(TextUtils.TruncateAt.END);
        titleBox.setPadding(0, 0, MiscHelpers.pixToDp(context, 10), 0);
        dtlBox = new TextView(context);
        dtlBox.setId(View.generateViewId());
        dtlBox.setText(event.getStartTime() + "\n" + event.getLocation());
        dtlBox.setGravity(Gravity.BOTTOM);
        dtlBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.display_text_size));
        dtlBox.setTextColor(Color.BLACK);
        dtlBox.setMaxLines(2);
        dtlBox.setEllipsize(TextUtils.TruncateAt.END);
        previewBox = new TextView(context);
        previewBox.setId(View.generateViewId());
        previewBox.setText(event.getDescription());
        previewBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.small_display_text_size));
        previewBox.setTextColor(Color.GRAY);
        previewBox.setMaxLines(3);
        previewBox.setEllipsize(TextUtils.TruncateAt.END);
        previewBox.setPadding(0, 0, MiscHelpers.pixToDp(context, 10), 0);
        rsvpBox = new TextView(context);
        rsvpBox.setId(View.generateViewId());
        rsvpBox.setText("Attending:");
        rsvpBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.display_text_size));
        rsvpBox.setTextColor(Color.BLACK);
        rsvpCountBox = new TextView(context);
        rsvpCountBox.setId(View.generateViewId());
        rsvpCountBox.setText("" + event.getRsvps().size());
        rsvpCountBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.xxxlarge_display_text_size));
        rsvpCountBox.setTextColor(Color.BLACK);
        rsvpCountBox.setGravity(Gravity.CENTER_HORIZONTAL);
        rsvpCountBox.setPadding(0, MiscHelpers.pixToDp(context, 10), 0, 0);
        creatorBox = new TextView(context);
        creatorBox.setId(View.generateViewId());
        creatorBox.setText("By " + FacebookUtil.getFacebookProfileField(event.getOwner(), "name"));
        creatorBox.setGravity(Gravity.BOTTOM);
        creatorBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.display_text_size));
        creatorBox.setTextColor(Color.BLACK);
        creatorBox.setSingleLine(true);
        creatorBox.setEllipsize(TextUtils.TruncateAt.END);
        creatorBox.setPadding(0, 0, MiscHelpers.pixToDp(context, 10), 0);

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
        titleParams.addRule(RelativeLayout.LEFT_OF, rsvpBox.getId());
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleBox.setLayoutParams(titleParams);

        RelativeLayout.LayoutParams creatorParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        creatorParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        creatorParams.addRule(RelativeLayout.BELOW, titleBox.getId());
        creatorParams.addRule(RelativeLayout.LEFT_OF, rsvpBox.getId());
        creatorBox.setLayoutParams(creatorParams);

        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        previewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        previewParams.addRule(RelativeLayout.LEFT_OF, rsvpBox.getId());
        previewParams.addRule(RelativeLayout.BELOW, creatorBox.getId());
        previewBox.setLayoutParams(previewParams);

        RelativeLayout.LayoutParams countParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        countParams.addRule(RelativeLayout.ALIGN_LEFT, rsvpBox.getId());
        countParams.addRule(RelativeLayout.ALIGN_RIGHT, rsvpBox.getId());
        countParams.addRule(RelativeLayout.BELOW, rsvpBox.getId());
        rsvpCountBox.setLayoutParams(countParams);

        RelativeLayout.LayoutParams dtlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        dtlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        dtlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dtlParams.addRule(RelativeLayout.BELOW, previewBox.getId());
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
        titleBox.setText(title);
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
        previewBox.setText(description);
    }

    @Override
    public String getStartTime() {
        return event.getStartTime();
    }

    @Override
    public void setStartTime(String startTime) {
        event.setStartTime(startTime);
        dtlBox.setText(startTime+"\n"+event.getLocation());
    }

    @Override
    public String getLocation() {
        return event.getLocation();
    }

    @Override
    public void setLocation(String location) {
        event.setLocation(location);
        dtlBox.setText(event.getStartTime()+"\n"+location);
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
    public Integer getMinRsvps() {
        return event.getMinRsvps();
    }

    @Override
    public void setMinRsvps(int min) {
        event.setMinRsvps(min);
    }

    @Override
    public Integer getMaxRsvps() {
        return event.getMaxRsvps();
    }

    @Override
    public void setMaxRsvps(int max) {
        event.setMaxRsvps(max);
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
    public List<String> getRsvps() {
        return event.getRsvps();
    }

    @Override
    public void setRsvps(List<String> rsvps) {
        event.setRsvps(rsvps);
        rsvpCountBox.setText(""+rsvps.size());
    }
}
