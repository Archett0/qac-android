package sg.edu.nus.qac_android.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import sg.edu.nus.qac_android.data.entity.Auth0User;

public class AuthManager {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_ID_TOKEN = "ID_TOKEN";
    private static final String KEY_USER_ID = "USER_UUID_ID";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "KEYSTORE_ENCRYPTION_KEY";
    private final SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        try {
            createSecretKeyIfNotExists();
        } catch (Exception e) {
            Log.e("AuthManager", "Error initializing KeyStore: " + e.getMessage());
        }
    }

    /**
     * Save Access Token to SP
     *
     * @param token access_token
     */
    public void saveToken(String token) {
        try {
            String encryptedToken = encryptToken(token);
            Log.d("AuthManager", "Saving encrypted access token: " + encryptedToken);
            sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, encryptedToken).apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve Access Token
     *
     * @return access_token
     */
    public String getToken() {
        String encryptedToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
        if (encryptedToken == null) {
            Log.d("AuthManager", "No access token found");
            return null;
        }
        try {
            String decryptedToken = decryptToken(encryptedToken);
            Log.d("AuthManager", "Retrieved and decrypted access_token.");
            return decryptedToken;
        } catch (Exception e) {
            Log.e("AuthManager", "Error decrypting access_token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save ID Token to SP
     *
     * @param token id_token
     */
    public void saveIdToken(String token) {
        try {
            String encryptedToken = encryptToken(token);
            Log.d("AuthManager", "Saving encrypted id_token: " + encryptedToken);
            sharedPreferences.edit().putString(KEY_ID_TOKEN, encryptedToken).apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve ID Token
     *
     * @return id_token
     */
    public String getIdToken() {
        String encryptedToken = sharedPreferences.getString(KEY_ID_TOKEN, null);
        if (encryptedToken == null) {
            Log.d("AuthManager", "No id_token found");
            return null;
        }
        try {
            String decryptedToken = decryptToken(encryptedToken);
            Log.d("AuthManager", "Retrieved and decrypted id_token.");
            return decryptedToken;
        } catch (Exception e) {
            Log.e("AuthManager", "Error decrypting id_token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Set user UUID as String
     *
     * @param id user's id
     */
    public void saveUserId(String id) {
        try {
            String encryptedId = encryptToken(id);
            Log.d("AuthManager", "Saving encrypted user UUID id as String: " + encryptedId);
            sharedPreferences.edit().putString(KEY_USER_ID, encryptedId).apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get user UUID as String
     *
     * @return user's id
     */
    public String getUserId() {
        try {
            String encryptedId = sharedPreferences.getString(KEY_USER_ID, null);
            String decryptedId = decryptToken(encryptedId);
            Log.d("AuthManager", "Retrieved and decrypted user UUID id as String.");
            return decryptedId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private void createSecretKeyIfNotExists() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
            keyGenerator.generateKey();
        }
    }

    @NonNull
    private SecretKey getSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
    }

    private String encryptToken(String token) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedBytes.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedBytes);
        return Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);
    }

    private String decryptToken(String encryptedToken) throws Exception {
        byte[] tokenBytes = Base64.decode(encryptedToken, Base64.DEFAULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(tokenBytes);
        int ivLength = byteBuffer.getInt();
        if (ivLength < 12 || ivLength >= 16) {
            throw new IllegalArgumentException("Invalid IV length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        byte[] encryptedBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedBytes);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


}
