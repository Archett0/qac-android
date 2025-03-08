package sg.edu.nus.qac_android.question;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import sg.edu.nus.qac_android.R;
import sg.edu.nus.qac_android.data.entity.Answer;
import sg.edu.nus.qac_android.splash.AnswerAdapter;

public class QuestionDetailActivity extends AppCompatActivity {

    private TextView questionTitle, questionContent;
    private EditText answerEditText;
    private Button submitAnswerButton;
    private RecyclerView answersRecyclerView;
    private AnswerAdapter answerAdapter;
    private List<Answer> answerList = new ArrayList<>();

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

        // 获取问题数据
        String questionIdString = getIntent().getStringExtra("QUESTION_ID");
        String title = getIntent().getStringExtra("QUESTION_TITLE");
        String content = getIntent().getStringExtra("QUESTION_CONTENT");
        Log.d("QuestionDetail", "Received QUESTION_CONTENT: " + content);
        questionTitle.setText(title);
        if (content != null && !content.isEmpty()) {
            questionContent.setText(content);
        } else {
            questionContent.setText("No content available");
        }

        // 初始化答案列表
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        answerAdapter = new AnswerAdapter(answerList);
        answersRecyclerView.setAdapter(answerAdapter);

        // 提交答案按钮点击事件
        submitAnswerButton.setOnClickListener(v -> {
            String answerText = answerEditText.getText().toString().trim();

            if (!answerText.isEmpty()) {
                try {
                    // 获取问题 ID（String 转 UUID）
                    UUID questionId = UUID.fromString(questionIdString);

                    // 生成新的 Answer
                    UUID newAnswerId = UUID.randomUUID(); // 生成唯一的 Answer ID
                    UUID userId = UUID.randomUUID(); // TODO: 这里需要替换成当前用户 ID
                    LocalDateTime createdAt = LocalDateTime.now();

                    Answer newAnswer = new Answer(newAnswerId, questionId, answerText, createdAt, userId);

                    // 将答案添加到列表
                    answerList.add(0, newAnswer); // 最新的答案显示在最上方
                    answerAdapter.notifyItemInserted(0);
                    answersRecyclerView.scrollToPosition(0);

                    // 清空输入框
                    answerEditText.setText("");

                } catch (IllegalArgumentException e) {
                    Log.e("SubmitAnswer", "Invalid QUESTION_ID format", e);
                }
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
