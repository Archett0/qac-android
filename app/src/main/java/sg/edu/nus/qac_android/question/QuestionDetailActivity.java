package sg.edu.nus.qac_android.question;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.nus.qac_android.R;
import sg.edu.nus.qac_android.auth.AuthManager;
import sg.edu.nus.qac_android.data.entity.Answer;
import sg.edu.nus.qac_android.network.ApiService;
import sg.edu.nus.qac_android.network.RetrofitClient;
import sg.edu.nus.qac_android.splash.AnswerAdapter;
import sg.edu.nus.qac_android.utils.UUIDConverter;

public class QuestionDetailActivity extends AppCompatActivity {

    private TextView questionTitle, questionContent;
    private EditText answerEditText;
    private Button submitAnswerButton;
    private RecyclerView answersRecyclerView;
    private AnswerAdapter answerAdapter;
    private List<Answer> answerList = new ArrayList<>();

    private ApiService apiService;
    private AuthManager authManager;
    private UUID questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // 设置 Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Question Detail");
        }

        // 绑定 UI 组件
        questionTitle = findViewById(R.id.question_title);
        questionContent = findViewById(R.id.question_content);
        answerEditText = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);
        answersRecyclerView = findViewById(R.id.answersRecyclerView);

        // 初始化 AuthManager 和 API Service
        authManager = new AuthManager(this);
        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        // 获取 Intent 传递的数据
        String questionIdString = getIntent().getStringExtra("QUESTION_ID");
        String title = getIntent().getStringExtra("QUESTION_TITLE");
        String content = getIntent().getStringExtra("QUESTION_CONTENT");

        // ✅ **使用 UUIDConverter 转换 `String` -> `UUID`**
        questionId = UUIDConverter.convertStringToUUID(questionIdString);
        if (questionId == null) {
            Log.e("QuestionDetail", "Invalid QUESTION_ID format");
            Toast.makeText(this, "Invalid Question ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("QuestionDetail", "Received QUESTION_ID: " + questionId);
        Log.d("QuestionDetail", "Received QUESTION_TITLE: " + title);
        Log.d("QuestionDetail", "Received QUESTION_CONTENT: " + content);

        questionTitle.setText(title);
        questionContent.setText(content != null ? content : "No content available");

        // 初始化答案列表
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        answerAdapter = new AnswerAdapter(answerList, QuestionDetailActivity.this, title);
        answersRecyclerView.setAdapter(answerAdapter);

        // 加载问题的所有回答
        fetchAnswers(questionId);

        // 提交答案按钮点击事件
        submitAnswerButton.setOnClickListener(v -> submitAnswer());
    }

    private void fetchAnswers(UUID questionId) {
        Log.d("QuestionDetail", "Fetching answers for question: " + questionId);
        apiService.getAnswersByQuestionId(questionId).enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    answerList.clear();
                    answerList.addAll(response.body());
                    answerAdapter.notifyDataSetChanged();
                    Log.d("QuestionDetail", "Answers loaded: " + answerList.size());
                } else {
                    Log.e("QuestionDetail", "Failed to load answers: " + response.message());
                    Toast.makeText(QuestionDetailActivity.this, "Failed to load answers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                Log.e("QuestionDetail", "Error fetching answers: " + t.getMessage());
                Toast.makeText(QuestionDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitAnswer() {
        String answerText = answerEditText.getText().toString().trim();
        if (answerText.isEmpty()) {
            Toast.makeText(this, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取当前用户 ID
        String userIdString = authManager.getUserId();
        UUID ownerId = UUIDConverter.convertStringToUUID(userIdString);
        if (ownerId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        UUID newAnswerId = UUID.randomUUID();
        Answer newAnswer = new Answer(newAnswerId, questionId, answerText, ownerId);

        Log.d("QuestionDetail", "Submitting answer: " + newAnswer.toString());

        apiService.submitAnswer(newAnswer).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    answerList.add(0, response.body()); // 把最新的答案放到顶部
                    answerAdapter.notifyItemInserted(0);
                    answersRecyclerView.scrollToPosition(0);
                    answerEditText.setText("");
                    Toast.makeText(QuestionDetailActivity.this, "Answer submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("QuestionDetail", "Failed to submit answer: " + response.message());
                    Toast.makeText(QuestionDetailActivity.this, "Failed to submit answer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.e("QuestionDetail", "Error submitting answer: " + t.getMessage());
                Toast.makeText(QuestionDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
