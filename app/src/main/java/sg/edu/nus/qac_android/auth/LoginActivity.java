package sg.edu.nus.qac_android.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import sg.edu.nus.qac_android.MainActivity;
import sg.edu.nus.qac_android.R;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.result.UserProfile;
import sg.edu.nus.qac_android.data.entity.Auth0User;



public class LoginActivity extends AppCompatActivity {
    private Auth0 auth0;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化 Auth0
        auth0 = new Auth0(
                getString(R.string.com_auth0_client_id),
                getString(R.string.com_auth0_domain)
        );

        authManager = new AuthManager(this);

        if (authManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        findViewById(R.id.loginButton).setOnClickListener(v -> login());
        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }

    private void login() {
        WebAuthProvider.login(auth0)
                .withScheme("demo")
                .withAudience("https://qac.com")
                .withScope("openid profile email")
                .start(this, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        if (credentials.getAccessToken().isEmpty()) {
                            Log.e("LoginActivity", "Access Token is NULL or Empty, login failed");
                            return;
                        }
                        if (credentials.getIdToken().isEmpty()) {
                            Log.e("LoginActivity", "ID TOKEN EMPTY");
                            return;
                        }
                        authManager.saveToken(credentials.getAccessToken());
                        authManager.saveIdToken(credentials.getIdToken());
                        Log.d("LoginActivity", "Both tokens SAVED successfully, now parsing");

                        Auth0User auth0User = authManager.parseIdToken(credentials.getIdToken());
                        Log.d("LoginActivity", "ID token PARSED successfully: " + auth0User.toString());

                        if (auth0User != null && auth0User.getUuid() != null) {
                            authManager.saveUserId(auth0User.getUuid());
                            Log.d("LoginActivity", "User ID saved: " + auth0User.getUuid());
                        } else {
                            Log.e("LoginActivity", "Failed to parse user ID from ID Token");
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("TOKEN", credentials.getAccessToken());
                        intent.putExtra("ID_TOKEN", credentials.getIdToken());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.e("LoginActivity", "Login Failed: " + error.getMessage());
                    }
                });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getData() != null) {
            WebAuthProvider.resume(intent);
        }
    }
}
