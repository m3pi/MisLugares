package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // insertando preferencias
        /*getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,  new PreferencesFragment())
                .commit();*/

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new PreferencesFragment())
                .commit();
    }
}