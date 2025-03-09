package sg.edu.nus.qac_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.nus.qac_android.auth.AuthManager;
import sg.edu.nus.qac_android.auth.LoginActivity;
import sg.edu.nus.qac_android.data.entity.Question;
import sg.edu.nus.qac_android.databinding.ActivityMainBinding;
import sg.edu.nus.qac_android.network.ApiService;
import sg.edu.nus.qac_android.network.RetrofitClient;
import sg.edu.nus.qac_android.notification.NotificationBottomSheet;
import sg.edu.nus.qac_android.question.CreateQuestionActivity;
import sg.edu.nus.qac_android.splash.QuestionAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AuthManager authManager;
    private ApiService apiService;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager = new AuthManager(this);
        apiService = RetrofitClient.getClient(this).create(ApiService.class); // 传入 Context


        if (!authManager.isLoggedIn()) {
            Log.d("MainActivity", "User not logged in, redirecting to LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Q&A Platform");

        // 跳转到创建问题页面
        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CreateQuestionActivity.class));
        });

        recyclerView = findViewById(R.id.splash_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchQuestions(); // 🚀 从 API 获取所有问题
    }

    private void fetchQuestions() {
        Log.d("MainActivity", "Fetching questions from API...");

        apiService.getQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body();
                    adapter = new QuestionAdapter(questions, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.d("MainActivity", "Questions loaded successfully: " + questions.size());
                } else {
                    Log.e("MainActivity", "Failed to fetch questions: " + response.message());
                    Log.e("MainActivity", "API URL: " + call.request().url());
                    Toast.makeText(MainActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching questions: " + t.getMessage());
                Log.e("MainActivity", "API URL: " + call.request().url());
                Toast.makeText(MainActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            new NotificationBottomSheet().show(getSupportFragmentManager(), "NotificationBottomSheet");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Log.d("MainActivity", "Logging out...");
        authManager.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
