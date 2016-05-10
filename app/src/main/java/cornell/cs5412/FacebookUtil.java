package cornell.cs5412;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

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

    public static Bitmap getFacebookProfilePicture(String userID, Context context) throws IOException {
        try {
            return new GetProfilePicture(context).execute("https://graph.facebook.com/" + userID + "/picture?type=large").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFacebookProfileField(String userID, String field) {
        try {
            return new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/"+userID,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {}
                    }
            ).executeAsync().get().get(0).getJSONObject().getString(field);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class GetStream extends AsyncTask<URL, Void, InputStream> {
        @Override
        protected InputStream doInBackground(URL... args) {
            try {
                InputStream stream = args[0].openConnection().getInputStream();
                return stream;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class GetProfilePicture extends AsyncTask<String, Void, Bitmap> {

        private Context context;

        public GetProfilePicture(Context context) {
            this.context = context;
        }

        @Override
        protected  Bitmap doInBackground(String... args) {
            try {
                return Picasso.with(context)
                        .load(args[0])
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
