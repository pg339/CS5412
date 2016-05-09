package cornell.cs5412;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class FacebookUtil {
    public static void logout(Context activity) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LoginActivity.LOGOUT_ACTION);
        activity.sendBroadcast(broadcastIntent);
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        LoginManager.getInstance().logOut();
    }

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageUrl = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        try {
            return BitmapFactory.decodeStream(new GetStream().execute(imageUrl).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFacebookName(String userID) throws IOException, JSONException {
        URL imageUrl = new URL("https://graph.facebook.com/" + userID + "?fields=name");
        try {
            InputStream stream = new GetStream().execute(imageUrl).get();
            JSONObject obj = new JSONObject(NetworkUtil.readIt(stream, 1000));
            return obj.getString("name");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class GetStream extends AsyncTask<URL, Void, InputStream> {
        @Override
        protected InputStream doInBackground(URL... args) {
            try {
                return args[0].openConnection().getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
