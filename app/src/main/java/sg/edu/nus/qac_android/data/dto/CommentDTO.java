package sg.edu.nus.qac_android.data.dto;

import java.util.UUID;

/**
 * @Author: Cooper
 * @Date: 3/9/2025
 * @Description:
 */
public class CommentDTO {
    String content;
    String ownerId;
    UUID answerId;

    public CommentDTO(String content, String ownerId, UUID answerId) {
        this.content = content;
        this.ownerId = ownerId;
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "content='" + content + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", answerId=" + answerId +
                '}';
    }
}
