package sg.edu.nus.qac_android.splash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.UUID;

import sg.edu.nus.qac_android.R;

import sg.edu.nus.qac_android.data.entity.Question;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.splash_question_list_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.titleTextView.setText(question.getTitle());
        holder.contentTextView.setText(question.getShortContent());

        holder.itemView.setOnClickListener(v -> {
            // TODO: Jump to question page
            UUID questionId = question.getId();
            String questionTitle = question.getTitle();
            Snackbar.make(v, "Question ID: " + questionId.toString() + " | Title: " + questionTitle, Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            contentTextView = itemView.findViewById(R.id.text_content);
        }
    }
}
