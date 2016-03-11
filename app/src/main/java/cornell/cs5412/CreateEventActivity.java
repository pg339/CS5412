package cornell.cs5412;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

public class CreateEventActivity extends Activity {
    EditText titleBox;
    EditText descriptionBox;
    EditText categoryBox;
    EditText locationBox;
    EditText latitudeBox;
    EditText longitudeBox;
    EditText minRsvpsBox;
    EditText maxRsvpsBox;
    TextView info;

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

    public void create(View view) {
//        double latitude = Double.parseDouble(latitudeBox.getText().toString());
//        double longitude = Double.parseDouble(longitudeBox.getText().toString());
//        int minRsvps = Integer.parseInt(minRsvpsBox.getText().toString());
//        int maxRsvps = Integer.parseInt(maxRsvpsBox.getText().toString());
//        Event event = new Event(titleBox.getText().toString(), descriptionBox.getText().toString(),
//                categoryBox.getText().toString(), locationBox.getText().toString(), latitude, longitude,
//                minRsvps, maxRsvps);
        Event event = new Event("Frisbee", "it'll be fun", "Sports", "Mi casa", 10.1, 10.5, 5, 10);
        Gson gson = new Gson();
        String json = gson.toJson(event);

        new CreateEventTask().execute(json);
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
