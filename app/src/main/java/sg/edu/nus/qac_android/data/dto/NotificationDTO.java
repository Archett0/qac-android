package sg.edu.nus.qac_android.data.dto;

import java.time.LocalDateTime;

/**
 * @Author: Cooper
 * @Date: 3/9/2025
 * @Description:
 */
public class NotificationDTO {

    private String message;

    private NotificationType notificationType;

    public NotificationDTO(String message, NotificationType notificationType) {
        this.message = message;
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public String toString() {
        String typeStr;
        if (notificationType != null) {
            switch (notificationType) {
                case ANSWER_POSTED:
                    typeStr = "New answer";
                    break;
                case COMMENT_POSTED:
                    typeStr = "New comment";
                    break;
                case UPVOTE_RECEIVED:
                    typeStr = "Upvote Received";
                    break;
                case DOWNVOTE_RECEIVED:
                    typeStr = "Downvote Received";
                    break;
                default:
                    typeStr = "Other Notification";
                    break;
            }
        } else {
            typeStr = "Unknown Notification";
        }
        return String.format("[%s]: %s", typeStr, message);
    }
}

