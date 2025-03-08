package sg.edu.nus.qac_android.question;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.nus.qac_android.databinding.ActivityCreateQuestionBinding;

import java.util.Objects;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置 Toolbar
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Question");

        // 设置返回按钮点击事件
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        // 设置提交按钮的点击事件
        binding.submitButton.setOnClickListener(view -> {
            String title = binding.titleEditText.getText().toString().trim();
            String content = binding.contentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                binding.titleEditText.setError("Title cannot be empty");
                binding.contentEditText.setError("Content cannot be empty");
                return;
            }

            // TODO: 这里可以添加提交逻辑，例如调用 API 或保存数据
            binding.titleEditText.setText("");
            binding.contentEditText.setText("");
            finish(); // 提交后返回主页面
        });
    }
}
