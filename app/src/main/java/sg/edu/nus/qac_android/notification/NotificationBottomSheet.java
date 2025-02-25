package sg.edu.nus.qac_android.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import sg.edu.nus.qac_android.R;
import sg.edu.nus.qac_android.data.dto.NotificationType;
import sg.edu.nus.qac_android.data.entity.Notification;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class NotificationBottomSheet extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private Button btnClear;
    private TextView tvNotificationCentre;

    public NotificationBottomSheet() {
        // TODO: Mock data
        notificationList = new ArrayList<>();
        notificationList.add(new Notification(UUID.randomUUID(), UUID.randomUUID(), "John Doe",
                "New answer on: 'How to learn Java?'", LocalDateTime.now(), NotificationType.ANSWER_POSTED));

        notificationList.add(new Notification(UUID.randomUUID(), UUID.randomUUID(), "Jane Doe",
                "New comment on: 'Spring Boot and Android'", LocalDateTime.now(), NotificationType.COMMENT_POSTED));

        notificationList.add(new Notification(UUID.randomUUID(), UUID.randomUUID(), "Mike Lee",
                "New upvote on: 'RecyclerView in Android'", LocalDateTime.now(), NotificationType.UPVOTE_RECEIVED));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment_bottom_sheet, container, false);

        recyclerView = view.findViewById(R.id.notification_recycler_view);
        btnClear = view.findViewById(R.id.btn_clear_notifications);
        tvNotificationCentre = view.findViewById(R.id.tv_notification_centre);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        updateNotificationCentreText();
        btnClear.setOnClickListener(v -> {
            notificationList.clear();
            adapter.notifyDataSetChanged();
            dismiss();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Snackbar.make(activity.findViewById(android.R.id.content), "Notifications cleared", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void updateNotificationCentreText() {
        String notificationText = "Notification Centre (" + notificationList.size() + ")";
        tvNotificationCentre.setText(notificationText);
    }
}
