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
        String token = authManager.getToken();
        Log.d("MainActivity", "Token found in MainActivity: " + token);

        if (token == null || token.isEmpty()) {
            Log.e("MainActivity", "Token is NULL or Empty, redirecting to LoginActivity");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
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

        questions.add(new Question(UUID.randomUUID(), "What is Dependency Injection?",
                "Dependency Injection (DI) is a design pattern used to implement IoC (Inversion of Control), allowing objects to be injected instead of being created inside a class. Popular frameworks for DI include Spring and Dagger.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "How does Retrofit work in Android?",
                "Retrofit is a type-safe HTTP client for Android and Java, used for network communication. It simplifies REST API calls and JSON parsing with built-in serialization support.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "What are the benefits of using Jetpack Compose?",
                "Jetpack Compose is Android’s modern UI toolkit that simplifies UI development with a declarative approach, reducing boilerplate code and improving performance.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "How to optimize RecyclerView performance?",
                "To optimize RecyclerView performance, use ViewHolder pattern, enable DiffUtil for item changes, avoid nested layouts, and use setHasFixedSize(true) when applicable.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "Why use Kotlin for Android development?",
                "Kotlin offers concise syntax, null safety, coroutine support for asynchronous programming, and seamless Java interoperability, making it an excellent choice for Android development.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "What is MVVM architecture in Android?",
                "MVVM (Model-View-ViewModel) is a design pattern that separates UI logic from business logic. It enhances code maintainability and testability by using ViewModel to manage UI-related data lifecycle-aware components.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "How does Room Database work in Android?",
                "Room is a part of the Android Jetpack suite, providing an abstraction layer over SQLite to allow database access with minimal boilerplate code.",
                UUID.randomUUID()));

        questions.add(new Question(UUID.randomUUID(), "How to handle background tasks efficiently in Android?",
                "Android provides WorkManager, JobScheduler, and coroutines to handle background tasks efficiently, ensuring battery optimization and proper execution lifecycle management.",
                UUID.randomUUID()));

        return questions;
    }



}
