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
                .withAudience("https://dev-jr5iip1iu6v8pylo.us.auth0.com/userinfo")
                .withScope("openid profile email")
                .start(this, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        Log.d("LoginActivity", "Login Success: " + credentials.getAccessToken());

                        if (credentials.getAccessToken() == null || credentials.getAccessToken().isEmpty()) {
                            Log.e("LoginActivity", "Access Token is NULL or Empty, login failed");
                            return;
                        }

                        // 存储 Token
                        authManager.saveToken(credentials.getAccessToken());

                        Log.d("LoginActivity", "Token saved successfully: " + credentials.getAccessToken());

                        // 跳转到 MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("TOKEN", credentials.getAccessToken()); // 传递 Token
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
