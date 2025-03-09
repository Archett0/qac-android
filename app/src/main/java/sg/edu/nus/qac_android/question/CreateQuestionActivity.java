package sg.edu.nus.qac_android.question;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.nus.qac_android.auth.AuthManager;
import sg.edu.nus.qac_android.data.entity.Question;
import sg.edu.nus.qac_android.databinding.ActivityCreateQuestionBinding;
import sg.edu.nus.qac_android.network.RetrofitClient;
import sg.edu.nus.qac_android.network.ApiService;
import sg.edu.nus.qac_android.utils.UUIDConverter;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private AuthManager authManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 AuthManager 和 API Service
        authManager = new AuthManager(this);
        apiService = RetrofitClient.getClient(this).create(ApiService.class); // 传入 Context

        // 设置 Toolbar
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Question");

        // 设置返回按钮点击事件
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        // 设置提交按钮点击事件
        binding.submitButton.setOnClickListener(view -> submitQuestion());
    }

    private void submitQuestion() {
        String title = binding.titleEditText.getText().toString().trim();
        String content = binding.contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            binding.titleEditText.setError("Title cannot be empty");
            binding.contentEditText.setError("Content cannot be empty");
            return;
        }

        // 获取用户 ID（String）
        String userIdString = authManager.getUserId();
        if (userIdString == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.e("CreateQuestionActivity", "Error: User ID is null");
            return;
        }

        // 转换 String 为 UUID
        UUID userId = UUIDConverter.convertStringToUUID(userIdString);
        if (userId == null) {
            Toast.makeText(this, "Invalid User ID format.", Toast.LENGTH_SHORT).show();
            Log.e("CreateQuestionActivity", "Error: User ID conversion failed");
            return;
        }

        // 生成问题 ID 和创建时间（ISO 8601 格式）
        UUID questionId = UUID.randomUUID();
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        // 创建 Question 对象
        Question newQuestion = new Question(questionId, title, content,userId);

        Log.d("CreateQuestionActivity", "Submitting question: " + newQuestion.toString());

        apiService.createQuestion(newQuestion).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateQuestionActivity.this, "Question submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("CreateQuestionActivity", "Failed to create question: " + response.message());
                    Toast.makeText(CreateQuestionActivity.this, "Failed to create question", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Log.e("CreateQuestionActivity", "Error: " + t.getMessage());
                Toast.makeText(CreateQuestionActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}