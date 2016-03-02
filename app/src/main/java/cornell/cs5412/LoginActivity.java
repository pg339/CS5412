package cornell.cs5412;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
    public final static String SESSION_TOKEN_KEY = "cornell.CS5412.SESSION_TOKEN_KEY";
    public final static String USER_ID = "cornell.CS5412.USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void login(View view) {
        EditText userIdBox = (EditText) findViewById(R.id.login_user_id);
        String userId = userIdBox.getText().toString();

        //TODO: Remove. Only default for convenience
        if (userId.equals("")) userId = "1";

        SharedPreferences.Editor editor = getSharedPreferences(SESSION_TOKEN_KEY, 0).edit();
        editor.putString(USER_ID, userId);
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
        startActivity(intent);
    }
}
