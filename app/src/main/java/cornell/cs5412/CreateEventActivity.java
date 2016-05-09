package cornell.cs5412;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends AppCompatActivity {
    EditText titleBox;
    EditText descriptionBox;
    EditText categoryBox;
    EditText locationBox;
    EditText latitudeBox;
    EditText longitudeBox;
    EditText minRsvpsBox;
    EditText maxRsvpsBox;
    TextView info;
    IEvent event;

    private BroadcastReceiver logoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleBox = (EditText) findViewById(R.id.event_title_box);
        descriptionBox = (EditText) findViewById(R.id.description_box);
        categoryBox = (EditText) findViewById(R.id.category_box);
        locationBox = (EditText) findViewById(R.id.location_box);
        latitudeBox = (EditText) findViewById(R.id.latitude_box);
        longitudeBox = (EditText) findViewById(R.id.longitude_box);
        minRsvpsBox = (EditText) findViewById(R.id.minRsvps_box);
        maxRsvpsBox = (EditText) findViewById(R.id.maxRsvps_box);
        info = (TextView) findViewById(R.id.create_event_label);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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

    /**
     * From Android developer's guide
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            GregorianCalendar cal = new GregorianCalendar(year, month, day);

        }
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
