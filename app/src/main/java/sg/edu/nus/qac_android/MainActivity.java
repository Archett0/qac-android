package sg.edu.nus.qac_android;

import android.content.Intent;
import android.os.Bundle;

import com.auth0.android.authentication.AuthenticationException;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.nus.qac_android.auth.AuthManager;
import sg.edu.nus.qac_android.auth.LoginActivity;
import sg.edu.nus.qac_android.data.entity.Auth0User;
import sg.edu.nus.qac_android.data.entity.Question;
import sg.edu.nus.qac_android.databinding.ActivityMainBinding;
import sg.edu.nus.qac_android.notification.NotificationBottomSheet;
import sg.edu.nus.qac_android.question.CreateQuestionActivity;
import sg.edu.nus.qac_android.splash.QuestionAdapter;

import android.view.Menu;
import android.view.MenuItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import com.auth0.android.Auth0;
import com.auth0.android.provider.WebAuthProvider;


/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private List<Question> questionList;
    private ActivityMainBinding binding;

    private AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authManager = new AuthManager(this);


        if (!authManager.isLoggedIn()) {
            Log.d("MainActivity", "user not login LoginActivity");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        //test
        try {
            String token = authManager.getToken();
            Auth0User user = authManager.parseIdToken(authManager.getIdToken());
            Log.d("MainActivity", "Token found in MainActivity: " + token);
            Log.d("MainActivity", "User found in MainActivity: " + user.toString());

            if (token == null || token.isEmpty()) {
                Log.e("MainActivity", "Token is NULL or Empty, redirecting to LoginActivity");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Token parse/get failed, " + e.getMessage());
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Q&A Platform");

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateQuestionActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.splash_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionList = getMockQuestions(); // TODO: Replace with API data
        adapter = new QuestionAdapter(questionList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_notifications) {
            NotificationBottomSheet bottomSheet = new NotificationBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "NotificationBottomSheet");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Log.d("MainActivity", "Logging out...");

        Auth0 auth0 = new Auth0(
                getString(R.string.com_auth0_client_id),
                getString(R.string.com_auth0_domain)
        );

        WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(this, new com.auth0.android.callback.Callback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("MainActivity", "Logout successful");

                        authManager.logout();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.e("MainActivity", "Logout failed: " + error.getMessage());
                    }
                });
    }




    private List<Question> getMockQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(UUID.randomUUID(), "How to learn Java?",
                "Java is a widely used object-oriented programming language primarily for enterprise application development.",
                LocalDateTime.now(), UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "Spring Boot and Android interaction?",
                "Spring Boot provides powerful RESTful API capabilities, and Android accesses it through Retrofit.",
                LocalDateTime.now(), UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "What is RecyclerView?",
                "RecyclerView is a component in Android used to efficiently display long lists of data, offering more flexibility than ListView abc abc abc abc abc abc abc abc abc ",
                LocalDateTime.now(), UUID.randomUUID()));
        questions.add(new Question(UUID.randomUUID(), "Benefits of Using Kotlin",
                "Kotlin is a modern, statically typed programming language that enhances productivity and developer happiness.",
                LocalDateTime.now(), UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "Introduction to Jetpack Compose",
                "Jetpack Compose is Android's modern toolkit for building native UI, which simplifies and accelerates UI development.",
                LocalDateTime.now(), UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "Understanding LiveData",
                "LiveData is an observable data holder class in the lifecycle library that is lifecycle-aware, making it useful for updating the UI.",
                LocalDateTime.now(), UUID.randomUUID()));
        return questions;
    }


}
