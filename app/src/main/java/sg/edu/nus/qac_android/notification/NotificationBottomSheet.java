package sg.edu.nus.qac_android.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.nus.qac_android.R;
import sg.edu.nus.qac_android.auth.AuthManager;
import sg.edu.nus.qac_android.data.dto.NotificationDTO;
import sg.edu.nus.qac_android.network.ApiService;
import sg.edu.nus.qac_android.network.RetrofitClient;
import sg.edu.nus.qac_android.utils.UUIDConverter;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class NotificationBottomSheet extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationDTO> notificationList = new ArrayList<>();
    private Button btnClear;
    private TextView tvNotificationCentre;
    private ApiService apiService;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment_bottom_sheet, container, false);

        recyclerView = view.findViewById(R.id.notification_recycler_view);
        btnClear = view.findViewById(R.id.btn_clear_notifications);
        tvNotificationCentre = view.findViewById(R.id.tv_notification_centre);

        authManager = new AuthManager(getContext());
        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        fetchNotifications();
        btnClear.setOnClickListener(v -> {
            if (notificationList.size() != 0) {
                clearAllNotifications();
            }
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

    private void fetchNotifications() {
        String userIdString = authManager.getUserId();
        Log.d("NotificationBottomSheet", "Fetching notifications for user: " + userIdString);
        UUID uuidId = UUIDConverter.convertStringToUUID(userIdString);
        apiService.getNotificationsById(uuidId).enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notificationList.clear();
                    notificationList.addAll(response.body());
                    String notificationText = "Notification Centre (" + notificationList.size() + ")";
                    tvNotificationCentre.setText(notificationText);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("NotificationBottomSheet", "Failed to load notifications: " + response.message());
                    Toast.makeText(getContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                Log.e("NotificationBottomSheet", "Failed fetching notifications: " + t.getMessage());
                Toast.makeText(getContext(), "Failed fetching notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAllNotifications() {
        String userIdString = authManager.getUserId();
        Log.d("NotificationBottomSheet", "Deleting all notifications for user: " + userIdString);
        UUID uuidId = UUIDConverter.convertStringToUUID(userIdString);
        apiService.deleteNotification(uuidId, 0).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("NotificationBottomSheet", "Cleared notifications type 0: " + response.message());
                } else {
                    Log.e("NotificationBottomSheet", "Failed clear notifications type 0: " + response.message());
                    Toast.makeText(getContext(), "Failed to clear notifications", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("NotificationBottomSheet", "Failed clearing notifications: " + t.getMessage());
                Toast.makeText(getContext(), "Failed clearing notifications", Toast.LENGTH_SHORT).show();
            }
        });
        apiService.deleteNotification(uuidId, 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("NotificationBottomSheet", "Cleared notifications type 1: " + response.message());
                } else {
                    Log.e("NotificationBottomSheet", "Failed clear notifications type 1: " + response.message());
                    Toast.makeText(getContext(), "Failed to clear notifications", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("NotificationBottomSheet", "Failed clearing notifications: " + t.getMessage());
                Toast.makeText(getContext(), "Failed clearing notifications", Toast.LENGTH_SHORT).show();
            }
        });
        apiService.deleteNotification(uuidId, 2).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("NotificationBottomSheet", "Cleared notifications type 2: " + response.message());
                } else {
                    Log.e("NotificationBottomSheet", "Failed clear notifications type 2: " + response.message());
                    Toast.makeText(getContext(), "Failed to clear notifications", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("NotificationBottomSheet", "Failed clearing notifications: " + t.getMessage());
                Toast.makeText(getContext(), "Failed clearing notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
