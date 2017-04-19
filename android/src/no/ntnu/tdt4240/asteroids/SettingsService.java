package no.ntnu.tdt4240.asteroids;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsService implements ISettingsService {

    private final SharedPreferences preferences;
    private Context context;

    SettingsService(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean getBoolean(String id) {
        return preferences.getBoolean(id, false);
    }

    @Override
    public void setBoolean(String id, boolean value) {
        preferences.edit().putBoolean(id, value).apply();
    }

    @Override
    public String getString(String id, String defaultValue) {
        return preferences.getString(id, defaultValue);
    }

    @Override
    public void setString(String id, String value) {
        preferences.edit().putString(id, value).apply();
    }

    @Override
    public int getInt(String id, int defaultValue) {
        return preferences.getInt(id, 0);
    }

    @Override
    public void setInt(String id, int value) {
        preferences.edit().putInt(id, value).apply();
    }

//    public void setDefaultPreferences() {
//        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
//    }
}
