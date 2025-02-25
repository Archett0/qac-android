package sg.edu.nus.qac_android.notification;

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
import sg.edu.nus.qac_android.data.entity.Notification;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationText.setText(notification.getMessage());

        holder.itemView.setOnClickListener(v -> {
            String sender = notification.getSender();
            UUID questionId = notification.getId();
            Snackbar.make(v, "Question ID: " + questionId.toString() + " | Sender: " + sender, Snackbar.LENGTH_LONG).show();
            // TODO: Click and jump to question page
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationText = itemView.findViewById(R.id.notification_msg);
        }
    }
}
