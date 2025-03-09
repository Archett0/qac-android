package sg.edu.nus.qac_android.splash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import sg.edu.nus.qac_android.R;
import sg.edu.nus.qac_android.comment.CommentActivity;
import sg.edu.nus.qac_android.data.entity.Answer;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<Answer> answerList;
    private Context context;
    private String questionTitle;

    public AnswerAdapter(List<Answer> answerList, Context context, String questionTitle) {
        this.answerList = answerList;
        this.context = context;
        this.questionTitle = questionTitle;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        holder.answerContent.setText(answer.getContent());

        holder.answerTime.setVisibility(View.GONE); // 隐藏时间的 TextView

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("ANSWER_ID", answer.getId().toString());
            intent.putExtra("QUESTION_TITLE", questionTitle);
            intent.putExtra("ANSWER_CONTENT", answer.getContent());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerContent, answerTime;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answerContent = itemView.findViewById(R.id.text_answer);
            answerTime = itemView.findViewById(R.id.text_time);
        }
    }
}
