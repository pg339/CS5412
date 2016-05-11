package cornell.cs5412;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FeedActivity extends AppCompatActivity {
    private static final int[] ICONS = {R.drawable.ic_mode_edit_black_24dp,
            R.drawable.ic_today_black_24dp,
            R.drawable.ic_view_headline_black_24dp,
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
    private TextView label;

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
        }

        @Override
        public void onProviderDisabled(String provider) {
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
                    FacebookUtil.getFacebookProfilePicture(Profile.getCurrentProfile().getId(), getApplicationContext()));
            dRecyclerView.setAdapter(dAdapter);
            dLayoutManager = new LinearLayoutManager(this);
            dRecyclerView.setLayoutManager(dLayoutManager);
            dRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String[] options = getResources().getStringArray(R.array.drawer_options);
                            switch (options[position - 1]) {
                                case "My events":
                                    clearFeed();
                                    events = getMyEvents();
                                    if (events != null && events.length == 0) {
                                        label.setText(getString(R.string.no_events_created_text));
                                    } else if (events != null){
                                        label.setText("");
                                        setFeedEvents(events);
                                    }
                                    drawer.closeDrawers();
                                    break;
                                case "Events I'm attending":
                                    clearFeed();
                                    events = getEventsImAttending();
                                    if (events != null && events.length == 0) {
                                        label.setText(getString(R.string.no_events_attending_text));
                                    } else if (events != null) {
                                        label.setText("");
                                        setFeedEvents(events);
                                    }
                                    drawer.closeDrawers();
                                    break;
                                case "Newsfeed":
                                    clearFeed();
                                    events = getFeedEvents();
                                    if (events != null && events.length == 0) {
                                        label.setText(getString(R.string.empty_feed_text));
                                    } else if (events != null){
                                        label.setText("");
                                        setFeedEvents(events);
                                    }
                                    drawer.closeDrawers();
                                    break;
                                case "Log out":
                                    FacebookUtil.logout(view.getContext());
                                    break;
                            }
                        }
                    }));
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.feed_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        events = getFeedEvents();
        if (events == null || events.length == 0) {
            clearFeed();
            label.setText(getString(R.string.empty_feed_text));
        } else {
            label.setText("");
            setFeedEvents(events);
        }
    }

    public void initializeMainContent() {
        setContentView(R.layout.activity_feed);
        events = getFeedEvents();

        label = (TextView) findViewById(R.id.feed_activity_label);
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

        if (events == null || events.length == 0) {
            label.setText(getString(R.string.empty_feed_text));
        } else {
            setFeedEvents(events);
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

    public void clearFeed() {
        mAdapter = new FeedAdapter(new FeedEvent[0]);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setFeedEvents(FeedEvent[] events) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        events = getFeedEvents();
        if (events == null || events.length == 0) {
            clearFeed();
            label.setText(getString(R.string.empty_feed_text));
        } else {
            label.setText("");
            setFeedEvents(events);
        }
    }

    public FeedEvent[] getMyEvents() {
        try {
            DisplayUser user = new GetUserTask().execute(Profile.getCurrentProfile().getId()).get();
            if (user == null || user.events == null) {
                label.setText("Fetching my events failed\nbecause of null "+(user == null ? "user" : "event list"));
                return null;
            }
            FeedEvent[] feedEvents = new FeedEvent[user.events.size()];
            for (int i=0; i<feedEvents.length; i++) {
                feedEvents[i] = new FeedEvent(getApplicationContext(), user.events.get(i));
            }
            return feedEvents;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            label.setText("Fetching my events failed with\n"+e.toString());
        } return null;
    }

    public FeedEvent[] getEventsImAttending() {
        try {
            DisplayUser user = new GetUserTask().execute(Profile.getCurrentProfile().getId()).get();
            if (user == null || user.rsvps == null) {
                label.setText("Fetching rsvp'd events failed\nbecause of null "+(user == null ? "user" : "rsvp list"));
                return null;
            }
            FeedEvent[] feedEvents = new FeedEvent[user.rsvps.size()];
            for (int i=0; i<feedEvents.length; i++) {
                feedEvents[i] = new FeedEvent(getApplicationContext(), user.rsvps.get(i));
            }
            return feedEvents;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            label.setText("Fetching rsvp'd events failed\nwith error "+e.toString());
        } return null;
    }

    public FeedEvent[] getFeedEvents() {
        String in = "?lat="+latitute+"&lon="+longitude;
        try {
            List<Event> feed = new GetMainFeedTask().execute(in).get();
            if (feed == null) {
                label.setText("Fetching newsfeed failed\nbecause of null feed");
                return null;
            }
            FeedEvent[] feedEvents = new FeedEvent[feed.size()];
            for (int i=0; i<feedEvents.length; i++) {
                feedEvents[i] = new FeedEvent(getApplicationContext(), feed.get(i));
            }
            return feedEvents;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            label.setText("Fetching newsfeed failed with\n"+e.toString());
        } return null;
//        Event event1 = new Event("My event", Profile.getCurrentProfile().getId(), "Fun in the sun", "Saturday, April 23rd at 8 PM", "David L. Call Auditorium");
//        Event event2 = new Event("My super long and obnoxious event name hahahahahahahahahahhahaahahahahahahaa",
//                Profile.getCurrentProfile().getId(),
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ex ante, tempor vel pellentesque ut, cursus at ligula. Sed purus nisi, fermentum nec felis id, viverra consequat nisi. Sed fermentum convallis purus, eu viverra enim feugiat sed. Proin sit amet pulvinar dui. Nam nibh nisl, scelerisque vel elementum vitae, iaculis a arcu. Ut convallis nunc non metus pellentesque semper. Cras eget elit lorem. Aliquam erat volutpat. Praesent sollicitudin imperdiet condimentum. Sed a purus at dolor rhoncus elementum a et ligula. Duis odio tortor, ornare ac elementum ac, varius id nunc. Etiam blandit gravida urna in cursus. Praesent ultricies tortor et arcu luctus, bibendum consectetur massa eleifend. Proin fringilla lorem vel feugiat maximus. Vivamus nec lobortis ante. Phasellus dictum fringilla semper.\n" +
//                        "\n" +
//                        "Mauris sit amet elit interdum, egestas elit at, luctus purus. Donec sed lorem vel tellus imperdiet efficitur. Cras dapibus id sapien ut sodales. Nulla id eleifend ex. Pellentesque ante ipsum, ullamcorper vel posuere nec, venenatis non ante. Nullam ut sem eget erat posuere accumsan tincidunt id justo. Etiam pulvinar sagittis nulla vitae fringilla. Donec a massa eu tortor aliquam interdum. Morbi eget congue neque, eget maximus justo. Suspendisse et urna ac tortor tristique porta nec eu dui. Fusce in commodo dui. Cras eu venenatis mauris. Aenean sed tempor libero, consectetur ornare lacus. Sed nec augue purus. Nam non erat et neque semper porttitor. Nulla a lobortis dolor, volutpat posuere est.\n" +
//                        "\n" +
//                        "Sed in lacus in metus aliquam gravida id eu nibh. Integer magna velit, posuere ac ex ac, molestie tincidunt augue. Curabitur et felis est. Vestibulum consectetur, diam aliquam scelerisque venenatis, ante massa eleifend risus, ac commodo justo ligula eget lacus. Aenean efficitur lacinia odio, vel finibus mi varius interdum. Nam id velit suscipit, dignissim velit sed, varius nulla. Aenean sit amet ligula luctus, maximus ante vel, congue arcu. Cras finibus posuere quam.",
//                "Saturday, April 23rd at 8 PM",
//                "David L. Call Auditorium, Ithaca, NY 14850000000000000000000000000000000000");
//        return new FeedEvent[] {new FeedEvent(getApplicationContext(), event1),
//                new FeedEvent(getApplicationContext(), event2),
//                new FeedEvent(getApplicationContext(), event2),
//                new FeedEvent(getApplicationContext(), event2),
//                new FeedEvent(getApplicationContext(), event2),
//                new FeedEvent(getApplicationContext(), event2)};
    }

    private class GetMainFeedTask extends AsyncTask<String, Void, List<Event>> {

        public boolean success;

        @Override
        protected List<Event> doInBackground(String... args) {
            try {
                String url = getString(R.string.base_api_url)+getString(R.string.get_feed_url);
                HttpResponse response = NetworkUtil.httpGet(url+args[0]);
                if (response == null || (response.responseCode >= 400 && response.responseCode < 600)) {
                    cancel(true);
                    return null;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<List<Event>>(){}.getType();
                List<Event> events = gson.fromJson(response.content, type);
                success = true;
                return events;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class GetUserTask extends AsyncTask<String, Void, DisplayUser> {

        private boolean success;

        @Override
        protected DisplayUser doInBackground(String... args) {
            try {
                String url = getString(R.string.base_api_url)+getString(R.string.get_user_url);
                HttpResponse response = NetworkUtil.httpGet(url+args[0]);
                if (response == null || (response.responseCode >= 400 && response.responseCode < 600)) {
                    cancel(true);
                    return null;
                }
                Gson gson = new Gson();
                success = true;
                return gson.fromJson(response.content, DisplayUser.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}