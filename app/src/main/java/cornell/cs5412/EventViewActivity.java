package cornell.cs5412;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class EventViewActivity extends Activity {

    public static final String EVENT_EXTRA = "cornell.cs5412.EVENT_EXTRA";

    public IEvent event;
    private String user;
    private RelativeLayout bottom;
    private Button countButton;
    private TextView label;
    private Button cancel;
    private Switch attendingSwitch;
    private TextView titleView;
    private TextView descriptionView;
    private TextView creatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        Intent intent = getIntent();
        event = intent.getParcelableExtra(EVENT_EXTRA);
        user = Profile.getCurrentProfile().getId();

        titleView = (TextView) findViewById(R.id.event_view_title);
        descriptionView = (TextView) findViewById(R.id.event_view_description);
        descriptionView.setMovementMethod(new ScrollingMovementMethod());
        creatorView = (TextView) findViewById(R.id.event_view_creator);

        RelativeLayout parent = (RelativeLayout) findViewById(R.id.event_view_parent_layout);

        if (event.getOwner().equals(user)) {
            bottom = (RelativeLayout) View.inflate(this, R.layout.event_view_bottom_owner, parent);
            countButton = (Button) findViewById(R.id.event_view_owner_rsvp_count_button);
            label = (TextView) findViewById(R.id.event_view_owner_rsvp_label);
            cancel = (Button) findViewById(R.id.event_view_cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //TODO: Cancel event
                }
            });
        } else {
            bottom = (RelativeLayout) View.inflate(this, R.layout.event_view_bottom_standard, parent);
            countButton = (Button) findViewById(R.id.event_view_standard_rsvp_count_button);
            label = (TextView) findViewById(R.id.event_view_standard_rsvp_label);
            attendingSwitch = (Switch) findViewById(R.id.event_view_attendance_switch);
            attendingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //TODO: Update rsvp list and send update
                }
            });
            if (event.getRsvps().contains(user)) {
                attendingSwitch.setChecked(true);
            } else {
                attendingSwitch.setChecked(false);
            }
        }

        countButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO: Display fragment with rsvp list
            }
        });

        titleView.setText(event.getTitle());
        descriptionView.setText(event.getDescription());
        creatorView.setText("Hosted by "+event.getOwner());
        String rsvpCount = ""+event.getRsvps().size();
        if (event.getMaxRsvps() != null) {
            rsvpCount += "/"+event.getMaxRsvps();
        }
        countButton.setText(rsvpCount);
        String rsvpLabel = "Currently attending.";
        if (event.getMinRsvps() == null || event.getMinRsvps() <= event.getRsvps().size()) {
            rsvpLabel += "\nThe event is on!";
        } else {
            rsvpLabel += "\n"+(event.getRsvps().size() - event.getMinRsvps())+"more needed.";
        }
        label.setText(rsvpLabel);
    }


}
