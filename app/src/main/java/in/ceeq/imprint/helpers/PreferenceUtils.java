package in.ceeq.imprint.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String API_TOKEN = "api_token";

    public static final String GCM_REGISTRATION_TOKEN = "gcm_registration_token";

    public static final String USE_FINGERPRINT = "use_fingerprint";

    private static final String PREFERENCE_FILE_NAME = "common_preferences";
    private final SharedPreferences mSharedPreference;

    private PreferenceUtils(Context context) {
        mSharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public PreferenceUtils(Context context, String preferenceFileName) {
        mSharedPreference = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
    }

    public static PreferenceUtils newInstance(Context context) {
        return new PreferenceUtils(context);
    }

    public boolean getBooleanPrefs(String key) {
        return getBooleanPrefs(key, false);
    }

    public boolean getBooleanPrefs(String key, Boolean defaultValue) {
        return mSharedPreference.getBoolean(key, defaultValue);
    }

    public String getStringPrefs(String key) {
        return mSharedPreference.getString(key, "");
    }

    public String getStringPrefs(String key, String defaultValue) {
        return mSharedPreference.getString(key, defaultValue);
    }

    public int getIntegerPrefs(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    public int getIntegerPrefs(String key, int defaultValue) {
        return mSharedPreference.getInt(key, defaultValue);
    }

    public long getLongPrefs(String key) {
        return mSharedPreference.getLong(key, 0L);
    }

    public long getLongPrefs(String key, long defaultValue) {
        return mSharedPreference.getLong(key, defaultValue);
    }


    public float getFloatPrefs(String key) {
        return mSharedPreference.getFloat(key, 0);
    }

    public void set(final String key, final Object value) {
        final SharedPreferences.Editor sharedPreferenceEditor = mSharedPreference.edit();
        if (value instanceof String) {
            sharedPreferenceEditor.putString(key, (String) value);
        } else if (value instanceof Long) {
            sharedPreferenceEditor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            sharedPreferenceEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            sharedPreferenceEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            sharedPreferenceEditor.putFloat(key, (Float) value);
        }
        sharedPreferenceEditor.apply();
    }

    public void clear() {
        mSharedPreference.edit().clear().commit();
    }

    public boolean isKeyPresent(String key) {
        return mSharedPreference.contains(key);
    }

    public static void clear(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }
}
