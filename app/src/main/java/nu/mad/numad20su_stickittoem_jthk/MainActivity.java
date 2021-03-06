package nu.mad.numad20su_stickittoem_jthk;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;

import nu.mad.numad20su_stickittoem_jthk.models.User;

public class MainActivity extends AppCompatActivity {

    protected static final String SERVER_KEY = "key=AAAAxOrTZLk:APA91bFZDOlilw3TN4GoVctPtcHHf4yHFqmV0oseXWgUKlli5tLrb_SnVjKunonV65A0K2UsLstMuZ25h6-GsGuRMQPZnzzdCA0alGAG-OvPmx0TqE7W249QIHP6fFIHComImLjAxNKM";

    private DatabaseReference databaseReference;
    protected static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}