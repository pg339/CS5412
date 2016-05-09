package cornell.cs5412;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
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
    int hour;
    int minute;
    int day;
    int month;
    int year;

    GoogleApiClient mGoogleApiClient;

    private BroadcastReceiver logoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();

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

        Calendar c = Calendar.getInstance();
        dateBox.setText(java.text.DateFormat.getDateInstance().format(c.getTime()));
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        hour = c.get(Calendar.HOUR);
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
//        double latitude = Double.parseDouble(latitudeBox.getText().toString());
//        double longitude = Double.parseDouble(longitudeBox.getText().toString());
//        int minRsvps = Integer.parseInt(minRsvpsBox.getText().toString());
//        int maxRsvps = Integer.parseInt(maxRsvpsBox.getText().toString());
//        Event event = new Event(titleBox.getText().toString(), descriptionBox.getText().toString(),
//                categoryBox.getText().toString(), locationBox.getText().toString(), latitude, longitude,
//                minRsvps, maxRsvps);
        Event event = new Event("Frisbee", "it'll be fun", "Mi casa", 10.1, 10.5, 5, 10);
        Gson gson = new Gson();
        String json = gson.toJson(event);

        new CreateEventTask().execute(json);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

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

    private class CreateEventTask extends AsyncTask<String, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(String... args) {
            try {
                String url = getString(R.string.create_event_url);
                HttpResponse response = NetworkUtil.httpPost(url, args[0]);
                if (response.responseCode >= 400 && response.responseCode < 600) {
                    cancel(true);
                    return null;
                }
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpResponse res) {
            info.setText("Response code is: " + res.responseCode + " and content is " + res.content);
        }

        @Override
        protected void onCancelled(HttpResponse s) {
            //TODO: Refine this possibly
            info.setText("Event creation failed");
        }
    }
}
