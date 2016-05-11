package cornell.cs5412;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MiscHelpers {
    public static int pixToDp(Context context, int pix) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pix * scale + 0.5f);
    }

    public static String formatDateForNetwork(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.format(date);
    }

    public static String makeDatePretty(String in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            Date date = format.parse(in);
            return DateFormat.getDateInstance().format(date) + " at " +
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        try {
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;
        } catch (SecurityException e) {}
        return null;
    }
}
