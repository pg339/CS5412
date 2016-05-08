package cornell.cs5412;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;

import java.io.IOException;

public class FeedActivity extends Activity {
    private static final int[] ICONS = {R.drawable.ic_mode_edit_black_24dp,
            R.drawable.ic_today_black_24dp,
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_do_not_disturb_black_24dp};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FeedEvent[] events;

    private RecyclerView dRecyclerView;
    private RecyclerView.Adapter dAdapter;
    private RecyclerView.LayoutManager dLayoutManager;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private BroadcastReceiver logoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMainContent();
        toolbar = (Toolbar) findViewById(R.id.feed_activity_toolbar);
        setActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.feed_activity_drawer_layout);
        dRecyclerView = (RecyclerView) findViewById(R.id.activity_feed_drawer_recycler);
        dRecyclerView.setHasFixedSize(true);
        getResources().getStringArray(R.array.drawer_options);
        try {
            dAdapter = new DrawerAdapter(getResources().getStringArray(R.array.drawer_options),
                    ICONS, Profile.getCurrentProfile().getName(),
                    FacebookUtil.getFacebookProfilePicture(Profile.getCurrentProfile().getId()));
            dRecyclerView.setAdapter(dAdapter);
            dLayoutManager = new LinearLayoutManager(this);
            dRecyclerView.setLayoutManager(dLayoutManager);
            dRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            //TODO: Handle menu item click
                            String[] options = getResources().getStringArray(R.array.drawer_options);
                            switch (options[position-1]) {
                                case "My events": break;
                                case "Events I'm attending": break;
                                case "Settings": break;
                                case "Log out":
                                    FacebookUtil.logout(view.getContext());
                                    break;
                            }
                        }
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeMainContent() {
        events = getEvents();
        if (events.length == 0) {
            setContentView(R.layout.activity_feed_empty);
        } else {
            setContentView(R.layout.activity_feed);
            mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new FeedAdapter(events);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getApplicationContext(), EventViewActivity.class);
                            intent.putExtra(EventViewActivity.EVENT_EXTRA, (Parcelable) ((FeedAdapter) mAdapter).events[position].event);
                            startActivity(intent);
                        }
                    }));
        }

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public FeedEvent[] getEvents() {
        //placeholder event samples
        Event event1 = new Event("My event", "Pablo", "Fun in the sun", "Saturday, April 23rd at 8 PM", "David L. Call Auditorium");
        Event event2 = new Event("My super long and obnoxious event name hahahahahahahahahahhahaahahahahahahaa",
                "Pablo Garciaaaaaaaaaaaaaaaaaaaaa-Quesada",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ex ante, tempor vel pellentesque ut, cursus at ligula. Sed purus nisi, fermentum nec felis id, viverra consequat nisi. Sed fermentum convallis purus, eu viverra enim feugiat sed. Proin sit amet pulvinar dui. Nam nibh nisl, scelerisque vel elementum vitae, iaculis a arcu. Ut convallis nunc non metus pellentesque semper. Cras eget elit lorem. Aliquam erat volutpat. Praesent sollicitudin imperdiet condimentum. Sed a purus at dolor rhoncus elementum a et ligula. Duis odio tortor, ornare ac elementum ac, varius id nunc. Etiam blandit gravida urna in cursus. Praesent ultricies tortor et arcu luctus, bibendum consectetur massa eleifend. Proin fringilla lorem vel feugiat maximus. Vivamus nec lobortis ante. Phasellus dictum fringilla semper.\n" +
                        "\n" +
                        "Mauris sit amet elit interdum, egestas elit at, luctus purus. Donec sed lorem vel tellus imperdiet efficitur. Cras dapibus id sapien ut sodales. Nulla id eleifend ex. Pellentesque ante ipsum, ullamcorper vel posuere nec, venenatis non ante. Nullam ut sem eget erat posuere accumsan tincidunt id justo. Etiam pulvinar sagittis nulla vitae fringilla. Donec a massa eu tortor aliquam interdum. Morbi eget congue neque, eget maximus justo. Suspendisse et urna ac tortor tristique porta nec eu dui. Fusce in commodo dui. Cras eu venenatis mauris. Aenean sed tempor libero, consectetur ornare lacus. Sed nec augue purus. Nam non erat et neque semper porttitor. Nulla a lobortis dolor, volutpat posuere est.\n" +
                        "\n" +
                        "Sed in lacus in metus aliquam gravida id eu nibh. Integer magna velit, posuere ac ex ac, molestie tincidunt augue. Curabitur et felis est. Vestibulum consectetur, diam aliquam scelerisque venenatis, ante massa eleifend risus, ac commodo justo ligula eget lacus. Aenean efficitur lacinia odio, vel finibus mi varius interdum. Nam id velit suscipit, dignissim velit sed, varius nulla. Aenean sit amet ligula luctus, maximus ante vel, congue arcu. Cras finibus posuere quam.",
                "Saturday, April 23rd at 8 PM",
                "David L. Call Auditorium, Ithaca, NY 14850000000000000000000000000000000000");
        return new FeedEvent[] {new FeedEvent(getApplicationContext(), event1), new FeedEvent(getApplicationContext(), event2)};
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