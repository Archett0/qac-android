package sg.edu.nus.qac_android.comment;

import android.os.Bundle;
import android.text.TextUtils;
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
import sg.edu.nus.qac_android.data.dto.CommentDTO;
import sg.edu.nus.qac_android.data.entity.Comment;
import sg.edu.nus.qac_android.network.ApiService;
import sg.edu.nus.qac_android.network.RetrofitClient;
import sg.edu.nus.qac_android.utils.UUIDConverter;

/**
 * @Author: Cooper
 * @Date: 3/9/2025
 * @Description:
 */
public class CommentActivity extends AppCompatActivity {
    private TextView tvQuestionTitle, tvAnswerContent;
    private EditText etComment;
    private Button btnSubmit;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private String questionTitle;
    private String answerContent;
    private ApiService apiService;
    private AuthManager authManager;
    private UUID answerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Comment list");
        }

        tvQuestionTitle = findViewById(R.id.tv_question_title);
        tvAnswerContent = findViewById(R.id.tv_answer_content);
        etComment = findViewById(R.id.et_comment);
        btnSubmit = findViewById(R.id.btn_submit);
        rvComments = findViewById(R.id.rv_comments);

        answerId = UUIDConverter.convertStringToUUID(getIntent().getStringExtra("ANSWER_ID"));
        questionTitle = getIntent().getStringExtra("QUESTION_TITLE");
        answerContent = getIntent().getStringExtra("ANSWER_CONTENT");
        if (questionTitle == null) {
            questionTitle = "ERROR";
        }
        if (answerContent == null) {
            answerContent = "ERROR";
        }

        authManager = new AuthManager(this);
        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        tvQuestionTitle.setText(questionTitle);
        tvAnswerContent.setText(answerContent);

        commentAdapter = new CommentAdapter(commentList);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(commentAdapter);

        fetchComments(answerId);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = etComment.getText().toString().trim();
                if (TextUtils.isEmpty(commentText)) {
                    etComment.setError("You must input something...");
                    return;
                }
                submitComment();
            }
        });
    }

    private void fetchComments(UUID answerId) {
        Log.d("CommentActivity", "Fetching comments for answer: " + answerId);
        apiService.getCommentsByAnswerId(answerId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentList.clear();
                    commentList.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    Log.e("CommentActivity", "Failed to load comments: " + response.message());
                    Toast.makeText(CommentActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("CommentActivity", "Failed fetching comments: " + t.getMessage());
                Toast.makeText(CommentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void submitComment() {
        String commentText = etComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String userIdString = authManager.getUserId();
        if (userIdString == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (answerId == null) {
            Toast.makeText(this, "Answer ID error", Toast.LENGTH_SHORT).show();
        }
        CommentDTO commentDTO = new CommentDTO(commentText, userIdString, answerId);

        Log.d("CommentActivity", "Submitting comment: " + commentDTO.toString());

        apiService.createComment(commentDTO).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentList.add(0, response.body());
                    commentAdapter.notifyItemInserted(0);
                    rvComments.scrollToPosition(0);
                    etComment.setText("");
                    Toast.makeText(CommentActivity.this, "Comment submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("CommentActivity", "Failed to submit comment: " + response.message());
                    Toast.makeText(CommentActivity.this, "Failed to submit comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("CommentActivity", "Error submitting comment: " + t.getMessage());
                Toast.makeText(CommentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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