package cornell.cs5412;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends Activity {
    public final static String SESSION_TOKEN_KEY = "cornell.CS5412.SESSION_TOKEN_KEY";
    public final static String USER_ID = "cornell.CS5412.USER_ID";
    public final static String LOGOUT_ACTION = "cornell.CS412.LOGOUT_ACTION";

    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public TextView loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginStatus = (TextView) findViewById(R.id.login_label);

        ArrayList<String> readPermissions = new ArrayList<>();
        readPermissions.add("public_profile");
        readPermissions.add("user_friends");
        loginButton.setReadPermissions(readPermissions);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new CreateAccountTask().execute("pablo");
            }

            @Override
            public void onCancel() {
                loginButton.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                loginButton.setText("Login attempt failed.");
            }
        });

        if (FacebookUtil.isLoggedIn()) {
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class CreateAccountTask extends AsyncTask<String, Void, HttpResponse> {
        //TODO: Change return to Feed, implement parcelable in Feed and Coordinates

        @Override
        protected HttpResponse doInBackground(String... args) {
            try {
                String url = getString(R.string.create_account_url);
                HttpResponse response = NetworkUtil.httpPost(url + args[0], "");
                if (response.responseCode >= 500 && response.responseCode < 600) {
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
            if (res != null) {
                loginStatus.setText("Response code is: " + res.responseCode + " and content is " + res.content);
                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                loginStatus.setText("Response came back empty");
            }
        }

        @Override
        protected void onCancelled(HttpResponse s) {
            //TODO: Refine this possibly
            loginStatus.setText("Login failed");
        }
    }
}
