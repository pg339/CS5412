package cornell.cs5412;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;

public class FeedActivity extends Activity {

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Display feed JSON
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        info = (TextView) findViewById(R.id.feed_text);
        Profile profile = Profile.getCurrentProfile();
        info.setText("User ID: "
                        + profile.getId()
                        + "\n" +
                        "Auth Token: "
                        + AccessToken.getCurrentAccessToken().getToken()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
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

    public void createEvent(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
        startActivity(intent);
    }

    private class GetMainFeedTask extends AsyncTask<String, Void, Feed> {
        //TODO: Actually get and process feed

        @Override
        protected Feed doInBackground(String... args) {
            return null;
        }

        @Override
        protected void onPostExecute(Feed feed) {

        }
    }
}
