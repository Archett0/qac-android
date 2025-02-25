package sg.edu.nus.qac_android;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.nus.qac_android.data.entity.Question;
import sg.edu.nus.qac_android.databinding.ActivityMainBinding;
import sg.edu.nus.qac_android.notification.NotificationBottomSheet;
import sg.edu.nus.qac_android.splash.QuestionAdapter;

import android.view.Menu;
import android.view.MenuItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Q&A Platform");

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Link this to CREATE QUESTION
                Snackbar.make(view, "TODO: Link this to CREATE QUESTION", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.splash_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionList = getMockQuestions(); // TODO: Replace with API data
        adapter = new QuestionAdapter(questionList);
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

        if (id == R.id.action_notifications) {
            NotificationBottomSheet bottomSheet = new NotificationBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "NotificationBottomSheet");
            return true;
        } else if (id == R.id.action_user) {
            // TODO: Login/logout here
            Snackbar.make(findViewById(android.R.id.content), "User clicked", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
