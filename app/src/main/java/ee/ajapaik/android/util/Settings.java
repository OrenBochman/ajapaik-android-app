package ee.ajapaik.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import ee.ajapaik.android.data.Profile;
import ee.ajapaik.android.data.Session;
import ee.ajapaik.android.data.Upload;

public class Settings {
    private static final String TAG = "Settings";
    private static final String SHARED_PREFS = "prefs";

    private static final String KEY_AUTHORIZATION = "authorization";
    private static final String KEY_LOCATION_ACCURACY = "location.accuracy";
    private static final String KEY_LOCATION_LATITUDE = "location.latitude";
    private static final String KEY_LOCATION_LONGITUDE = "location.longitude";
    private static final String KEY_LOCATION_TIME = "location.time";
    private static final String KEY_PROFILE = "profile";
    private static final String KEY_UPLOAD = "upload";
    private static final String KEY_SESSION = "session";

    private SharedPreferences m_preferences;

    public Settings(Context context) {
        m_preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public Authorization getAuthorization() {
        return Authorization.parse(m_preferences.getString(KEY_AUTHORIZATION, null));
    }

    public void setAuthorization(Authorization authorization) {
        SharedPreferences.Editor editor = m_preferences.edit();

        editor.putString(KEY_AUTHORIZATION, (authorization != null) ? authorization.toString() : null);
        editor.apply();
    }

    public Location getLocation() {
        String accuracy = m_preferences.getString(KEY_LOCATION_ACCURACY, null);
        String latitude = m_preferences.getString(KEY_LOCATION_LATITUDE, null);
        String longitude = m_preferences.getString(KEY_LOCATION_LONGITUDE, null);
        String time = m_preferences.getString(KEY_LOCATION_TIME, null);

        if(latitude != null && longitude != null) {
            try {
                Location location = new Location(TAG);

                if(accuracy != null) {
                    location.setAccuracy(Float.parseFloat(accuracy));
                }

                if(time != null) {
                    location.setTime(Long.parseLong(time));
                }

                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));

                return location;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setLocation(Location location) {
        SharedPreferences.Editor editor = m_preferences.edit();

        if(location != null) {
            editor.putString(KEY_LOCATION_ACCURACY, Float.toString(location.getAccuracy()));
            editor.putString(KEY_LOCATION_LATITUDE, Double.toString(location.getLatitude()));
            editor.putString(KEY_LOCATION_LONGITUDE, Double.toString(location.getLongitude()));
            editor.putString(KEY_LOCATION_TIME, Long.toString(location.getTime()));
        } else {
            editor.putString(KEY_LOCATION_ACCURACY, null);
            editor.putString(KEY_LOCATION_LATITUDE, null);
            editor.putString(KEY_LOCATION_LONGITUDE, null);
            editor.putString(KEY_LOCATION_TIME, null);
        }

        editor.apply();
    }

    public Profile getProfile() {
        return Profile.parse(m_preferences.getString(KEY_PROFILE, null));
    }

    public void setProfile(Profile profile) {
        SharedPreferences.Editor editor = m_preferences.edit();

        editor.putString(KEY_PROFILE, (profile != null) ? profile.toString() : null);
        editor.apply();
    }

    public Session getSession() {
        return Session.parse(m_preferences.getString(KEY_SESSION, null));
    }

    public void setSession(Session session) {
        SharedPreferences.Editor editor = m_preferences.edit();

        editor.putString(KEY_SESSION, (session != null) ? session.toString() : null);
        editor.apply();
    }

    public Upload getUpload() {
        return Upload.parse(m_preferences.getString(KEY_UPLOAD, null));
    }

    public void setUpload(Upload upload) {
        SharedPreferences.Editor editor = m_preferences.edit();

        editor.putString(KEY_UPLOAD, (upload != null) ? upload.toString() : null);
        editor.apply();
    }
}
