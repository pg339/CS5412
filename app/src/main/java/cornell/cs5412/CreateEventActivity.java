package cornell.cs5412;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends AppCompatActivity implements
        DatePickerFragment.OnCompleteListener,
        TimePickerFragment.OnCompleteListener {
    EditText titleBox;
    EditText descriptionBox;
    EditText locationBox;
    TextView dateBox;
    TextView timeBox;
    EditText minRsvpsBox;
    EditText maxRsvpsBox;
    TextView info;
    Button createButton;
    TextView label;
    int hour;
    int minute;
    int day;
    int month;
    int year;
    double longitude;
    double latitute;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            longitude = location.getLongitude();
            latitute = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (label.getText().toString().equals(getResources().getString(R.string.provider_off_message))) {
                label.setText("");
            }
            createButton.setClickable(true);
        }

        @Override
        public void onProviderDisabled(String provider) {
            label.setText(getResources().getString(R.string.provider_off_message));
            createButton.setClickable(false);
        }
    };

    private LocationManager mLocationManager;
    private BroadcastReceiver logoutReceiver;
    private static final int LOCATION_REFRESH_TIME = 60000;
    private static final int LOCATION_REFRESH_DISTANCE = 500;
    public static final int PERMISSIONS_REQUEST_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
        Location location = MiscHelpers.getLastKnownLocation(mLocationManager);
        latitute = location.getLatitude();
        longitude = location.getLongitude();

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_creation_toolbar);
        setActionBar(toolbar);

        titleBox = (EditText) findViewById(R.id.event_title_box);
        descriptionBox = (EditText) findViewById(R.id.description_box);
        locationBox = (EditText) findViewById(R.id.location_box);
        minRsvpsBox = (EditText) findViewById(R.id.minRsvps_box);
        maxRsvpsBox = (EditText) findViewById(R.id.maxRsvps_box);
        dateBox = (TextView) findViewById(R.id.date_box);
        timeBox = (TextView) findViewById(R.id.time_box);
        info = (TextView) findViewById(R.id.create_event_label);
        createButton = (Button) findViewById(R.id.create_event_button);
        label = (TextView) findViewById(R.id.create_event_label);

        Calendar c = Calendar.getInstance();
        dateBox.setText(java.text.DateFormat.getDateInstance().format(c.getTime()));
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        timeBox.setText((hour > 12 ? hour-12: hour)+":"+
                (minute < 10 ? "0"+minute : minute) +
                (hour>12 ? " PM" : " AM"));

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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void create(View view) {
        createButton.setClickable(false);
        label.setText("");
        Event event = new Event();
        event.setTitle(titleBox.getText().toString());
        event.setDescription(descriptionBox.getText().toString());
        GregorianCalendar c = new GregorianCalendar(year, month, day, hour, minute);
        event.setStartTime(MiscHelpers.formatDateForNetwork(c.getTime()));
        event.setLocation(locationBox.getText().toString());
        String min = minRsvpsBox.getText().toString();
        event.setMinRsvps(minRsvpsBox.getText().toString().equals("") ? 0 : Integer.parseInt(min));
        String max = maxRsvpsBox.getText().toString();
        event.setMaxRsvps(maxRsvpsBox.getText().toString().equals("") ? Integer.MAX_VALUE : Integer.parseInt(max));
        event.updateEventStatus();
        event.setLatitude(latitute);
        event.setLongitude(longitude);

        String errorMessage = "";
        if (Calendar.getInstance().getTime().after(c.getTime())) {
            errorMessage += "Please select a start time in the future\n";
        } if (event.getMinRsvps() != null && event.getMaxRsvps() != null && event.getMinRsvps() > event.getMaxRsvps()) {
            errorMessage += "Please select a valid range of guests\n";
        } if (event.getMaxRsvps() != null && event.getMaxRsvps().equals(0)) {
            errorMessage += "Please let at least one person attend\n";
        } if (event.getTitle().length() == 0) {
            errorMessage += "Please provide a title for the event\n";
        } if (event.getLocation().length() == 0)  {
            errorMessage += "Please provide a location for the event";
        } if (errorMessage.length() > 0) {
            label.setText(errorMessage);
            return;
        } else {
            //        Event event = new Event("Frisbee", "it'll be fun", "Mi casa", 10.1, 10.5, 5, 10);
            Gson gson = new Gson();
            String json = gson.toJson(event);

            new CreateEventTask(this).execute(json);
        }
    }


    @Override
    public void onCompleteTimePicker(int hour, int minute) {
        timeBox.setText((hour > 12 ? hour-12: hour)+":"+
                (minute < 10 ? "0"+minute : minute) +
                (hour>12 ? " PM" : " AM"));
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public void onCompleteDatePicker(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar(year, month, day);
        dateBox.setText(java.text.DateFormat.getDateInstance().format(cal.getTime()));
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private class CreateEventTask extends AsyncTask<String, Void, String> {

        private Activity activity;
        private boolean success;

        public CreateEventTask(Activity activity) {
            this.activity = activity;
            success = false;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String url = getString(R.string.base_api_url)+getString(R.string.create_event_url);
                HttpResponse response = NetworkUtil.httpPost(url, args[0]);
                if (response == null) {
                    return "Response was null";
                } else if (response.responseCode >= 400 && response.responseCode < 600) {
                    return "Got response code "+response.responseCode;
                }
                success = true;
                return getString(R.string.event_created_message);
            } catch (IOException e) {
                e.printStackTrace();
                return "Event creation failed with "+e.toString();
            }
        }

        @Override
        protected void onPostExecute(String res) {
            createButton.setClickable(true);
            new AlertDialog.Builder(activity)
                    .setMessage(res)
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
