package cornell.cs5412;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.io.IOException;

public class EventViewActivity extends AppCompatActivity {

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
    private EventViewActivity activity;

    private BroadcastReceiver logoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_view_toolbar);
        setActionBar(toolbar);

        Intent intent = getIntent();
        event = intent.getParcelableExtra(EVENT_EXTRA);
        user = Profile.getCurrentProfile().getId();

        titleView = (TextView) findViewById(R.id.event_view_title);
        descriptionView = (TextView) findViewById(R.id.event_view_description);
        descriptionView.setMovementMethod(new ScrollingMovementMethod());
        creatorView = (TextView) findViewById(R.id.event_view_creator);

        bottom = (RelativeLayout) findViewById(R.id.event_view_bottom);
        label = (TextView) findViewById(R.id.event_view_rsvp_label);
        countButton = (Button) findViewById(R.id.event_view_rsvp_count_button);

        if (event.getOwner().equals(user)) {
            RelativeLayout cancelLayout = (RelativeLayout) View.inflate(this, R.layout.cancel_event_button, bottom);
            cancel = (Button) findViewById(R.id.event_view_cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new CancelEventTask(activity).execute(event.getEventId(), user);
                }
            });
        } else {
            RelativeLayout switchComponents = (RelativeLayout) View.inflate(this, R.layout.event_view_bottom_switch, bottom);
            attendingSwitch = (Switch) findViewById(R.id.event_view_attendance_switch);
            TextView goingLabel = (TextView) findViewById(R.id.event_view_attendance_switch_label);
            attendingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !event.getRsvps().contains(user)) {
                        new RsvpTask(activity, isChecked).execute(event.getEventId(), user);
                    } else if (!isChecked && event.getRsvps().contains(user)) {
                        new RsvpTask(activity, isChecked).execute(event.getEventId(), user);
                    }
                }
            });
            if (event.getRsvps().contains(user)) {
                attendingSwitch.setChecked(true);
            } else {
                if (event.getMaxRsvps() <= event.getRsvps().size()) {
                    attendingSwitch.setClickable(false);
                }
                attendingSwitch.setChecked(false);
            }
        }

        countButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (event.getRsvps().size() == 0) {
                    return;
                }
                String attendees = "";
                for (int i=0; i<event.getRsvps().size(); i++) {
                    attendees += FacebookUtil.getFacebookProfileField(event.getRsvps().get(i), "name")+"\n";
                }
                new AlertDialog.Builder(activity)
                        .setMessage(attendees.substring(0, attendees.length()-1))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        titleView.setText(event.getTitle());
        descriptionView.setText(event.getDescription());
        creatorView.setText("Hosted by "+FacebookUtil.getFacebookProfileField(event.getOwner(), "name"));
        updateButtonCount();
        String rsvpLabel = "Currently attending.";
        if (event.getMinRsvps() == null || event.getMinRsvps() <= event.getRsvps().size()) {
            rsvpLabel += "\nThe event is on!";
        } else {
            rsvpLabel += "\n"+(event.getMinRsvps() - event.getRsvps().size())+" more needed.";
        }
        label.setText(rsvpLabel);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginActivity.LOGOUT_ACTION);
        logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(logoutReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutReceiver);
    }

    public void updateButtonCount() {
        String rsvpCount = ""+event.getRsvps().size();
        if (event.getMaxRsvps() != null) {
            rsvpCount += "/"+event.getMaxRsvps();
        }
        countButton.setText(rsvpCount);
    }

    private class RsvpTask extends AsyncTask<String, Void, String> {

        private Activity activity;
        private boolean attending;

        public RsvpTask(Activity activity, boolean attending) {
            this.activity = activity;
            this.attending = attending;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String tail = attending ? getString(R.string.rsvp_url) : getString(R.string.unrsvp_url);
                String url = getString(R.string.base_api_url)+tail+args[0]+"/"+args[1];
                HttpResponse response = NetworkUtil.httpPut(url, "");
                if (response == null) {
                    return "Response was null";
                } else if (response.responseCode >= 400 && response.responseCode < 600) {
                    return "Got response code "+response.responseCode;
                }
                return attending ? getString(R.string.successful_attending_message) : getString(R.string.successful_unattending_message);
            } catch (IOException e) {
                e.printStackTrace();
                return "Rsvp failed with "+e.toString();
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (attending) {
                event.getRsvps().add(user);
            } else {
                event.getRsvps().remove(user);
            }
            updateButtonCount();
            new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private class CancelEventTask extends AsyncTask<String, Void, String> {

        private Activity activity;
        private boolean success;

        public CancelEventTask(Activity activity) {
            this.activity = activity;
            success = false;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String url = getString(R.string.base_api_url)+getString(R.string.cancel_event_url);
                HttpResponse response = NetworkUtil.httpPut(url+args[0]+"/"+args[1], "");
                if (response == null) {
                    return "Response was null";
                } else if (response.responseCode >= 400 && response.responseCode < 600) {
                    return "Got response code "+response.responseCode;
                }
                success = true;
                return "Event cancel successful";
            } catch (IOException e) {
                e.printStackTrace();
                return "Event cancel with "+e.toString();
            }
        }

        @Override
        protected void onPostExecute(String message) {
            new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (success) {
                                Intent intent = new Intent(activity.getApplicationContext(), FeedActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    })
                    .show();
        }
    }
}
