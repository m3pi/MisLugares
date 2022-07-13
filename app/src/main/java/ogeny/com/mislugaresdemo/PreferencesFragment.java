package ogeny.com.mislugaresdemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class PreferencesFragment extends PreferenceFragmentCompat {
    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }*/

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
