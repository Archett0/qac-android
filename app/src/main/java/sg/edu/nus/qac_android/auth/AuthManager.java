package sg.edu.nus.qac_android.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AuthManager {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_TOKEN = "ACCESS_TOKEN";

    private SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        Log.d("AuthManager", "Saving token: " + token);
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        Log.d("AuthManager", "Retrieved token: " + token);
        return token;
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        Log.d("AuthManager", "Logging out and clearing token");
        sharedPreferences.edit().remove(KEY_TOKEN).apply();
    }
}
