package sg.edu.nus.qac_android.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import sg.edu.nus.qac_android.data.entity.Auth0User;

public class AuthManager {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_ID_TOKEN = "ID_TOKEN";
    private static final String KEY_USER_ID = "USER_UUID_ID";
    private final SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save Access Token to SP
     *
     * @param token access_token
     */
    public void saveToken(String token) {
        Log.d("AuthManager", "Saving access token: " + token);
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    /**
     * Retrieve Access Token
     *
     * @return access_token
     */
    public String getToken() {
        String token = sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
        Log.d("AuthManager", "Retrieved access_token: " + token);
        return token;
    }

    /**
     * Save ID Token to SP
     *
     * @param token id_token
     */
    public void saveIdToken(String token) {
        Log.d("AuthManager", "Saving id_token: " + token);
        sharedPreferences.edit().putString(KEY_ID_TOKEN, token).apply();
    }

    /**
     * Retrieve ID Token
     *
     * @return id_token
     */
    public String getIdToken() {
        String token = sharedPreferences.getString(KEY_ID_TOKEN, null);
        Log.d("AuthManager", "Retrieved id_token: " + token);
        return token;
    }

    /**
     * Set user UUID as String
     *
     * @param id user's id
     */
    public void saveUserId(String id) {
        Log.d("AuthManager", "Saving user UUID id as String: " + id);
        sharedPreferences.edit().putString(KEY_USER_ID, id).apply();
    }

    /**
     * Get user UUID as String
     *
     * @return user's id
     */
    public String getUserId() {
        String id = sharedPreferences.getString(KEY_USER_ID, null);
        Log.d("AuthManager", "Retrieved user UUID id as String: " + id);
        return id;
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        Log.d("AuthManager", "Logging out and clearing token");
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_ID_TOKEN).apply();
    }

    /**
     * Parse ID Token to Auth0User Object
     *
     * @param idToken id_token from Auth0
     * @return Auth0User
     */
    public Auth0User parseIdToken(String idToken) {
        if (idToken == null || idToken.isEmpty()) {
            Log.e("AuthManager", "ID Token is null or empty");
            return null;
        }
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                Log.e("AuthManager", "Invalid ID Token format, thus enable to parse");
                return null;
            }
            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE);
            String payload = new String(decodedBytes, StandardCharsets.UTF_8);
            // Log.d("AuthManager", "Decoded payload: " + payload);

            JSONObject jsonObject = new JSONObject(payload);
            String uuid = jsonObject.optString("https://your-domain.com/uuid");
            String nickname = jsonObject.optString("nickname");
            String email = jsonObject.optString("email");
            String name = jsonObject.optString("name");
            String sub = jsonObject.optString("sub");

            return new Auth0User(uuid, nickname, email, name, sub);
        } catch (Exception e) {
            Log.e("AuthManager", "Error parsing ID Token: " + e.getMessage());
            return null;
        }
    }
}
