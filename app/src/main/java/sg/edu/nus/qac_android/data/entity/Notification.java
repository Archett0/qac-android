package sg.edu.nus.qac_android.data.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import sg.edu.nus.qac_android.data.dto.NotificationType;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class Notification {
    private UUID id;
    private UUID toUserId;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
    private NotificationType notificationType;

    public Notification() {
    }

    public Notification(UUID id, UUID toUserId, String sender, String message, LocalDateTime sentAt, NotificationType notificationType) {
        this.id = id;
        this.toUserId = toUserId;
        this.sender = sender;
        this.message = message;
        this.sentAt = sentAt;
        this.notificationType = notificationType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getToUserId() {
        return toUserId;
    }

    public void setToUserId(UUID toUserId) {
        this.toUserId = toUserId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
